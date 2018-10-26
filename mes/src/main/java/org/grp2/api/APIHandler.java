package org.grp2.api;

import io.javalin.Context;
import org.grp2.Javalin.Message;
import org.grp2.dao.MesDAO;
import org.grp2.domain.Plant;
import org.grp2.shared.Batch;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;

import java.util.List;

public class APIHandler {

    private MesDAO mesDAO;

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

}
