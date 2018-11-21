package org.grp2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.grp2.shared.Batch;

import java.time.temporal.ChronoUnit;
import java.util.List;

public class BatchStatistics {
    private double avgAccepted;
    private double avgDefects;
    private double avgProductionSeconds;

    @JsonIgnore
    private List<Batch> batchList;

    public BatchStatistics(double avgAccepted, double avgDefects, double avgProductionSeconds, List<Batch> batchList) {
        this.avgAccepted = avgAccepted;
        this.avgDefects = avgDefects;
        this.avgProductionSeconds = avgProductionSeconds;
        this.batchList = batchList;
    }

    public BatchStatistics(List<Batch> batches) {
        calculateStatistics(batches);
        this.batchList = batches;
    }

    public double getAvgAccepted() {
        return avgAccepted;
    }

    public void setAvgAccepted(double avgAccepted) {
        this.avgAccepted = avgAccepted;
    }

    public double getAvgDefects() {
        return avgDefects;
    }

    public void setAvgDefects(double avgDefects) {
        this.avgDefects = avgDefects;
    }

    public double getAvgProductionSeconds() {
        return avgProductionSeconds;
    }

    public void setAvgProductionSeconds(double avgProductionSeconds) {
        this.avgProductionSeconds = avgProductionSeconds;
    }

    public List<Batch> getBatchList() {
        return batchList;
    }

    public void setBatchList(List<Batch> batchList) {
        this.batchList = batchList;
    }

    private void calculateStatistics(List<Batch> batches) {
        double accepted = 0;
        double defects = 0;
        double seconds = 0;
        for(Batch batch : batches) {
            accepted += batch.getAccepted();
            defects += batch.getDefect();
            seconds += ChronoUnit.SECONDS.between(batch.getStarted(), batch.getFinished());
        }
        this.avgAccepted = accepted / batches.size();
        this.avgDefects = defects / batches.size();
        this.avgProductionSeconds = seconds / batches.size();
    }
}








