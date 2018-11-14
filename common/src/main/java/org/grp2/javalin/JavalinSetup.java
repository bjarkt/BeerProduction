package org.grp2.javalin;

import io.javalin.Javalin;

public class JavalinSetup {
    public static Javalin setup(int port) {
        Javalin app = Javalin.create()
                .enableRouteOverview("/routes")
                .enableCorsForAllOrigins()
                .port(port);

        return app;
    }
}
