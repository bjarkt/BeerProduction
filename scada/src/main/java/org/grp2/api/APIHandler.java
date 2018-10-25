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
        Message message = new Message(200, "Succes");
        ProductionInformation pi = context.bodyAsClass(ProductionInformation.class);
        int batchId = scadaDao.createBatch(pi);

        if (batchId == -1) {
            message.setStatus(422);
            message.setMessage("Order already has that type of beer");
        } else {
            Recipe recipe = scadaDao.getRecipe(pi.getRecipeName());
            pi.setBatchId(batchId);

            hardwareProvider.setBatchId(batchId);
            hardwareProvider.setProduct(recipe.getId());
            hardwareProvider.setAmountToProduce(pi.getQuantity());
            hardwareProvider.setMachSpeed(pi.getMachineSpeed());
            if(!pi.validateMachSpeed(recipe.getMinSpeed(), recipe.getMaxSpeed())){
                message.setStatus(422);
                message.setMessage("Invalid Speed");
            }
            hardwareProvider.stop();
            hardwareProvider.reset();
            hardwareProvider.start();
        }

        context.json(message);
    }

    public void manageProduction(Context context) {
        String choice = context.pathParam("choice");
        Message message = new Message(200, "");
        switch (choice) {
            case "start":
                hardwareProvider.start();
                break;
            case "stop":
                hardwareProvider.stop();
                break;
            case "clear":
                hardwareProvider.clear();
                break;
            case "abort":
                hardwareProvider.abort();
                break;
            case "reset":
                hardwareProvider.reset();
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
        Measurements measurements = new Measurements(0, 0, 0);
        BatchOrder batchorder = new BatchOrder(0, 0, 0);
        BatchData batchdata = new BatchData(0, 0, 0);


        measurements.setTemperature(hardwareProvider.getTemperature());
        measurements.setHumidity(hardwareProvider.getHumidity());
        measurements.setVibration(hardwareProvider.getVibration());

        batchorder.setBatchId((int) hardwareProvider.getBatchId());
        batchorder.setAmountToProduce((int) hardwareProvider.getAmountToProduce());

        LocalDateTime started = scadaDao.getBatchStartTime(batchorder.getBatchId());
        LocalDateTime now = LocalDateTime.now();
        long minutes = started.until(now, ChronoUnit.MINUTES);
        batchorder.setProductsPerMinute((int) ((batchorder.getAmountToProduce()) / minutes));

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
