package org.grp2.domain;

import org.grp2.shared.Batch;

public class OEE {
    private double availability;
    private double performance;
    private double quality;
    private double oee;

    public OEE(Batch batch, double stopTime) {
        this.availability = getOEEAvailability(batch, stopTime);
        this.performance = getOEEPerformance(batch);
        this.quality = getOEEQuality(batch);
        this.oee = calculateOEE();
    }

    public OEE() { }

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
        return oee;
    }

    public double calculateOEE(){
        return availability * performance * quality;
    }

    /**
     * This method calculates the availability from the data that is in the database. The method is needed to
     * calculate the OEE.
     * @param batch
     * @return
     */
    private double getOEEAvailability(Batch batch, double stopTime) {
        double plannedProductionTime = (60.0/((double) batch.getMachineSpeed())) * (((double) batch.getAccepted()) + ((double)batch.getDefect()));
        double runtime = plannedProductionTime - stopTime;

        double availability = runtime / plannedProductionTime;

        return availability;
    }

    /**
     * Performance cannot be calculated in our system so it will always be 1.
     * @param batch
     * @return
     */
    private double getOEEPerformance(Batch batch) {
        return 1;
    }

    /**
     * The method calculates the quality by getting the amount of accepted and defect beers and dividing the total
     * @param batch
     * @return
     */
    private static double getOEEQuality(Batch batch) {
        double quality = ((double) batch.getAccepted()) / (((double) batch.getAccepted()) + ((double) batch.getDefect()));

        return quality;
    }

}
