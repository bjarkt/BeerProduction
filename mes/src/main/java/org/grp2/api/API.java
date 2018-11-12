package org.grp2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import io.javalin.Javalin;
import org.grp2.javalin.AbstractAPI;
import org.grp2.javalin.JavalinSetup;

import java.io.IOException;

import static io.javalin.apibuilder.ApiBuilder.*;

public class API extends AbstractAPI {

    public API(int port) {
        super(port);
    }

    public void start() {
        Javalin app = JavalinSetup.setup(PORT);
        APIHandler handler = new APIHandler();
        setRoutes(app, handler);
        setUniRestMapper();
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                get("/view-orders", handler::viewOrders);
                get("/view-order-items/:order-number", handler::viewOrderItems);
                get("/view-all-batches", handler::viewAllBatches);
                get("/get-plant-statistics", handler::viewPlantStatistics);
                get("/get-report/:batch-id", handler::getReport);
                post("/create-batches/", handler::createBatches);
                get("/get-oee/:batch-id", handler::getOEE);
            });
        });
    }

    private void setUniRestMapper () {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

}
