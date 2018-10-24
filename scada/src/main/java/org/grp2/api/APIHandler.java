package org.grp2.api;

import io.javalin.Context;
import org.grp2.Domain.*;
import org.grp2.Javalin.Message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIHandler {
    // private CubeFacade facade;

    public APIHandler() {
        // this.facade = facade
    }

    public void startNewProduction(Context context) {
        // this.facade.startNewProduction()
        context.json(new Message(200, "Production started")); // TODO return error if not started
    }

    public void manageProduction(Context context) {
        String choice = context.pathParam("choice");
        Message message = new Message(200, "");
        switch (choice) {
            case "start":
                //this.facade.start()
            break;
            case "stop":
                //this.facade.start()
            break;
            case "clear":
                //this.facade.start()
            break;
            case "abort":
                //this.facade.start()
            break;
            case "reset":
                //this.facade.start()
            break;
            default:
                message.setStatus(422);
                message.setMessage("Choice not supported");
        }
        context.status(message.getStatus());
        context.json(message);
    }

    public void viewScreen(Context context) {
        Map<String, Object> map = new HashMap<>();
        map.put("Measurements", new Measurements(0, 0, 0)); // TODO hent fra cube
        map.put("BatchOrder", new BatchOrder(0, 0, 0)); // TODO
        map.put("BatchData", new BatchData(0, 0, 0)); // TODO

        context.json(map);
    }

    public void viewLog(Context context) {
        int batchId = Integer.parseInt(context.pathParam("batch-id"));
        List<MeasurementLog> measurementLogs = new ArrayList<>();// TODO db.getMeasurementLogs(batchId)
        MeasurementLog measurementLog = new MeasurementLog(batchId, LocalDateTime.now(), 0, 0, 0);
        measurementLogs.add(measurementLog);

        List<StateTimeLog> stateTimeLogs = new ArrayList<>(); // TODO db.getStateTimeLogs(batchId)
        StateTimeLog stateTimeLog = new StateTimeLog(batchId, "fixme", 0);
        stateTimeLogs.add(stateTimeLog);

        Map<String, List> map = new HashMap<>();
        map.put("MeasurementLogs", measurementLogs);
        map.put("StateTimeLogs", stateTimeLogs);

        context.json(map);
    }
}
