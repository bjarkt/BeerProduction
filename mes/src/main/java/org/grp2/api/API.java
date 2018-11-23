package org.grp2.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import io.javalin.Javalin;
import org.grp2.data.MesDAO;
import org.grp2.utility.UnirestWrapper;
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
        APIHandler handler = new APIHandler(new MesDAO(), new UnirestWrapper());
        setRoutes(app, handler);
        setUniRestMapper();
        app.start();
    }

    public void setRoutes(Javalin app, APIHandler handler) {
        app.routes(() -> {
            path("/api", () -> {
                get(APIRoutes.VIEW_ORDERS, handler::viewOrders);
                get(APIRoutes.VIEW_ORDER_ITEMS, handler::viewOrderItems);
                get(APIRoutes.VIEW_ALL_BATCHES, handler::viewAllBatches);
                get(APIRoutes.VIEW_PLANT_STATISTICS, handler::viewPlantStatistics);
                get(APIRoutes.GET_REPORT, handler::getReport);
                post(APIRoutes.CREATE_BATCHES, handler::createBatches);
                get(APIRoutes.GET_OEE, handler::getOEE);
                get(APIRoutes.GET_PROFITABLE_MACH_SPEED, handler::getProfitableMachSpeed);
                get(APIRoutes.GET_FASTEST_MACH_SPEED, handler::getFastestMachSpeed);
                get(APIRoutes.GET_SAVING_MACH_SPEED, handler::getSavingMachSpeed);
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
