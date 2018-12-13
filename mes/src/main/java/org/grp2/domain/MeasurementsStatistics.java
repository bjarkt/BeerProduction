package org.grp2.domain;

import org.grp2.shared.MeasurementLog;

import java.util.List;

public class MeasurementsStatistics {
    private Double highestTemp;
    private Double lowestTemp;
    private Double avgTemp;

    public MeasurementsStatistics(Double highestTemp, Double lowestTemp, Double avgTemp) {
        this.highestTemp = highestTemp;
        this.lowestTemp = lowestTemp;
        this.avgTemp = avgTemp;
    }

    public MeasurementsStatistics() {
    }

    public MeasurementsStatistics(List<MeasurementLog> measurements) {
        calculateStatistics(measurements);
    }

    public Double getHighestTemp() {
        return highestTemp;
    }

    public void setHighestTemp(Double highestTemp) {
        this.highestTemp = highestTemp;
    }

    public Double getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(Double lowestTemp) {
        this.lowestTemp = lowestTemp;
    }

    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }

    private void calculateStatistics(List<MeasurementLog> measurements) {
        Double highestTemp = null;
        Double lowestTemp = null;
        for (MeasurementLog measurement : measurements) {
            if (highestTemp == null && lowestTemp == null) {
                highestTemp = measurement.getMeasurements().getTemperature();
                lowestTemp = measurement.getMeasurements().getTemperature();
            }
            if (measurement.getMeasurements().getTemperature() > highestTemp) {
                highestTemp = measurement.getMeasurements().getTemperature();
            }
            if (measurement.getMeasurements().getTemperature() < lowestTemp) {
                lowestTemp = measurement.getMeasurements().getTemperature();
            }
        }

        Double tempSum = 0.0;
        for (MeasurementLog measurement : measurements) {
            tempSum += measurement.getMeasurements().getTemperature();
        }

        Double avgTemp = tempSum / measurements.size();

        this.avgTemp = avgTemp;
        this.highestTemp = highestTemp;
        this.lowestTemp = lowestTemp;
    }
}
