package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import util.Config;

public class DBConnection {

	private static String user = Config.getCredentials().get("user");
	private static String pass = Config.getCredentials().get("pass");
	private static String connectionStr = "jdbc:mysql://localhost/" + Config.getCredentials().get("name");

	private static Connection c;

	static {
		try {
			c = DriverManager.getConnection(connectionStr, user, pass);
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DBConnection() {
	}

	public static Connection getConn() {
		return c;
	}

}
