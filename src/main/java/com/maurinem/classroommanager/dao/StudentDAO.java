package com.maurinem.classroommanager.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;

import com.maurinem.classroommanager.exception.DatabaseOperationException;
import com.maurinem.classroommanager.model.Contact;
import com.maurinem.classroommanager.model.Student;

/**
 * Creates the student list used to populate the {@link window#CheckIn} table
 * 
 * @author Maurine
 *
 */
public class StudentDAO implements Importable<Student> {

	private String create = "INSERT INTO student (tag, first_name, last_name, birthday, subject_id, notes, notes_expiry, indicator_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

	private String readStudentPhones = "SELECT s.student_id, s.tag, s.first_name, s.last_name, s.birthday, s.subject_id, s.notes, s.notes_expiry, p.numbers, s.indicator_id "
			+ "FROM student s LEFT JOIN "
			+ "(SELECT "
			+ "c.student_id, GROUP_CONCAT(IF (c.notifications > 0, c.phone, NULL) SEPARATOR ',') AS numbers "
			+ "FROM contact c GROUP BY c.student_id) p "
			+ "ON s.student_id = p.student_id;";

	private String readStudentContact = "SELECT "
			+ "c1_cid, c1_first, c1_last, c1_phone, c1_email, c1_notifications, c1_sid, c1_rid, "
			+ "c2_cid, c2_first, c2_last, c2_phone, c2_email, c2_notifications, c2_sid, c2_rid, "
			+ "s.student_id, s.tag, s.first_name, s.last_name, s.birthday, s.subject_id, s.notes, s.notes_expiry, s.indicator_id "
			+ "FROM student s LEFT JOIN "
			+ "(SELECT "
			+ "contact_id AS c1_cid, first_name AS c1_first, last_name AS c1_last, "
			+ "phone AS c1_phone, email AS c1_email, notifications AS c1_notifications, "
			+ "student_id AS c1_sid, relationship_id AS c1_rid FROM contact "
			+ "WHERE relationship_id = 301) c1 ON s.student_id = c1.c1_sid LEFT JOIN "
			+ "(SELECT "
			+ "contact_id AS c2_cid, first_name AS c2_first, last_name AS c2_last, "
			+ "phone AS c2_phone, email AS c2_email, notifications AS c2_notifications, "
			+ "student_id AS c2_sid, relationship_id AS c2_rid FROM contact "
			+ "WHERE relationship_id = 302) c2 ON s.student_id = c2.c2_sid;";

	private String update = "UPDATE student SET tag = ?, first_name = ?, last_name = ?, birthday = ?, subject_id = ?, notes = ?, notes_expiry = ?, indicator_id = ? WHERE student_id = ?;";

	private String delete = "DELETE FROM student WHERE student_id = ?;";

	private String save = "INSERT INTO student (student_ID, first_name, last_name, birthday, subject_id, indicator_id) VALUES (?, ?, ?, ?, ?, ?)";

	public StudentDAO() {
	}

	/**
	 * Create a new Student from fields in {@link window#EditStudent}
	 * 
	 * @param student
	 */
	public void create(Student student) {
		try (PreparedStatement stmt = DBConnection.getConn().prepareStatement(create,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, student.getTag() != null && !student.getTag().trim().isEmpty() ? student.getTag() : "");
			stmt.setString(2, student.getFName());
			stmt.setString(3, student.getLName());
			stmt.setString(4, student.getBirthday());
			stmt.setInt(5, student.getSubjectID());
			stmt.setString(6,
					(student.getNotes() != null && !student.getNotes().trim().isEmpty()) ? student.getNotes() : "");
			stmt.setTimestamp(7, Timestamp.valueOf(student.getNotesExpiryDate()));
			stmt.setInt(8, student.getIndicatorID());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new DatabaseOperationException("An error occured when inserting " + student.getName() + ".");

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					student.setID(generatedKeys.getInt(1));
				} else {
					throw new DatabaseOperationException("Creating student failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Student> readStudentContactMap() {
		HashMap<Integer, Student> students = new HashMap<>();
		try (Statement stmt = DBConnection.getConn().createStatement();
				ResultSet rs = stmt.executeQuery(readStudentContact);) {
			while (rs.next()) {
				HashMap<Integer, Contact> contacts = new HashMap<>();
				setContact(rs, contacts, "c1");
				setContact(rs, contacts, "c2");

				students.put(rs.getInt("student_id"),
						new Student(rs.getInt("student_id"),
								rs.getString("tag"),
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("birthday"),
								rs.getInt("subject_id"),
								rs.getString("notes"),
								rs.getTimestamp("notes_expiry").toLocalDateTime(),
								contacts,
								rs.getInt("indicator_id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return students;
	}

	private void setContact(ResultSet rs, HashMap<Integer, Contact> contacts, String id) {
		try {
			if (rs.getObject(id + "_rid") != null) { // double check contact_id is not null
				contacts.put(rs.getInt(id + "_rid"),
						new Contact(rs.getInt(id + "_cid"),
								rs.getString(id + "_first"),
								rs.getString(id + "_last"),
								rs.getString(id + "_phone"),
								rs.getString(id + "_email"),
								rs.getBoolean(id + "_notifications"),
								rs.getInt(id + "_sid"),
								rs.getInt(id + "_rid")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Generates map of all students keyed by studentID */
	public HashMap<Integer, Student> readStudentIDMap() {
		HashMap<Integer, Student> students = new HashMap<>();
		String phones = "";
		try (Statement stmt = DBConnection.getConn().createStatement();
				ResultSet rs = stmt.executeQuery(readStudentPhones);) {
			while (rs.next()) {
				if (rs.getString("numbers") != null && rs.getString("numbers").length() > 9) {
					phones = rs.getString("numbers");
				}
				students.put(rs.getInt("student_id"),
						new Student(rs.getInt("student_id"),
								rs.getString("tag"),
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("birthday"),
								rs.getInt("subject_id"),
								rs.getString("notes"),
								rs.getTimestamp("notes_expiry").toLocalDateTime(),
								phones,
								rs.getInt("indicator_id")));
				phones = ""; // RESET PHONE # FOR NEXT ENTRY
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return students;
	}

	public void update(Student student) {
		try (PreparedStatement stmt = DBConnection.getConn().prepareStatement(update,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, student.getTag());
			stmt.setString(2, student.getFName());
			stmt.setString(3, student.getLName());
			stmt.setString(4, student.getBirthday());
			stmt.setInt(5, student.getSubjectID());
			stmt.setString(6, student.getNotes());
			stmt.setTimestamp(7, Timestamp.valueOf(student.getNotesExpiryDate()));
			stmt.setInt(8, student.getIndicatorID());
			stmt.setInt(9, student.getID());
			int result = stmt.executeUpdate();

			if (result == 1)
				System.out.println("One row updated.");
			else
				System.out.println("An error occured.");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(int studentID) {
		try (PreparedStatement stmt = DBConnection.getConn().prepareStatement(delete,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, studentID);
			int result = stmt.executeUpdate();
			if (result == 0)
				throw new DatabaseOperationException("No records deleted for student# " + studentID);
		} catch (DatabaseOperationException | SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int save(Student student) {
		int rows = 0;
		try (PreparedStatement stmt = DBConnection.getConn().prepareStatement(save);) {
			stmt.setInt(1, student.getID());
			// stmt.setString(2, t.getStudentNumber());
			stmt.setString(2, student.getFName());
			stmt.setString(3, student.getLName());
			stmt.setString(4, student.getBirthday());
			stmt.setInt(5, student.getSubjectID());
			stmt.setInt(6, student.getIndicatorID());
			rows = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("Added student " + student.getID());
		return rows;
	}

}
