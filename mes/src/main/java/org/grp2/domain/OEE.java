package org.grp2.domain;

public class OEE {
    private double availability;
    private double performance;
    private double quality;
    private double OEE;

    public OEE(double availability, double performance, double quality) {
        this.availability = availability;
        this.performance = performance;
        this.quality = quality;
        this.OEE = calculateOEE();
    }

    public double getAvailability() {
        return availability;
    }

    public double getPerformance() {
        return performance;
    }

    public double getQuality() {
        return quality;
    }

    public double getOEE() {
        return OEE;
    }

    public double calculateOEE(){
        return availability * performance * quality;
    }
}
