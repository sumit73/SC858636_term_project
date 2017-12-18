package com.data.conn;

import java.sql.Connection;
import java.sql.DriverManager;

import com.se.utilities.Utilities;;

public class DataConnect {

	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String connstring = "jdbc:mysql://" + ":"
					+ Utilities.PORT_NUMBER + "/" + Utilities.DATABASE_NAME;
			Connection con = DriverManager.getConnection(connstring, Utilities.USER_NAME, Utilities.PASSWORD);
			return con;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void close(Connection con) {
		try {
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
