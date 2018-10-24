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
                post("/delete-order/:order-id", handler::deleteOrder);
                post("/edit-order/:order-id", handler::editOrder);
                get("/view-order/:order-id", handler::viewOrder);
            });
        });
    }

}
