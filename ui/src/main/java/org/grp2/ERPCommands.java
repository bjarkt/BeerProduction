package org.grp2;

public enum ERPCommands implements ICommand {

    CREATE_ORDER("create:create-order:0:post"),
    ADD_ORDER_ITEM("additem:add-order-item:3:post"),
    DELETE_ORDER("deleteorder:delete-order:1:post"),
    EDIT_ORDER_ITEM("edititem:edit-order-item:2:post"),
    DELETE_ORDER_ITEM("deleteitem:delete-order-item:1:post"),
    VIEW_ORDER_ITEMS("vitem:view-order-items:1:get"),
    VIEW_ORDER_DETAILS("vdetails:view-order-details:1:get");

    private String name;

    ERPCommands(String name) {
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
