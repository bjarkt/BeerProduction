package org.grp2.javalin;

public abstract class AbstractAPI {

    protected final int PORT;

    public AbstractAPI(int port) {
        this.PORT = port;
    }

    public abstract void start();
}
