package org.grp2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.javalin.Context;
import org.grp2.javalin.Message;
import org.grp2.dao.MesDAO;
import org.grp2.domain.Plant;
import org.grp2.enums.OrderItemStatus;
import org.grp2.shared.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class APIHandler {

    private Plant plant;
    private ObjectMapper mapper;

    public APIHandler() {
        this.plant = Plant.getInstance();
        mapper = new ObjectMapper();
    }

    public void viewOrders(Context context) {
        List<Order> orders = plant.getMesDAO().getLockedOrders();
        context.json(orders);
    }


    public void viewOrderItems(Context context) {
        int orderNumber = Integer.parseInt(context.pathParam("order-number"));
        Map<OrderItem, Recipe> orderItems = plant.getMesDAO().getOrderItems(orderNumber);
        context.json(orderItems);
    }

    public void viewAllBatches(Context context) {

        List<Batch> batches = plant.getMesDAO().viewAllBatches();
        context.json(batches);
    }

    public void viewPlantStatistics(Context context) {

        //mesDAO.viewPlantStatistics();

        //context.json(statascstics);

    }

    public void createBatches(Context context) {
        Message message = new Message(200, "Success");

        List<ProductionInformation> orderList;

        try {
            Map<String, List<ProductionInformation>> temp = mapper.readValue(context.body(), new TypeReference<Map<String, List<ProductionInformation>>>() {
            });

            orderList = temp.get("orderItems");

            plant.getMesDAO().addToQueueItems(orderList);

            try {
                HttpResponse<Message> postMessage = Unirest.post("http://localhost:7000/api/start-new-production").asObject(Message.class);
            } catch (UnirestException e) {
                message.set(422, "JSON error : " + e.getMessage());
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
        // TODO: Get byte[] from PrintManager
        //byte[] report = plant.getPrintManager().getDocument(batch, measurementLogs);
        context.header("Content-Type", "application/pdf");
        //context.header("Content-Length", String.valueOf(report.length));
        context.header("Content-Disposition", "attachment;filename=batch-report.pdf");
        //context.result(new ByteArrayInputStream(report));
    }

}