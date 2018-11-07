package org.grp2.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.grp2.shared.Batch;

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
}








