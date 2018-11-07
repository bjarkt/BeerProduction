package org.grp2;

public enum MESCommands {

    VIEW_ORDERS("vorders"), VIEW_ORDER_ITEMS("vitems"), VIEW_ALL_BATCHES("vbatches"), GET_PLANT_STATISTICS("statistics"), GET_REPORT("getreport"), CREATE_BATCHES("createbatches");

    private String name;

    MESCommands(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MESCommands fromCommand(String command)
    {
        for (MESCommands value : MESCommands.values())
            if (command.equals(value.getName()))
                return value;

        return null;
    }
}
