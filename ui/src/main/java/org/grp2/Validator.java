package org.grp2;

public class Validator {

    public Validator() {}

    /**
     * Checks if the command is valid and returns information about the command
     * @param subSystem any Subsystem
     * @param command any string represented in a Commands Enum
     * @return the value of the command enum
     */
    public ICommand getCommandInfo(SubSystem subSystem, String command)
    {
        switch (subSystem)
        {
            case SCADA:
                for (SCADACommands value : SCADACommands.values())
                    if (command.equals(value.getName()))
                        return value;
                break;
            case MES:
                for (MESCommands value : MESCommands.values())
                    if (command.equals((value.getName())))
                        return value;
                break;
            case ERP:
                for (ERPCommands value : ERPCommands.values())
                    if (command.equals((value.getName())))
                        return value;
                break;
            default:
        }
        return null;
    }
}
