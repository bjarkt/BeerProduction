package org.grp2.api;

import io.javalin.Javalin;
import org.grp2.dao.ErpDAO;
import org.grp2.javalin.AbstractAPI;
import org.grp2.javalin.JavalinSetup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API extends AbstractAPI {

    public API(int port) {
        super(port);
    }

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);
        APIHandler handler = new APIHandler(new ErpDAO());
        setRoutes(app, handler);
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                post(APIRoutes.CREATE_ORDER, handler::createOrder);
                post(APIRoutes.ADD_ORDER_ITEM, handler::addOrderItem);
                post(APIRoutes.DELETE_ORDER, handler::deleteOrder);
                post(APIRoutes.EDIT_ORDER_ITEM, handler::editOrderItem);
                post(APIRoutes.DELETE_ORDER_ITEM, handler::deleteOrderItem);
                post(APIRoutes.LOCK_ORDER, handler::lockOrder);
                get(APIRoutes.VIEW_ORDER_ITEMS, handler::viewOrderItems);
                get(APIRoutes.VIEW_ORDER_DETAILS, handler::viewOrderDetails);
                get(APIRoutes.VIEW_ORDERS, handler::viewOrders);
            });
        });
    }

}
