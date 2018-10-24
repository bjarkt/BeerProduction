package org.grp2;

import org.grp2.database.DatabaseConnection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ErpServer {
	public static void main(String[] args) {


		DatabaseConnection connection = new DatabaseConnection();
		connection.executeQuery(conn -> {

			PreparedStatement ps = conn.prepareStatement("SELECT 1");
			ResultSet rs = ps.executeQuery();
			while(rs.next()){
				System.out.println(rs.getInt(1));
			}

		});

	}
}
