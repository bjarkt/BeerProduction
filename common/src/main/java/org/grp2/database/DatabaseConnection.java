package org.grp2.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private DatabaseLogin loginInformation;

    /**
     * Specify which database to use
     *
     * @param loginInformation
     */
    public DatabaseConnection(DatabaseLogin loginInformation) {
        this.loginInformation = loginInformation;
    }

    /**
     * Uses live database by default
     */
    public DatabaseConnection() {
        loginInformation = DatabaseLogin.LIVE;
    }

    public void executeQuery(IDatabaseCallback callback) {
        try (Connection connection = getConnection(this.loginInformation)) {
            callback.execute(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static Connection getConnection(DatabaseLogin login) throws SQLException {
        return DriverManager.getConnection(login.getUrl(), login.getUsername(), login.getPassword());
    }

}
