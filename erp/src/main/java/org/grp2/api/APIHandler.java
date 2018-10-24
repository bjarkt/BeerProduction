package org.grp2.api;

import io.javalin.Context;
import org.grp2.Javalin.Message;
import org.grp2.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicInteger;

public class APIHandler {

    public APIHandler() {
        // this.facade = facade

    }

    public void createOrder(Context context) {
        AtomicInteger orderID = new AtomicInteger();
        DatabaseConnection connection = new DatabaseConnection();
        connection.executeQuery(conn -> {
            PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders VALUES(DEFAULT, DEFAULT, DEFAULT)");
            ps.execute();
            ResultSet rs = ps.getGeneratedKeys();
            orderID.set(rs.getInt(1));
            ps.close();
        });

        context.json(new Message(200, orderID.toString()));
    }

    public void deleteOrder(Context context){
        context.json(new Message(200, "Order deleted!"));
    }

    public void editOrder(Context context){
        context.json(new Message(200, "Order edited!"));
    }

    public void viewOrder(Context context){
        context.json(new Message(200, "Order viewed!"));
    }


}
