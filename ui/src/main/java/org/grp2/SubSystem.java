package org.grp2;

public enum SubSystem {
    SCADA("scada"), MES("mes"), ERP("erp");

    private String name;

    SubSystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
