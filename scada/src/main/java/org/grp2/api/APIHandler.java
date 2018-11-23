package org.grp2.api;

import io.javalin.Context;
import org.grp2.domain.*;
import org.grp2.javalin.Message;
import org.grp2.enums.State;
import org.grp2.shared.Batch;
import org.grp2.shared.Measurements;

import java.util.*;

public class APIHandler {
    private Machinery machinery;

    public APIHandler(Machinery machinery) {
        this.machinery = machinery;
    }

    public void startNewProduction(Context context) {
        Message message = new Message(200, "Success");
        try {
            message = machinery.startBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        message.send(context);
    }


    public void manageProduction(Context context) {
        String choice = context.pathParam("choice");
        Message message = new Message(200, "");
        switch (choice) {
            case "start":
                machinery.getHardware().getProvider().start();
                break;
            case "stop":
                machinery.getHardware().getProvider().stop();
                break;
            case "clear":
                machinery.getHardware().getProvider().clear();
                break;
            case "abort":
                machinery.getHardware().getProvider().abort();
                break;
            case "reset":
                machinery.getHardware().getProvider().reset();
                break;
            default:
                message.setStatus(422);
                message.setMessage("Choice not supported");
        }

        message.send(context);
    }

    public void viewScreen(Context context) {
        Map<String, Object> map = new HashMap<>();
        Measurements measurements = new Measurements(0, 0, 0);
        BatchViewOrder batchorder = new BatchViewOrder(0, 0, 0);
        BatchViewData batchdata = new BatchViewData(0, 0, 0);


        measurements.setTemperature(machinery.getHardware().getProvider().getTemperature());
        measurements.setHumidity(machinery.getHardware().getProvider().getHumidity());
        measurements.setVibration(machinery.getHardware().getProvider().getVibration());

        batchorder.setBatchId((int) machinery.getHardware().getProvider().getBatchId());
        batchorder.setAmountToProduce((int) machinery.getHardware().getProvider().getAmountToProduce());
        batchorder.setProductsPerMinute((int) machinery.getHardware().getProvider().getMachSpeed());

        batchdata.setProduced(machinery.getHardware().getProvider().getCurrentBeersProduced());
        batchdata.setAcceptable(machinery.getHardware().getProvider().getAcceptedBeersProduced());
        batchdata.setDefect(machinery.getHardware().getProvider().getDefectiveBeersProduced());


        map.put("Measurements", measurements);
        map.put("BatchOrder", batchorder);
        map.put("BatchData", batchdata);
        map.put("State", State.fromCode(machinery.getHardware().getProvider().getState()));
        map.put("AmountInQueue", machinery.getScadaDAO().getQueueItems().size());
        map.put("BatchInfo", machinery.getScadaDAO().getCurrentBatch());

        context.json(map);
    }

    public void viewLog(Context context) {
        String batchParam = context.queryParam("batch-id");

        Integer batchId = null;
        try {
            if (batchParam != null)
                batchId = Integer.parseInt(batchParam);
        } catch (NumberFormatException e) {
            context.json(new Message(422, "Bad value for query-param: " + batchParam));
            return;
        }

        Batch currentBatch = machinery.getScadaDAO().getCurrentBatch();
        Map<String, List> map = new HashMap<>();
        if (batchId == null && currentBatch != null)
            batchId = currentBatch.getBatchId();

        if (batchId != null)
        {
            map.put("MeasurementLogs", machinery.getScadaDAO().getMeasurementLogs(batchId));
            map.put("StateTimeLogs", machinery.getScadaDAO().getStateTimeLogs(batchId));
            context.json(map);
        } else {
            context.json(new Message(422, "There need to be batch running if no batch id is sent"));
        }
    }
}
