package org.grp2.api;

import io.javalin.Javalin;
import org.grp2.Javalin.JavalinSetup;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API {
    private final int PORT = 7001;

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);
        APIHandler handler = new APIHandler();
        setRoutes(app, handler);
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                get("/view-orders", handler::viewOrders);
                get("/view-order-items/:order-number", handler::viewOrderItems);
                get("/view-all-batches", handler::viewAllBatches);
                get("/view-current-batch-status/:order-number", handler::viewCurrentBatchStatus);
                get("/view-plant-statistics", handler::viewPlantStatistics);
                post("/create-batches/:batches/", handler::createBatches);
            });
        });
    }

}
