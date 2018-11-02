package org.grp2;

public enum SCADACommands {
    START_NEW_BATCH("start"), MANAGE_PRODUCTION("manage"), VIEW_SCREEN("vscreen"), VIEW_LOG("vlog");

    private String name;

    SCADACommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SCADACommands fromCommand(String command)
    {
        for (SCADACommands value : SCADACommands.values())
            if (command.equals(value.getName()))
                return value;

        return null;
    }
}
