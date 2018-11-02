package org.grp2.shared;

import java.sql.Timestamp;
import java.time.LocalDateTime;

public class Order {

    private String status;
    private LocalDateTime date;
    private int orderNumber;

    public Order(int orderNumber){
        this.orderNumber = orderNumber;
    }

    public Order(int orderNumber, Timestamp date, String status){
        this.orderNumber = orderNumber;
        this.date = date.toLocalDateTime();
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(Timestamp date) {
        this.date = date.toLocalDateTime();
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
