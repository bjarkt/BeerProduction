package org.grp2.enums;

public enum OrderItemStatus {
    NONPROCESSED("nonProcessed"), PROCESSING("processing"), PROCESSED("processed"), ABORTED("aborted");

    private final String status;

    OrderItemStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
