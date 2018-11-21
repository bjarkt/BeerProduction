package org.grp2.api;

import io.javalin.Context;
import org.grp2.javalin.Message;
import org.grp2.dao.ErpDAO;
import org.grp2.shared.Order;
import org.grp2.shared.OrderItem;

import java.util.List;

public class APIHandler {

    private ErpDAO erpDAO;

    public APIHandler(ErpDAO erpDAO) {
        this.erpDAO = erpDAO;
    }

    public void createOrder(Context context) {
        int orderNumber = erpDAO.getNewOrderNumber();
        context.json(new Message(200, Integer.toString(orderNumber)));
    }

    public void addOrderItem(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));
        String beerName = context.pathParam("beer-name");
        int quantity = Integer.parseInt(context.pathParam("quantity"));

        boolean success = erpDAO.addOrderItem(orderNumber, beerName, quantity);

        if(success) context.json(new Message(200, "Order item added to order " + orderNumber + " with " + quantity + " " +  beerName));
        else context.json(new Message(403, "Order number doesn't exist or you are trying to add same beers to the same order."));
    }

    public void deleteOrder(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));

        boolean isOrderDeleted = erpDAO.deleteOrder(orderNumber);

        if(isOrderDeleted){
            context.json(new Message(200, "Order " + orderNumber + " has been deleted."));
        } else {
            context.json(new Message(403, "Order "+ orderNumber + " cannot be deleted. The order is being processed."));
        }

    }

    public void editOrderItem(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));
        String beerName = context.pathParam("beer-name");
        String quantityParam =  context.queryParam("quantity");
        Integer quantity = quantityParam != null ? Integer.parseInt(quantityParam) : null;
        String newBeerName = context.queryParam("new-beer-name");

        boolean isEdited = false;
        if(quantity != null && newBeerName != null) isEdited = erpDAO.editOrderItem(orderNumber, beerName, quantity, newBeerName);
        else if(quantity != null) isEdited = erpDAO.editOrderItem(orderNumber, beerName, quantity);
        else if (newBeerName != null) isEdited = erpDAO.editOrderItem(orderNumber, beerName, newBeerName);

        if(isEdited) context.json(new Message(200, "Order edited!"));
        else context.json(new Message(403, "Order item cannot be edited. Either the order item doesn't exist, the order already contains the beer type or the order is being processed."));
    }

    public void deleteOrderItem(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));
        String beerName = context.pathParam("beer-name");

        boolean isOrderItemDeleted = erpDAO.deleteOrderItem(orderNumber, beerName);

        if(isOrderItemDeleted){
            context.json(new Message(200, "Order item with order number: " + orderNumber + " for beer type " + beerName + " has been deleted."));
        } else {
            context.json(new Message(403, "Order item "+ orderNumber + " cannot be deleted. The order item doesn't exist or it is being processed."));
        }
    }

    public void lockOrder(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));
        erpDAO.lockOrder(orderNumber);
        context.json(new Message(200, "Order with order number: " + orderNumber + " has been locked."));
    }

    public void viewOrderItems(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));

        List<OrderItem> orderItems = erpDAO.viewOrderItems(orderNumber);

        context.json(orderItems);
    }

    public void viewOrderDetails(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));

        Order order = erpDAO.viewOrderDetails(orderNumber);

        context.json(order);
    }

    public void viewOrders(Context context) {
        String status = context.pathParam("status");
        List<Order> orders = erpDAO.getOrders(status);
        context.json(orders);
    }


}
