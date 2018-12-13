package org.grp2.enums;

public enum OrderStatus {

    OPEN("open"), LOCKED("locked"), DONE("done");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

}
