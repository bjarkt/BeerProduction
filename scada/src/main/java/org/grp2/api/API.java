package org.grp2.api;

import io.javalin.Javalin;
import org.grp2.javalin.AbstractAPI;
import org.grp2.javalin.JavalinSetup;
import org.grp2.hardware.Hardware;
import org.grp2.hardware.IHardware;
import org.grp2.utility.DockerUtility;

import java.util.concurrent.atomic.AtomicReference;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API extends AbstractAPI {

    public API(int port) {
        super(port);
    }

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);

        String url;
        if (DockerUtility.getBooleanEnv("useSimulation")) {
            url = DockerUtility.dockerValueOrDefault(IHardware.DOCKER_SIMULATION_URL, IHardware.SIMULATION_URL);
        } else {
            url = IHardware.CUBE_URL;
        }
        APIHandler handler = new APIHandler(new Hardware(url));
        setRoutes(app, handler);
        app.start();
    }

    private void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                post("/start-new-production", handler::startNewProduction);
                post("/manage-production/:choice", handler::manageProduction);
                get("/view-screen", handler::viewScreen);
                get("/view-log/", handler::viewLog);
            });
        });
    }

}
