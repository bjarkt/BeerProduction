package org.grp2;

import java.util.Scanner;

public class Parser {

    private Validator validator;
    private Scanner reader;

    public Parser() {
        validator = new Validator();
        reader = new Scanner(System.in);
    }

    public Command getCommand ()
    {
        String inputLine = reader.nextLine();
        String[] inputs = inputLine.split(" ");

        SubSystem subSystem = SubSystem.fromCommand(inputs[0]);

        if (subSystem == null)
        {
            System.err.println("Wrong subsystem!");
            return null;
        }

        String commandInput = inputs[1];

        ICommand commandInfo = validator.validateCommandAndGetInfo(subSystem, commandInput);

        if (commandInfo == null)
        {
            System.err.println("Invalid command!");
            return null;
        }

        String[] arguments = new String[0];
        if (inputs.length > 2)
        {
            arguments = new String[inputs.length - 2];
            for (int i = 0; i < arguments.length; i++)
                arguments[i] = inputs[i + 2];
        }

        String commandKeyword = commandInfo.getName();
        String commandURL = commandInfo.getURL();
        int numArgs = commandInfo.getNumArgs();
        String unirestCommand = commandInfo.getUnirestCommand();

        return new Command(subSystem, commandKeyword, arguments, commandURL, numArgs, unirestCommand);
    }
}
