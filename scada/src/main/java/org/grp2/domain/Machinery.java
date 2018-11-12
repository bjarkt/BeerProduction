package org.grp2.domain;

import org.grp2.dao.ScadaDAO;
import org.grp2.enums.State;
import org.grp2.hardware.CubeNodeId;
import org.grp2.hardware.IHardware;
import org.grp2.javalin.Message;
import org.grp2.shared.Batch;
import org.grp2.shared.ProductionInformation;
import org.grp2.shared.Recipe;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class Machinery {
    private ScadaDAO scadaDAO;
    private IHardware hardware;
    private ZoneId copenhagenZoneId;

    private int previousAccepted = 0;
    private int previousDefect = 0;

    public Machinery(IHardware hardware) {
        scadaDAO = new ScadaDAO();
        this.hardware = hardware;
        this.copenhagenZoneId = ZoneId.of("Europe/Copenhagen");
    }

    public void listenForStateChanges() {
        AtomicReference<LocalDateTime> now = new AtomicReference<>(LocalDateTime.now(copenhagenZoneId));
        AtomicReference<State> previousState = new AtomicReference<>();
        AtomicReference<State> state = new AtomicReference<>();

        this.hardware.getSubscriber().subscribe(CubeNodeId.READ_STATE, value -> {
            LocalDateTime stateChanged = LocalDateTime.now(copenhagenZoneId);
            long seconds = ChronoUnit.SECONDS.between(now.get(), stateChanged);
            System.out.println(String.format("CURRENT STATE: %s", (State.fromCode((Integer) value))));
            previousState.set(state.get());
            if (previousState.get() != null) {
                System.out.println(String.format("PREVIOUS STATE: %s - time spent %d\n", previousState.get(), seconds));
                scadaDAO.updateStateTimeLogs(previousState.get(), Math.toIntExact(seconds));
            }

            state.set(State.fromCode((int) value));
            now.set(LocalDateTime.now(copenhagenZoneId));
            if (state.get() != null) {
                handleStateChange(state.get());
            }
        }, 500);

        collectData();
    }

    private void handleStateChange(State state) {
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
                    if(calculateMissingBeers() == 0) this.completeBatch();
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

        this.getHardware().getSubscriber().subscribe(CubeNodeId.READ_VIBRATION, vibrationFloat -> {
            vibration.set(((Float) vibrationFloat).doubleValue());
        }, 1000);
        this.getHardware().getSubscriber().subscribe(CubeNodeId.READ_HUMIDITY, humidityFloat -> {
            humidity.set(((Float) humidityFloat).doubleValue());
        }, 1000);
        this.getHardware().getSubscriber().subscribe(CubeNodeId.READ_TEMPERAURE, temperatureFloat -> {
            temperature.set(((Float) temperatureFloat).doubleValue());
            if (temperature.get() != Double.MIN_VALUE && humidity.get() != Double.MIN_VALUE && vibration.get() != Double.MIN_VALUE) {
                getScadaDAO().updateMeasurementLogs(temperature.get(), humidity.get(), vibration.get());
            }
        }, 1000);


        // Collect accepted and defected
        this.getHardware().getSubscriber().subscribe(CubeNodeId.READ_CURRENT_PRODUCED, produced -> {
            updateDefective();
        }, 500);
        this.getHardware().getSubscriber().subscribe(CubeNodeId.READ_CURRENT_DEFECTIVE, defects -> {
            updateDefective();
        }, 500);
    }


    public Message startBatch() throws InterruptedException {
        Message message = new Message(200, "Batch started");
        ProductionInformation startedBatch = scadaDAO.startNextBatch();

        previousAccepted = 0;
        previousDefect = 0;


        if (startedBatch != null) {
            Recipe recipe = scadaDAO.getRecipe(startedBatch.getRecipeName());

            hardware.getProvider().setBatchId(startedBatch.getBatchId());
            hardware.getProvider().setProduct(recipe.getId());
            hardware.getProvider().setAmountToProduce(startedBatch.getQuantity());
            hardware.getProvider().setMachSpeed(startedBatch.getMachineSpeed());

            hardware.getProvider().stop();
            TimeUnit.SECONDS.sleep(2);
            hardware.getProvider().reset();
            TimeUnit.SECONDS.sleep(2);
            hardware.getProvider().start();
        } else {
            message.set(200, "No batch started. Queue is empty, or another batch is currently executing.");
        }

        return message;
    }


    private void completeBatch() throws InterruptedException {
        this.scadaDAO.updateCurrentBatchProduced(this.getHardware().getProvider().getAcceptedBeersProduced());
        this.scadaDAO.updateCurrentBatchDefects(this.getHardware().getProvider().getDefectiveBeersProduced());

        Batch finishedBatch = scadaDAO.updateCurrentBatchFinished();
        if (finishedBatch != null) {
            scadaDAO.updateOrderItemStatus(finishedBatch, "processed");
            scadaDAO.deleteQueueItem(finishedBatch);
        }

        //hardwareProvider.stop();
        TimeUnit.SECONDS.sleep(2);
        hardware.getProvider().reset();
    }


    private int calculateMissingBeers(){
        int quantity = (int) getHardware().getProvider().getAmountToProduce();
        int acceptedBeers = getHardware().getProvider().getAcceptedBeersProduced();
        if(quantity != acceptedBeers){
            return quantity - acceptedBeers;
        }
        return 0;
    }

    private void handleRejectedBeers(int rejects) throws InterruptedException{
        previousAccepted = 0;
        previousDefect = 0;

        this.getHardware().getProvider().setBatchId(this.scadaDAO.getCurrentBatch().getBatchId());
        this.getHardware().getProvider().setProduct(this.scadaDAO.getRecipe(this.scadaDAO.getCurrentBatch().getBeerName()).getId());
        this.getHardware().getProvider().setAmountToProduce(rejects);
        this.getHardware().getProvider().setMachSpeed(this.scadaDAO.getCurrentBatch().getMachineSpeed());

        getHardware().getProvider().stop();
        TimeUnit.SECONDS.sleep(2);
        getHardware().getProvider().reset();
        TimeUnit.SECONDS.sleep(2);
        getHardware().getProvider().start();
    }


    public ScadaDAO getScadaDAO() {
        return scadaDAO;
    }

    public IHardware getHardware() {
        return hardware;
    }

    public ZoneId getCopenhagenZoneId() {
        return copenhagenZoneId;
    }

    private void updateDefective() {
        int currentDefect = this.getHardware().getProvider().getDefectiveBeersProduced();
        int difference = Math.abs(previousDefect - currentDefect);
        this.scadaDAO.updateCurrentBatchDefects(difference);

        System.out.println("DEFECT: " + previousDefect  + " - " + currentDefect + " = " + difference);
        previousDefect = currentDefect;
    }

    private void updateAccepted() {
        int currentAccepted = this.getHardware().getProvider().getAcceptedBeersProduced();
        int difference = Math.abs(previousAccepted - currentAccepted);
        this.scadaDAO.updateCurrentBatchProduced(difference);

        System.out.println("ACCEPTED: " + previousAccepted  + " - " + currentAccepted + " = " + difference);
        previousAccepted = currentAccepted;
    }
}
