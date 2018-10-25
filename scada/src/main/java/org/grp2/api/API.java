package org.grp2.api;

import io.javalin.Javalin;
import org.grp2.Javalin.JavalinSetup;
import org.grp2.hardware.Hardware;
import org.grp2.hardware.IHardware;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API {
    private final int PORT = 7000;

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);
        APIHandler handler = new APIHandler(new Hardware(IHardware.SIMULATION_URL));
        setRoutes(app, handler);
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                post("/start-new-production", handler::startNewProduction);
                post("/manage-production/:choice", handler::manageProduction);
                get("/view-screen", handler::viewScreen);
                get("/view-log/:batch-id", handler::viewLog);
            });
        });
    }

}
