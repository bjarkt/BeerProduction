package org.grp2.api;

import io.javalin.Javalin;
import org.grp2.Javalin.JavalinSetup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API {
    private final int PORT = 7002;

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);
        APIHandler handler = new APIHandler();
        setRoutes(app, handler);
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                post("/create-order", handler::createOrder);
                post("/add-order-item/:order-id/:beer-name/:quantity", handler::addOrderItem);
                post("/delete-order/:order-id", handler::deleteOrder);
                post("/edit-order-item/:order-id/:beer-name", handler::editOrderItem);
                post("/delete-order-item/:order-id/:beer-name", handler::deleteOrderItem);
                get("/view-order-items/:order-id", handler::viewOrderItems);
                get("/view-order-details/:order-id", handler::viewOrderDetails);
            });
        });
    }

}
