package org.grp2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.grp2.javalin.Message;

import java.io.IOException;

public class UI {

    private Parser parser;

    public UI() {
        parser = new Parser();
        setUniRestMapper();
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

    private void changeSystem (SubSystem subSystem)
    {

    }

    private void performCommand(Command command)
    {
        Message message = new Message(200, "Success");
        boolean validCommand = true;
        String commandURL = "";

        switch (SCADACommands.fromCommand(command.getKeyword()))
        {
            case START_NEW_BATCH:
                System.out.println("Start New Batch");
                commandURL = "http://localhost:7000/api/start-new-production";
                break;
            case MANAGE_PRODUCTION:
                if (command.getArgs().length != 1)
                {
                    validCommand = false;
                    break;
                }

                System.out.println("Manage Production: " + command.getArgs()[0]);
                commandURL = "http://localhost:7000/api/manage-production/" + command.getArgs()[0];
                break;
            case VIEW_SCREEN:
                System.out.println("View Screen");
                commandURL = "http://localhost:7000/api/view-screen";
                break;
            case VIEW_LOG:
                if (command.getArgs().length != 1)
                {
                    validCommand = false;
                    break;
                }

                System.out.println("View log: " + command.getArgs()[0]);
                commandURL = "http://localhost:7000/api/view-log/" + command.getArgs()[0];
                break;
            default:
                System.out.println("Invalid command in UI");
                validCommand = false;
                break;
        }

        if (validCommand)
        {
            try {
                HttpResponse<Message> postMessage = Unirest.post(commandURL).asObject(Message.class);
            } catch (UnirestException e) {
                message.set(422, "Error from SCADA: " + e.getMessage());
            }

            System.out.println(message.getMessage());
        }
    }

    private void setUniRestMapper () {
        Unirest.setObjectMapper(new ObjectMapper() {
            private com.fasterxml.jackson.databind.ObjectMapper jacksonObjectMapper
                    = new com.fasterxml.jackson.databind.ObjectMapper();

            public <T> T readValue(String value, Class<T> valueType) {
                try {
                    return jacksonObjectMapper.readValue(value, valueType);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            public String writeValue(Object value) {
                try {
                    return jacksonObjectMapper.writeValueAsString(value);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
