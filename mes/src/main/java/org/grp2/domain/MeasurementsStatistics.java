package org.grp2.domain;

public class MeasurementsStatistics {
    private double highestTemp;
    private double lowestTemp;
    private double avgTemp;

    public MeasurementsStatistics(double highestTemp, double lowestTemp, double avgTemp) {
        this.highestTemp = highestTemp;
        this.lowestTemp = lowestTemp;
        this.avgTemp = avgTemp;
    }

    public double getHighestTemp() {
        return highestTemp;
    }

    public void setHighestTemp(double highestTemp) {
        this.highestTemp = highestTemp;
    }

    public double getLowestTemp() {
        return lowestTemp;
    }

    public void setLowestTemp(double lowestTemp) {
        this.lowestTemp = lowestTemp;
    }

    public double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(double avgTemp) {
        this.avgTemp = avgTemp;
    }
}
