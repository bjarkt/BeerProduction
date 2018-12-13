package org.grp2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.javalin.Context;
import org.grp2.data.MesDAO;
import org.grp2.domain.OEE;
import org.grp2.domain.Plant;
import org.grp2.domain.PlantStatistics;
import org.grp2.enums.OrderItemStatus;
import org.grp2.javalin.Message;
import org.grp2.shared.*;
import org.grp2.utility.UnirestWrapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIHandler {

    private Plant plant;
    private com.fasterxml.jackson.databind.ObjectMapper mapper;

    public APIHandler(MesDAO mesDAO, UnirestWrapper unirestWrapper) {
        this.plant = new Plant(mesDAO, unirestWrapper);
        mapper = new com.fasterxml.jackson.databind.ObjectMapper();
    }

    public void viewOrders(Context context) {
        List<Order> orders = plant.getMesDAO().getLockedOrders();
        context.json(orders);
    }


    public void viewOrderItems(Context context) {
        int orderNumber = Integer.parseInt(context.pathParam("order-number"));
        Map<OrderItem, Recipe> orderItems = plant.getMesDAO().getOrderItems(orderNumber);
        Map<String, Object> recipeAndItem = new HashMap<>();
        recipeAndItem.put("Recipe", orderItems.values());
        recipeAndItem.put("OrderItems", orderItems.keySet());

        context.json(recipeAndItem);
    }

    public void viewAllBatches(Context context) {

        List<Batch> batches = plant.getMesDAO().viewAllBatches();
        context.json(batches);
    }

    public void viewPlantStatistics(Context context) {
        String daysParam = context.queryParam("days");
        try {
            Integer days = daysParam != null ? Integer.parseInt(daysParam) : 1;
            PlantStatistics plantStatistics = plant.getMesDAO().viewPlantStatistics(LocalDateTime.now().minusDays(days), LocalDateTime.now());
            context.json(plantStatistics);
        } catch (NumberFormatException nfe) {
            context.json(new Message(422, "Bad value for query-param: " + daysParam));
        }
    }

    public void createBatches(Context context) {
        Message message = new Message(200, "Success");

        List<ProductionInformation> orderList;

        try {
            Map<String, List<ProductionInformation>> temp = mapper.readValue(context.body(), new TypeReference<Map<String, ArrayList<ProductionInformation>>>() {
            });

            orderList = temp.get("orderItems");

            plant.getMesDAO().addToQueueItems(orderList);

            try {
                String url = "http://localhost:7000/api/start-new-production";
                Message postMessage = plant.getUnirestWrapper().post(url, Message.class);
                if (postMessage != null) {
                    System.out.println("Message from SCADA: " + postMessage.getMessage());
                    message.set(postMessage.getStatus(), postMessage.getMessage());
                } else {
                    message.set(422, "Error from SCADA : scada service probably not running");
                }
            } catch (UnirestException e) {
                message.set(422, "Error from SCADA : " + e.getMessage());
            }

            if (!orderList.isEmpty())
                plant.getMesDAO().setOrderItemStatus(OrderItemStatus.PROCESSING, orderList.get(0).getOrderNumber());

        } catch (IOException e) {
            message.set(422, "JSON error : " + e.getMessage());
        }

        context.json(message);
    }

    public void getReport(Context context) {
        int batchID = Integer.parseInt(context.pathParam("batch-id"));
        List<MeasurementLog> measurementLogs = plant.getMesDAO().getMeasurementLogs(batchID);
        Batch batch = plant.getMesDAO().getBatch(batchID);

        if (batch != null && measurementLogs != null) {
            MeasurementLog[] mesLogs = new MeasurementLog[measurementLogs.size()];
            mesLogs = measurementLogs.toArray(mesLogs);

            plant.getPrintManager().writeDocument(batch, mesLogs);
            byte[] report = plant.getPrintManager().getDocument();
            context.header("Content-Type", "application/pdf");
            context.header("Content-Length", String.valueOf(report.length));
            context.header("Content-Disposition", "attachment;filename=batch-report.pdf");
            context.result(new ByteArrayInputStream(report));
        } else {
            context.json(new Message(404, "No measurement logs exists."));
        }

    }

    public void getOEE(Context context) {
        int batchID = Integer.parseInt(context.pathParam("batch-id"));
        OEE oee = plant.getMesDAO().getOEE(batchID);
        context.json(oee);
    }

    public void getProfitableMachSpeed(Context context) {
        String beerType = context.pathParam("beer-type");
        Beer beer = plant.getMesDAO().getBeerData(beerType);
        int machSpeed = plant.getOptimizer().getOptimalMachSpeed(beer);
        context.json(machSpeed);
    }

    public void getFastestMachSpeed(Context context) {
        String beerType = context.pathParam("beer-type");
        int quantity = Integer.parseInt(context.pathParam("quantity"));
        Beer beer = plant.getMesDAO().getBeerData(beerType);
        int machSpeed = plant.getOptimizer().getFastestMachSpeed(beer, quantity);
        context.json(machSpeed);
    }

    public void getSavingMachSpeed(Context context) {
        String beerType = context.pathParam("beer-type");
        Beer beer = plant.getMesDAO().getBeerData(beerType);
        int machSpeed = plant.getOptimizer().getMostSavingMachSpeed(beer);
        context.json(machSpeed);
    }

}
