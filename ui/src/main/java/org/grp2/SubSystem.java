package org.grp2;

public enum SubSystem {
    SCADA("s"), MES("m"), ERP("e");

    private String name;

    SubSystem(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static SubSystem fromCommand(String code) {
        for (SubSystem subSystem : SubSystem.values())
            if (subSystem.name.equals(code))
                return subSystem;

        return null;
    }
}
