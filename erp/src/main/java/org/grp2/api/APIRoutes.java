package org.grp2.api;

public interface APIRoutes {
    String CREATE_ORDER = "/create-order";
    String ADD_ORDER_ITEM = "/add-order-item/:order-id/:beer-name/:quantity";
    String DELETE_ORDER = "/delete-order/:order-id";
    String EDIT_ORDER_ITEM = "/edit-order-item/:order-id/:beer-name";
    String DELETE_ORDER_ITEM = "/delete-order-item/:order-id/:beer-name";
    String LOCK_ORDER = "/lock-order/:order-id";
    String VIEW_ORDER_ITEMS = "/view-order-items/:order-id";
    String VIEW_ORDER_DETAILS = "/view-order-details/:order-id";
    String VIEW_ORDERS = "/view-orders/:status";
}
