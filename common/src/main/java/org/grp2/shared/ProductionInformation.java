package org.grp2.shared;

public class ProductionInformation {
    private String recipeName;
    private int orderNumber;
    private int machineSpeed;
    private int quantity;
    private Integer batchId;

    public ProductionInformation() { }

    public ProductionInformation(String recipeName, int orderNumber, int machineSpeed, int quantity) {
        this.recipeName = recipeName;
        this.orderNumber = orderNumber;
        this.machineSpeed = machineSpeed;
        this.quantity = quantity;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public int getMachineSpeed() {
        return machineSpeed;
    }

    public void setMachineSpeed(int machineSpeed) {
        this.machineSpeed = machineSpeed;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getBatchId() {
        return batchId;
    }

    public void setBatchId(int batchId) {
        this.batchId = batchId;
    }

    public boolean validateMachSpeed(int min, int max){
        return this.machineSpeed >= min && this.machineSpeed <= max;
    }
}
