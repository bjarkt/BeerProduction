package org.grp2.domain;

public class MeasurementsStatistics {
    private Double highestTemp;
    private Double lowestTemp;
    private Double avgTemp;

    public MeasurementsStatistics(Double highestTemp, Double lowestTemp, Double avgTemp) {
        this.highestTemp = highestTemp;
        this.lowestTemp = lowestTemp;
        this.avgTemp = avgTemp;
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
}
