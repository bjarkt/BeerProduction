package org.grp2.shared;

import java.time.LocalDateTime;

public class MeasurementLog {
    private int batchId;
    private LocalDateTime measurementTime;
    private Measurements measurements;

    public MeasurementLog(int batchId, LocalDateTime measurementTime, Measurements measurements) {
        this.batchId = batchId;
        this.measurementTime = measurementTime;
        this.measurements = measurements;
    }

    public MeasurementLog(int batchId, LocalDateTime measurementTime, double temperature, double humidity, double vibration) {
        this(batchId, measurementTime, new Measurements(temperature, humidity, vibration));
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getMeasurementTime() {
        return measurementTime;
    }

    public void setMeasurementTime(LocalDateTime measurementTime) {
        this.measurementTime = measurementTime;
    }

    public Measurements getMeasurements() {
        return measurements;
    }

    public void setMeasurements(Measurements measurements) {
        this.measurements = measurements;
    }
}
