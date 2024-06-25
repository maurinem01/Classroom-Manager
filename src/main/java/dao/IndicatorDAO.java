package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import object.Indicator;
import util.ProgramException;

public class IndicatorDAO {

	private String create = "INSERT INTO indicator (color, definition) VALUES ('?', '?');";
	private String read = "SELECT indicator_id, color, definition FROM indicator;";
	private String update = "UPDATE indicator SET color=?, definition=? WHERE indicator_id=?;";
	private String delete = "DELETE FROM indicator WHERE indicator_id = ?;";

	public void create(Indicator indicator) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(create,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, indicator.getDefinition());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException("An error occured when inserting " + indicator.getDefinition() + ".");

			try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					indicator.setId(generatedKeys.getInt(1));
				} else {
					throw new ProgramException("Creating student failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<Integer, Indicator> read() {
		HashMap<Integer, Indicator> kvp = new HashMap<>();
		try (Statement stmt = new DBConnection().getConn().createStatement();
				ResultSet rs = stmt.executeQuery(read);) {
			while (rs.next()) {
				kvp.put(rs.getInt("indicator_id"),
						new Indicator(
								rs.getInt("indicator_id"),
								rs.getString("color"),
								rs.getString("definition")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return kvp;
	}

	public void update(Indicator indicator) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(update,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setString(1, indicator.getColor());
			stmt.setString(2, indicator.getDefinition());
			stmt.setInt(3, indicator.getId());
			int result = stmt.executeUpdate();

			if (result == 0)
				throw new ProgramException("An error occured when updating ID " + indicator.getId() + ".");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void delete(Indicator indicator) {
		try (PreparedStatement stmt = new DBConnection().getConn().prepareStatement(delete,
				Statement.RETURN_GENERATED_KEYS)) {
			stmt.setInt(1, indicator.getId());
			int result = stmt.executeUpdate();
			if (result == 0)
				throw new ProgramException("An error occured when deleting ID " + indicator.getId() + ".");
			else
				System.out.println("Contact deleted");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
