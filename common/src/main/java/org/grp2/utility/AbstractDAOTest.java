package org.grp2.utility;

import org.grp2.database.DatabaseConnection;
import org.grp2.database.DatabaseLogin;

import java.sql.PreparedStatement;
import java.util.Arrays;
import java.util.List;

public abstract class AbstractDAOTest {

    protected void cleanUpTables(DatabaseConnection connection) {
        List<String> tableNames = Arrays.asList("batches", "measurement_logs", "order_items", "orders",
                "queue_items", "state_time_logs");

        connection.executeQuery(conn -> {
            conn.setAutoCommit(false);
            for (String tableName : tableNames) {
                String deleteSQL = String.format("DELETE FROM %s", tableName);
                String truncateSQL = String.format("TRUNCATE TABLE %s RESTART IDENTITY CASCADE", tableName);
                PreparedStatement deletePS = conn.prepareStatement(deleteSQL);
                PreparedStatement truncatePS = conn.prepareStatement(truncateSQL);

                deletePS.executeUpdate();
                truncatePS.executeUpdate();
            }
            conn.commit();
        });
    }
}
