package org.grp2.hardware;

public enum CubeNodeId {
    READ_BATCH_ID("::Program:Cube.Status.Parameter[0].Value"),
    WRITE_BATCH_ID("::Program:Cube.Command.Parameter[0].Value"),

    READ_PRODUCT("::Program:Cube.Admin.Parameter[0].Value"),
    WRITE_PRODUCT("::Program:Cube.Command.Parameter[1].Value"),

    READ_AMOUNT_TO_PRODUCE("::Program:Cube.Status.Parameter[1].Value"),
    WRITE_AMOUNT_TO_PRODUCE("::Program:Cube.Command.Parameter[2].Value"),
    READ_CURRENT_PRODUCED("::Program:Cube.Admin.ProdProcessedCount"),
    READ_CURRENT_DEFECTIVE("::Program:Cube.Admin.ProdDefectiveCount"),

    READ_MACH_SPEED("::Program:Cube.Status.MachSpeed"),
    READ_MACH_SPEED_PCT("::Program:Cube.Status.CurMachSpeed"),
    WRITE_MACH_SPEED("::Program:Cube.Command.MachSpeed"),

    READ_HUMIDITY("::Program:Cube.Status.Parameter[2].Value"),
    READ_TEMPERAURE("::Program:Cube.Status.Parameter[3].Value"),
    READ_VIBRATION("::Program:Cube.Status.Parameter[4].Value"),
    READ_STATE("::Program:Cube.Status.StateCurrent"),
    READ_STOP_REASON("::Program:Cube.Admin.StopReason"),

    WRITE_MACH_CMD("::Program:Cube.Command.CntrlCmd"),
    EXECUTE_MACH_CMD("::Program:Cube.Command.CmdChangeRequest");

    private String nodeId;

    CubeNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getNodeId() {
        return nodeId;
    }
}