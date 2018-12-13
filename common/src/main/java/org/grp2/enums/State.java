package org.grp2.enums;

public enum State {
    DEACTIVATED("Deactivated", 0), CLEARING("Clearing", 1), STOPPED("Stopped", 2), STARTING("Starting", 3), IDLE("Idle", 4),
    SUSPENDED("Suspended", 5), EXECUTE("Execute", 6), STOPPING("Stopping", 7), ABORTING("Aborting", 8), ABORTED("Aborted", 9),
    HOLDING("Holding", 10), HELD("Held", 11), RESETTING("Resetting", 15), COMPLETING("Completing", 16), COMPLETE("Complete", 17),
    DEACTIVATING("Deactivating", 18), ACTIVATING("Activating", 19);

    private final String name;
    private final int value;

    State(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public static State fromCode(int code) {
        for (State status : State.values()) {
            if (status.value == code) {
                return status;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
