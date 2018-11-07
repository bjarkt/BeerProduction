package org.grp2;

public class UI {

    private Parser parser;

    public UI() {
        parser = new Parser();
    }

    public void readInputs()
    {

        do {
            System.out.print("What do you want to do?: ");
            Command command = parser.getCommand();
            if (command != null)
                performCommand(command);
        } while (true);
    }

    private void performCommand(Command command)
    {
        switch (SCADACommands.fromCommand(command.getKeyword()))
        {
            case START_NEW_BATCH:
                System.out.println("Start New Batch!");
                break;
            case MANAGE_PRODUCTION:
                System.out.println("Manage Production!");
                break;
            case VIEW_SCREEN:
                System.out.println("View Screen!");
                break;
            case VIEW_LOG:
                System.out.println("View Log!");
                break;
            default:

                break;
        }
    }
}
