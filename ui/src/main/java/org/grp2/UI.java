package org.grp2;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.grp2.javalin.Message;

import java.io.IOException;
import java.util.Arrays;

public class UI {

    private Parser parser;

    public UI() {
        parser = new Parser();
        setUniRestMapper();
    }

    public void readInputs() {

        do {
            System.out.print("What do you want to do?: ");
            Command command = parser.getCommand();
            if (command != null)
                performCommand(command);
        } while (true);
    }

    private void performCommand(Command command) {
        Message message = new Message(200, "Success");
        boolean validCommand = true;
        boolean validArguments = true;
        StringBuilder sb = new StringBuilder();

        switch (command.getSubSystem()) {
            case SCADA:
                sb.append("http://localhost:7000/api/");
                break;
            case MES:
                sb.append("http://localhost:7001/api/");
                break;
            case ERP:
                sb.append("http://localhost:7002/api/");
                break;
            default:
                validCommand = false;
        }

        if (validCommand) {
            sb.append(command.getCommandURL());

            if (command.getNumArgs() == command.getArgs().length) {
                if (command.getNumArgs() > 0 && !command.getCommandURL().equals("create-batches")) {
                    for (int i = 0; i < command.getNumArgs(); i++) {
                        sb.append("/");
                        sb.append(command.getArgs()[i]);
                    }
                }
            } else {
                System.err.println("Invalid amount of arguments! [" + command.getArgs().length + "]");
                validArguments = false;
            }

            if (validArguments) {
                System.out.println(command.getKeyword() + ": " + Arrays.toString(command.getArgs()));

                try {
                    if (command.getCommandURL().equals("create-batches")) {
                        HttpResponse<Message> postMessage = Unirest.post(sb.toString()).header("accept", "application/json").body(command.getArgs()[0]).asObject(Message.class);
                    } else if (command.getUnirestCommand().equals("post")) {
                        HttpResponse<Message> postMessage = Unirest.post(sb.toString()).asObject(Message.class);
                    } else if (command.getUnirestCommand().equals("get")) {
                        HttpResponse<String> postMessage = Unirest.get(sb.toString()).asString();
                        System.out.println(postMessage.getBody());
                    }

                } catch (UnirestException e) {
                    message.set(422, "Error from SCADA: " + e.getMessage());
                    e.printStackTrace();
                }

                System.out.println(message.getMessage());
            }
        }
    }

    private void setUniRestMapper() {
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
