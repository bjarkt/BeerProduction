package org.grp2.domain;

public class Measurements {
    private double temperature;
    private double humidity;
    private double vibration;

    public Measurements(double temperature, double humidity, double vibration) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.vibration = vibration;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public double getVibration() {
        return vibration;
    }

    public void setVibration(double vibration) {
        this.vibration = vibration;
    }
}
