package org.grp2.api;

import io.javalin.Context;
import org.grp2.Javalin.Message;
import org.grp2.dao.ErpDAO;
import org.grp2.domain.OrderItem;

import java.util.Arrays;
import java.util.List;

public class APIHandler {

    private ErpDAO erpDAO;

    public APIHandler() {
        // this.facade = facade
        this.erpDAO = new ErpDAO();

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
        int quantity = Integer.parseInt(context.pathParam("quantity"));
        String newBeerName = context.pathParam("new-beer-name");

        boolean isEdited = erpDAO.editOrderItem(orderNumber, beerName, quantity, newBeerName);

        context.json(new Message(200, "Order edited!"));
    }

    public void deleteOrderItem(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));
        String beerName = context.pathParam("beer-name");

        boolean isOrderItemDeleted = erpDAO.deleteOrderItem(orderNumber, beerName);

        if(isOrderItemDeleted){
            context.json(new Message(200, "Order item with order number: " + orderNumber + " for beer type " + beerName + " has been deleted."));
        } else {
            context.json(new Message(403, "Order item "+ orderNumber + " cannot be deleted. The order is being processed."));
        }
    }

    public void viewOrder(Context context){
        int orderNumber = Integer.parseInt(context.pathParam("order-id"));

        List<OrderItem> orderItems = erpDAO.viewOrder(orderNumber);

        context.json(orderItems);
    }


}
