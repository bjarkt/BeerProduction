package org.grp2.shared;

import java.time.LocalDateTime;

public class Batch {
    private String beerName;
    private int orderNumber;
    private int batchId;
    private LocalDateTime started;
    private LocalDateTime finished;
    private int accepted;
    private int defect;

    public Batch(String beerName, int orderNumber, int batchId, LocalDateTime started, LocalDateTime finished, int accepted, int defect) {
        this.beerName = beerName;
        this.orderNumber = orderNumber;
        this.batchId = batchId;
        this.started = started;
        this.finished = finished;
        this.accepted = accepted;
        this.defect = defect;
    }

    public String getBeerName() {
        return beerName;
    }

    public void setBeerName(String beerName) {
        this.beerName = beerName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public LocalDateTime getStarted() {
        return started;
    }

    public void setStarted(LocalDateTime started) {
        this.started = started;
    }

    public LocalDateTime getFinished() {
        return finished;
    }

    public void setFinished(LocalDateTime finished) {
        this.finished = finished;
    }

    public int getAccepted() {
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }

    public int getDefect() {
        return defect;
    }

    public void setDefect(int defect) {
        this.defect = defect;
    }
}
