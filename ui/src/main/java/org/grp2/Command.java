package org.grp2;

import java.util.Arrays;

public class Command {

    private SubSystem subSystem;
    private String keyword;
    private String[] args;

    private String commandURL;
    private int numArgs;
    private String unirestCommand;

    public Command(SubSystem subSystem, String keyword, String[] args, String commandURL, int numArgs, String unirestCommand) {
        this.subSystem = subSystem;
        this.keyword = keyword;
        this.args = args;

        this.commandURL = commandURL;
        this.numArgs = numArgs;
        this.unirestCommand = unirestCommand;
    }

    public SubSystem getSubSystem() {
        return subSystem;
    }

    public String getKeyword() {
        return keyword;
    }

    public String[] getArgs() {
        return args;
    }

    public String getCommandURL() {
        return commandURL;
    }

    public int getNumArgs() {
        return numArgs;
    }

    public String getUnirestCommand() {
        return unirestCommand;
    }

    @Override
    public String toString() {
        return "Name: " + this.keyword + "\nArguments: " + Arrays.toString(this.args) + "\nSystem: " + this.subSystem
                + "\nURL: " + this.commandURL + "\nNumber of arguments: " + this.numArgs;
    }
}
