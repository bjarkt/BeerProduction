package org.grp2.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Context;
import org.grp2.dao.ScadaDAO;
import org.grp2.domain.*;
import org.grp2.Javalin.Message;
import org.grp2.enums.State;
import org.grp2.hardware.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class APIHandler {
    private ScadaDAO scadaDao;
    private IHardwareProvider hardwareProvider;
    private IHardwareSubcriber hardwareSubcriber;
    private ObjectMapper mapper;

    public APIHandler(IHardware hardware) {
        this.scadaDao = new ScadaDAO();
        this.hardwareProvider = hardware.getProvider();
        this.hardwareSubcriber = hardware.getSubcriber();
        this.mapper = new ObjectMapper();
    }

    public void listenForStateChanges() {
        this.hardwareSubcriber.subcribe(CubeNodeId.READ_STATE, value -> {
            State state = State.fromCode((int) value);
            System.out.println("CURRENT STATE: " + state);
            if (state != null) {
                switch (state) {
                    case IDLE:
                        try {
                            this.startBatch();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        break;
                    case COMPLETE:
                        this.completeBatch();
                        break;
                    case EXECUTE:
                        // collect data
                        break;
                }
            }
        }, 1000);
    }

    public void startNewProduction(Context context) {
        Message message = new Message(200, "Success");
        List<ProductionInformation> productInfos = new ArrayList<>();
        try {
            Map<String, List<ProductionInformation>> temp = mapper.readValue(context.body(), new TypeReference<Map<String, ArrayList<ProductionInformation>>>() {
            });
            productInfos = temp.get("orderItems");
        } catch (IOException e) {
            message.set(422, "JSON Error : " + e.getMessage());
        }

        scadaDao.addToQueueItems(productInfos);
        try {
            message = startBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        BatchViewOrder batchorder = new BatchViewOrder(0, 0, 0);
        BatchViewData batchdata = new BatchViewData(0, 0, 0);


        measurements.setTemperature(hardwareProvider.getTemperature());
        measurements.setHumidity(hardwareProvider.getHumidity());
        measurements.setVibration(hardwareProvider.getVibration());

        batchorder.setBatchId((int) hardwareProvider.getBatchId());
        batchorder.setAmountToProduce((int) hardwareProvider.getAmountToProduce());

        Batch batch = scadaDao.getBatch(batchorder.getBatchId());
        if (batch != null) {
            LocalDateTime started = batch.getStarted();
            LocalDateTime now = LocalDateTime.now();
            long minutes = started.until(now, ChronoUnit.MINUTES);
            batchorder.setProductsPerMinute((int) ((batchorder.getAmountToProduce()) / (minutes == 0 ? 1 : minutes)));
        }

        batchdata.setProduced(hardwareProvider.getCurrentBeersProduced());
        batchdata.setAcceptable(hardwareProvider.getAcceptedBeersProduced());
        batchdata.setDefect(hardwareProvider.getDefectiveBeersProduced());


        map.put("Measurements", measurements);
        map.put("BatchOrder", batchorder);
        map.put("BatchData", batchdata);
        map.put("State", State.fromCode(hardwareProvider.getState()));
        map.put("AmountInQueue", scadaDao.getQueueItems().size());

        context.json(map);
    }

    public void viewLog(Context context) {
        int batchId = Integer.parseInt(context.pathParam("batch-id"));

        Map<String, List> map = new HashMap<>();
        map.put("MeasurementLogs", scadaDao.getMeasurementLogs(batchId));
        map.put("StateTimeLogs", scadaDao.getStateTimeLogs(batchId));

        context.json(map);
    }


    private Message startBatch() throws InterruptedException {
        Message message = new Message(200, "Order started");
        ProductionInformation startedBatch = scadaDao.startNextBatch();

        if (startedBatch != null) {
            Recipe recipe = scadaDao.getRecipe(startedBatch.getRecipeName());

            hardwareProvider.setBatchId(startedBatch.getBatchId());
            hardwareProvider.setProduct(recipe.getId());
            hardwareProvider.setAmountToProduce(startedBatch.getQuantity());
            hardwareProvider.setMachSpeed(startedBatch.getMachineSpeed());
            if (!startedBatch.validateMachSpeed(recipe.getMinSpeed(), recipe.getMaxSpeed())) {
                message.setStatus(422);
                message.setMessage("Invalid Speed");
            }
            hardwareProvider.stop();
            TimeUnit.SECONDS.sleep(1);
            hardwareProvider.reset();
            TimeUnit.SECONDS.sleep(1);
            hardwareProvider.start();
        } else {
            message.set(200, "Order placed in queue");
        }

        return message;
    }

    private void completeBatch() {
        scadaDao.updateCurrentBatchFinished();
        hardwareProvider.stop();
        hardwareProvider.reset();
    }
}
