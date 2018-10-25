package org.grp2.api;

import io.javalin.Context;
import org.grp2.dao.ScadaDAO;
import org.grp2.domain.*;
import org.grp2.Javalin.Message;
import org.grp2.hardware.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class APIHandler {
    // private CubeFacade facade;
    private ScadaDAO scadaDao;
    private IHardwareProvider hardwareProvider;

    public APIHandler(IHardware hardware) {
        // this.facade = facade
        this.scadaDao = new ScadaDAO();
        this.hardwareProvider = hardware.getProvider();
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
        Measurements measurements = new Measurements(0,0,0);
        BatchOrder batchorder = new BatchOrder(0,0,0);
        BatchData batchdata = new BatchData(0,0,0);


        measurements.setTemperature(hardwareProvider.getTemperature());
        measurements.setHumidity(hardwareProvider.getHumidity());
        measurements.setVibration(hardwareProvider.getVibration());

        batchorder.setBatchId((int) hardwareProvider.getBatchId());
        batchorder.setAmountToProduce((int) hardwareProvider.getAmountToProduce());

        LocalDateTime started = scadaDao.getBatchStartTime(batchorder.getBatchId());
        LocalDateTime now = LocalDateTime.now();
        long minutes = started.until(now, ChronoUnit.MINUTES);
        batchorder.setProductsPerMinute((int)((hardwareProvider.getCurrentBeersProduced()) / minutes));

        batchdata.setProduced(hardwareProvider.getCurrentBeersProduced());
        batchdata.setAcceptable(hardwareProvider.getAcceptedBeersProduced());
        batchdata.setDefect(hardwareProvider.getDefectiveBeersProduced());


        map.put("Measurements", measurements);
        map.put("BatchOrder", batchorder);
        map.put("BatchData", batchdata);

        context.json(map);
    }

    public void viewLog(Context context) {
        int batchId = Integer.parseInt(context.pathParam("batch-id"));

        Map<String, List> map = new HashMap<>();
        map.put("MeasurementLogs", scadaDao.getMeasurementLogs(batchId));
        map.put("StateTimeLogs", scadaDao.getStateTimeLogs(batchId));

        context.json(map);
    }
}
