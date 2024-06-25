package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import object.Contact;
import util.ProgramException;

public class ContactDAO implements Importable<Contact> {

	private String create = "INSERT INTO contact (first_name, last_name, phone, email, notifications, student_id, relationship_id) VALUES (?, ?, ?, ?, ?, ?, ?);";
	private String update = "UPDATE contact SET first_name = ?, last_name = ?, phone = ?, email = ?, notifications = ? WHERE contact_id = ?;";
	private String delete = "DELETE FROM contact WHERE contact_id = ?;";
	private String save = "INSERT INTO contact (contact_id, first_name, last_name, phone, email, notifications, student_id, relationship_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

	public void create(Contact contact) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(create,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, contact.getFName());
			stmt.setString(2, contact.getLName());
			stmt.setString(3, contact.getPhone());
			stmt.setString(4, contact.getEmail());
			stmt.setBoolean(5, contact.getAllowNotifications());
			stmt.setInt(6, contact.getStudentID());
			stmt.setInt(7, contact.getRelationshipID());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException("An error occured when inserting " + contact.getName() + ".");
			else
				System.out.println("Created " + contact.getName());

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					contact.setID(generatedKeys.getInt(1));
				} else {
					throw new ProgramException("Creating contact failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void update(Contact contact) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(update,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, contact.getFName());
			stmt.setString(2, contact.getLName());
			stmt.setString(3, contact.getPhone());
			stmt.setString(4, contact.getEmail());
			stmt.setBoolean(5, contact.getAllowNotifications());
			stmt.setInt(6, contact.getID());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException("An error occured when updating " + contact.getName() + ".");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Contact contact) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(delete,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, contact.getID());
			int result = stmt.executeUpdate();
			if (result == 0)
				throw new ProgramException("An error occured when deleting " + contact.getName() + ".");
			else
				System.out.println("Contact deleted");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int save(Contact contact) {
		int rows = 0;
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(save);) {
			stmt.setInt(1, contact.getID());
			stmt.setString(2, contact.getFName());
			stmt.setString(3, contact.getLName());
			stmt.setString(4, contact.getPhone());
			stmt.setString(5, contact.getEmail());
			stmt.setInt(6, contact.getNotifications());
			stmt.setInt(7, contact.getStudentID());
			stmt.setInt(8, contact.getRelationshipID());
			rows = stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
		}
		System.out.println("Added contact " + contact.getID());
		return rows;
	}

}
