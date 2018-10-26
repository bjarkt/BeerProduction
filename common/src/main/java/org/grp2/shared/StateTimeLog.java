package org.grp2.shared;


import org.grp2.enums.State;

public class StateTimeLog {
    private int batchId;
    private State phase;
    private long timeElapsed;

    public StateTimeLog(int batchId, State phase, long timeElapsed) {
        this.phase = phase;
        this.timeElapsed = timeElapsed;
        this.batchId = batchId;
    }

    public State getPhase() {
        return phase;
    }

    public void setPhase(State phase) {
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
