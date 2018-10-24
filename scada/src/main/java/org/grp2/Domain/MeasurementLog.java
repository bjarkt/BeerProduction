package org.grp2.Domain;

import java.time.LocalDateTime;

public class MeasurementLog {
    private int batchId;
    private LocalDateTime measuremenTime;
    private Measurements measurements;

    public MeasurementLog(int batchId, LocalDateTime measuremenTime, Measurements measurements) {
        this.batchId = batchId;
        this.measuremenTime = measuremenTime;
        this.measurements = measurements;
    }

    public MeasurementLog(int batchId, LocalDateTime measuremenTime, double temperature, double humidity, double vibration) {
        this(batchId, measuremenTime, new Measurements(temperature, humidity, vibration));
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getMeasuremenTime() {
        return measuremenTime;
    }

    public void setMeasuremenTime(LocalDateTime measuremenTime) {
        this.measuremenTime = measuremenTime;
    }

    public Measurements getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Measurements measurements) {
        this.measurements = measurements;
    }
}
