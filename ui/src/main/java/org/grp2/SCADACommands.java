package org.grp2;

public enum SCADACommands implements ICommand{

    START_NEW_BATCH("start:start-new-production:0:post"),
    MANAGE_PRODUCTION("manage:manage-production:1:post"),
    VIEW_SCREEN("vscreen:view-screen:0:get"),
    VIEW_LOG("vlog:view-log:1:get");

    private String name;

    SCADACommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name.split(":")[0];
    }

    public String getURL() {
        return name.split(":")[1];
    }

    public int getNumArgs() {
        return Integer.parseInt(name.split(":")[2]);
    }

    public String getUnirestCommand ()
    {
        return name.split(":")[3];
    }
}
