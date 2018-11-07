package org.grp2.domain;

public class PlantStatistics {
    private MeasurementsStatistics measurementsStatistics;
    private BatchStatistics batchStatistics;

    public PlantStatistics(MeasurementsStatistics measurementsStatistics, BatchStatistics batchStatistics) {
        this.measurementsStatistics = measurementsStatistics;
        this.batchStatistics = batchStatistics;
    }

    public MeasurementsStatistics getMeasurementsStatistics() {
        return measurementsStatistics;
    }

    public void setMeasurementsStatistics(MeasurementsStatistics measurementsStatistics) {
        this.measurementsStatistics = measurementsStatistics;
    }

    public BatchStatistics getBatchStatistics() {
        return batchStatistics;
    }

    public void setBatchStatistics(BatchStatistics batchStatistics) {
        this.batchStatistics = batchStatistics;
    }
}
