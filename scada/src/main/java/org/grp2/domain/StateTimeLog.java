package org.grp2.domain;


public class StateTimeLog {
    private int batchId;
    private String phase;
    private long timeElapsed;

    public StateTimeLog(int batchId, String phase, long timeElapsed) {
        this.phase = phase;
        this.timeElapsed = timeElapsed;
        this.batchId = batchId;
    }

    public String getPhase() {
        return phase;
    }

    public void setPhase(String phase) {
        this.phase = phase;
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }
}
