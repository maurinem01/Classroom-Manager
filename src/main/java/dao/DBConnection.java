package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class DBConnection {

	private Map<String, String> env = System.getenv();
	private String user = env.get("KUMON_DB_USER");
	private String pass = env.get("KUMON_DB_PASSWORD");
	private String connectionStr = "jdbc:mysql://" + env.get("KUMON_DB");

	private Connection c;

	{
		try {
			c = DriverManager.getConnection(connectionStr, user, pass);
			System.out.println("Connection success");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public DBConnection() {
	}

	public Connection getConn() {
		return c;
	}

}