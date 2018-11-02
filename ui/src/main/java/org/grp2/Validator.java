package org.grp2;

public class Validator {

    private SubSystem currentSystem;

    public Validator() {

    }

    public void setCurrentSystem(SubSystem currentSystem) {
        this.currentSystem = currentSystem;
    }

    public boolean isCommand (String command)
    {
        switch (currentSystem)
        {
            case SCADA:
                for (SCADACommands value : SCADACommands.values())
                    if (command.equals(value.getName()))
                        return true;
                break;
            default:

                break;
        }
        return false;
    }
}
