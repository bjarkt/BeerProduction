package org.grp2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import io.javalin.Context;
import org.grp2.Javalin.Message;
import org.grp2.dao.MesDAO;
import org.grp2.domain.Plant;
import org.grp2.enums.OrderItemStatus;
import org.grp2.shared.Batch;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;
import org.grp2.shared.ProductionInformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIHandler {

    private MesDAO mesDAO;
    private ObjectMapper mapper;

    public APIHandler() {
        // this.facade = facade
        this.mesDAO = new MesDAO();

    }

    public void viewOrders(Context context) {

        List<Order> orders = mesDAO.viewOrders();
        context.json(orders);
    }

    public void viewOrderItems(Context context) {

        int orderNumber = Integer.parseInt(context.pathParam("order-number"));

        List<OrderItem> orderItems = mesDAO.viewOrderItems(orderNumber);
        context.json(orderItems);

    }

    public void viewAllBatches(Context context) {

        List<Batch> batches = mesDAO.viewAllBatches();
        context.json(batches);
    }

    public void viewCurrentBatchStatus(Context context) {

        int batchId = Integer.parseInt(context.pathParam("order-number"));

        String status = mesDAO.viewCurrentBatchStatus(batchId);

        context.json(status);

    }

    public void viewPlantStatistics(Context context) {

        Plant plant  = mesDAO.viewPlantStatistics();

        context.json(plant);

    }

    /**
     *
     * @param context
     */
    public void createBatches(Context context) {
        Message message = new Message(200, "Success");

        Map<OrderItem, String> orderItems;
        List<ProductionInformation> orderList = new ArrayList<>();
        int orderNumber;

        try {
            orderItems = mapper.readValue(context.body(), new TypeReference<Map<OrderItem, String>>() {});

            for (Map.Entry<OrderItem, String> orderItemStringEntry : orderItems.entrySet()) {
                String recipeName = orderItemStringEntry.getKey().getBeerName();
                orderNumber = orderItemStringEntry.getKey().getOrderNumber();
                int quantity = orderItemStringEntry.getKey().getQuantity();
                int machineSpeed = Integer.parseInt(orderItemStringEntry.getValue());

                orderList.add(new ProductionInformation(recipeName, orderNumber, machineSpeed, quantity));
            }

            mesDAO.addToQueueItems(orderList);

            Unirest.post("localhost:7000/API/start-new-production");

            mesDAO.setOrderItemStatus(OrderItemStatus.PROCESSING, orderList.get(0).getOrderNumber());


        } catch (IOException e) {
            message.set(422, "JSON error : " +  e.getMessage());
        }

        context.json(message);
    }

}
