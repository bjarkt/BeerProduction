package org.grp2;

import java.util.Scanner;

public class Parser {

    private Validator validator;
    private Scanner reader;

    public Parser() {
        validator = new Validator();
        validator.setCurrentSystem(SubSystem.SCADA);
        reader = new Scanner(System.in);
    }

    public Command getCommand ()
    {
        String inputLine = reader.nextLine();
        String[] inputs = inputLine.split(" ");

        String command = inputs[0];

        if (!validator.isCommand(command))
            return null;

        String[] arguments = new String[inputs.length - 1];
        for (int i = 0; i < arguments.length - 1; i++)
            arguments[i] = inputs[i + 1];

        return new Command(command, arguments);
    }
}
