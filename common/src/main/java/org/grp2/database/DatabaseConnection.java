package org.grp2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static String url;
    private static String username;
    private static String password;

    static {
        url = "jdbc:postgresql://tek-mmmi-db0a.tek.c.sdu.dk:5432/si3_2018_group_22_db";
        username =  "si3_2018_group_22";
        password =  "snipe0[seism";
    }

    public void executeQuery(IDatabaseCallback callback) {
        try (Connection connection = getConnection()) {
            callback.execute(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
