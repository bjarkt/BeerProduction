package org.grp2.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseCallback {

    void execute(Connection conn) throws SQLException;
}
