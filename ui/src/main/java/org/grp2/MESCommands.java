package org.grp2;

public enum MESCommands implements ICommand{

    VIEW_ORDERS("vorders:view-orders:0:get"),
    VIEW_ORDER_ITEMS("vitems:view-order-items:1:get"),
    VIEW_ALL_BATCHES("vbatches:view-all-batches:0:get"),
    GET_PLANT_STATISTICS("statistics:get-plant-statistics:0:get"),
    GET_REPORT("getreport:get-report:1:get"),
    CREATE_BATCHES("createbatches:create-batches:0:post");

    private String name;

    MESCommands(String name) {
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
