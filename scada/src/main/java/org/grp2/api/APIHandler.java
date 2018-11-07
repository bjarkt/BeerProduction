package org.grp2.api;

import io.javalin.Context;
import org.grp2.dao.ScadaDAO;
import org.grp2.domain.*;
import org.grp2.javalin.Message;
import org.grp2.enums.State;
import org.grp2.hardware.*;
import org.grp2.shared.Batch;
import org.grp2.shared.Measurements;
import org.grp2.shared.ProductionInformation;
import org.grp2.shared.Recipe;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class APIHandler {
    private ScadaDAO scadaDao;
    private IHardwareProvider hardwareProvider;
    private IHardwareSubscriber hardwareSubscriber;
    private ZoneId copenhagenZoneId;

    public APIHandler(IHardware hardware) {
        this.scadaDao = new ScadaDAO();
        this.hardwareProvider = hardware.getProvider();
        this.hardwareSubscriber = hardware.getSubscriber();
        this.copenhagenZoneId = ZoneId.of("Europe/Copenhagen");
    }

    public void listenForStateChanges() {
        AtomicReference<LocalDateTime> now = new AtomicReference<>(LocalDateTime.now(copenhagenZoneId));
        AtomicReference<State> previousState = new AtomicReference<>();
        AtomicReference<State> state = new AtomicReference<>();

        this.hardwareSubscriber.subscribe(CubeNodeId.READ_STATE, value -> {
            System.out.println("State changed to: " + State.fromCode((int) value));
            LocalDateTime stateChanged = LocalDateTime.now(copenhagenZoneId);
            long seconds = ChronoUnit.SECONDS.between(now.get(), stateChanged);

            if (previousState.get() != State.EXECUTE) { // update of EXECUTE state is handled in completeBatch();
                scadaDao.updateStateTimeLogs(previousState.get(), Math.toIntExact(seconds));
            }
            previousState.set(state.get());

            state.set(State.fromCode((int) value));
            now.set(LocalDateTime.now(copenhagenZoneId));
            if (state.get() != null) {
                handleStateChange(state.get(), previousState.get(), Math.toIntExact(seconds));
            }
        }, 500);

        collectData();
    }

    public void startNewProduction(Context context) {
        Message message = new Message(200, "Success");
        try {
            message = startBatch();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        message.send(context);
    }

    private Message startBatch() throws InterruptedException {
        Message message = new Message(200, "Batch started");
        ProductionInformation startedBatch = scadaDao.startNextBatch();

        if (startedBatch != null) {
            Recipe recipe = scadaDao.getRecipe(startedBatch.getRecipeName());

            hardwareProvider.setBatchId(startedBatch.getBatchId());
            hardwareProvider.setProduct(recipe.getId());
            hardwareProvider.setAmountToProduce(startedBatch.getQuantity());
            hardwareProvider.setMachSpeed(startedBatch.getMachineSpeed());

            hardwareProvider.stop();
            TimeUnit.SECONDS.sleep(2);
            hardwareProvider.reset();
            TimeUnit.SECONDS.sleep(2);
            hardwareProvider.start();
        } else {
            message.set(200, "No batch started. Queue is empty, or another batch is currently executing.");
        }

        return message;
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

        message.send(context);
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
            LocalDateTime now = LocalDateTime.now(copenhagenZoneId);
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


    private void completeBatch(State state, int timeElapsed) throws InterruptedException {
        System.out.println("Starting complete batch");
        scadaDao.updateStateTimeLogs(state, timeElapsed);
        scadaDao.updateCurrentBatchProduced(hardwareProvider.getAcceptedBeersProduced());
        scadaDao.updateCurrentBatchDefects(hardwareProvider.getDefectiveBeersProduced());
        Batch finishedBatch = scadaDao.updateCurrentBatchFinished();
        System.out.println("Completed batch, trying to update status and delete");
        if (finishedBatch != null) {
            scadaDao.updateOrderItemStatus(finishedBatch, "processed");
            scadaDao.deleteQueueItem(finishedBatch);
        }

        //hardwareProvider.stop();
        TimeUnit.SECONDS.sleep(2);
        hardwareProvider.reset();
    }

    private int calculateMissingBeers(){
        int quantity = (int) hardwareProvider.getAmountToProduce();
        int acceptedBeers = hardwareProvider.getAcceptedBeersProduced();
        if(quantity != acceptedBeers){
            return quantity - acceptedBeers;
        }
        return 0;
    }

    private void handleRejectedBeers(int rejects) throws InterruptedException{
            hardwareProvider.setAmountToProduce(rejects);
            hardwareProvider.stop();
            TimeUnit.SECONDS.sleep(2);
            hardwareProvider.reset();
            TimeUnit.SECONDS.sleep(2);
            hardwareProvider.start();
    }

    private void handleStateChange(State state, State previousState, int seconds) {
        switch (state) {
            case IDLE:
                try {
                    this.startBatch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
            case COMPLETE:
                try {
                    if(calculateMissingBeers() == 0) this.completeBatch(previousState, seconds);
                    else handleRejectedBeers(calculateMissingBeers());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void collectData() {
        // Collect measurements
        AtomicReference<Double> vibration = new AtomicReference<>(Double.MIN_VALUE);
        AtomicReference<Double> humidity =  new AtomicReference<>(Double.MIN_VALUE);
        AtomicReference<Double> temperature =  new AtomicReference<>(Double.MIN_VALUE);

        this.hardwareSubscriber.subscribe(CubeNodeId.READ_VIBRATION, vibrationFloat -> {
            vibration.set(((Float) vibrationFloat).doubleValue());
        }, 1000);
        this.hardwareSubscriber.subscribe(CubeNodeId.READ_HUMIDITY, humidityFloat -> {
            humidity.set(((Float) humidityFloat).doubleValue());
        }, 1000);
        this.hardwareSubscriber.subscribe(CubeNodeId.READ_TEMPERAURE, temperatureFloat -> {
            temperature.set(((Float) temperatureFloat).doubleValue());
            if (temperature.get() != Double.MIN_VALUE && humidity.get() != Double.MIN_VALUE && vibration.get() != Double.MIN_VALUE) {
                scadaDao.updateMeasurementLogs(temperature.get(), humidity.get(), vibration.get());
            }
        }, 1000);


        // Collect accepted and defected
        this.hardwareSubscriber.subscribe(CubeNodeId.READ_CURRENT_PRODUCED, produced -> {
            scadaDao.updateCurrentBatchProduced((Integer) produced);
        }, 1000);
        this.hardwareSubscriber.subscribe(CubeNodeId.READ_CURRENT_DEFECTIVE, defects -> {
            scadaDao.updateCurrentBatchDefects((Integer) defects);
        }, 1000);
    }
}
