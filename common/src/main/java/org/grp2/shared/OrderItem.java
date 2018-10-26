package org.grp2.shared;

public class OrderItem {

    private int orderNumber;
    private String beerName;
    private int quantity;
    private String status;

    public OrderItem(int orderNumber, String beerName, int quantity, String status){
        this.orderNumber = orderNumber;
        this.beerName = beerName;
        this.quantity = quantity;
        this.status = status;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getBeerName() {
        return beerName;
    }

    public String getStatus() {
        return status;
    }
}
