package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import object.Log;
import object.Student;
import util.ProgramException;

public class LogDAO {

	private String checkIn = "INSERT INTO log (student_id, time_in) VALUES (?, ?)";
	private String checkOut = "UPDATE log SET time_out = ? WHERE log_id = ?";

	// private String read = "SELECT * FROM "
	// + "(SELECT s.student_id AS 'sid', s.first_name AS 'fName', s.last_name AS
	// 'lName', s.tag AS 'tag', s.subject_id AS 'subject_id', "
	// + "DATE(l.time_in) AS 'date', TIME(l.time_in) AS 'in', TIME(l.time_out) AS
	// 'out' "
	// + "FROM student s LEFT JOIN "
	// + "(SELECT student_id, time_in, time_out FROM log) l "
	// + "ON s.student_id = l.student_id "
	// + "WHERE time_in IS NOT NULL"
	// + ") t";

	String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

	private String read = "SELECT * FROM ("
			+ "SELECT s.student_id AS 'sid', "
			+ "s.first_name AS 'fName', "
			+ "s.last_name AS 'lName', "
			+ "DATE(l.time_in) AS 'date', TIME(l.time_in) AS 'in', "
			+ "TIME(l.time_out) AS 'out' FROM student s "
			+ "LEFT JOIN (SELECT student_id, time_in, time_out FROM log) l ON s.student_id = l.student_id WHERE time_in IS NOT NULL) t "
			+ "WHERE t.date >= '" + today + "' AND t.date <= '" + today + "' ORDER BY fName";

	private String getTags = "SELECT DISTINCT tag FROM student;";

	public void checkIn(Log log) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(checkIn,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, log.getStudent().getID());
			stmt.setTimestamp(2, Timestamp.valueOf(log.getSignIn()));
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException(
						"An error occured when creating checkIn log for student " + log.getStudent().getID() + ".");

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next())
					log.setID(generatedKeys.getInt(1));
				else
					throw new ProgramException("Creating contact failed, no ID obtained.");
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void checkOut(Log log) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(checkOut,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setTimestamp(1, Timestamp.valueOf(log.getSignOut()));
			stmt.setInt(2, log.getID());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException(
						"An error occured when updating checkOut log for student " + log.getStudent().getID() + ".");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<Log> read() {
		List<Log> logs = new ArrayList<>();
		try (Statement stmt = new DBConnection().getConn().createStatement();
				ResultSet rs = stmt.executeQuery(read);) {
			while (rs.next()) {
				logs.add(new Log(
						new Student(rs.getInt("sid"),
								rs.getString("fName"),
								rs.getString("lName")),
						rs.getDate("date").toLocalDate(),
						rs.getTime("in").toLocalTime(),
						rs.getTime("out").toLocalTime()));
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return logs;
	}

	public List<String> getTags() {
		List<String> tags = new ArrayList<>();
		try (Statement stmt = new DBConnection().getConn().createStatement();
				ResultSet rs = stmt.executeQuery(getTags);) {
			while (rs.next()) {
				tags.add(rs.getString("tag"));
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tags;
	}

}
