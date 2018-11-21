package org.grp2.api;

public interface APIRoutes {
    String VIEW_ORDERS = "/view-orders";
    String VIEW_ORDER_ITEMS = "/view-order-items/:order-number";
    String VIEW_ALL_BATCHES = "/view-all-batches";
    String VIEW_PLANT_STATISTICS = "/get-plant-statistics";
    String GET_REPORT = "/get-report/:batch-id";
    String CREATE_BATCHES = "/create-batches/";
    String GET_OEE = "/get-oee/:batch-id";
}
