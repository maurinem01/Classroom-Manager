package dao;

import java.awt.Color;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import util.ProgramException;

public class ConfigDAO {
	
	private static String read = "SELECT property, value FROM config";
	private static String update = "UPDATE config SET value = ? WHERE property = ?";
	
	/** Time format stored in SQL database to be used in entire system
	 * <ul><li>{@link #H12} displays 12-hour time format</li>
	 * <li>{@link #H24} displays 24-hour time format</li></ul> */
	private String timeFormat, sessionLength, warningTime, sendMessages, centre, phone, 
		textIn, textOut,overTimeFG, overTimeBG, warningTimeFG, warningTimeBG, 
		subjectChangeFG, subjectChangeBG, noStatusFG, noStatusBG, notesFG, notesBG, linkAcuity;
	private Map<String, String> kvp;
	
	/** */
	public ConfigDAO() {
		kvp = new HashMap<>();
		read();
		timeFormat = kvp.get("time_format");
		sessionLength = kvp.get("session_length");
		warningTime = kvp.get("warning_time");
		sendMessages = kvp.get("send_messages");
		centre = kvp.get("centre_name");
		phone = kvp.get("centre_phone");
		textIn = kvp.get("text_in");
		textOut = kvp.get("text_out");
		overTimeFG = kvp.get("color_over_time_fg");
		overTimeBG = kvp.get("color_over_time_bg");
		warningTimeFG = kvp.get("color_warning_time_fg");
		warningTimeBG = kvp.get("color_warning_time_bg");
		subjectChangeFG = kvp.get("color_subject_change_fg");
		subjectChangeBG = kvp.get("color_subject_change_bg");
		noStatusFG = kvp.get("color_no_status_fg");
		noStatusBG = kvp.get("color_no_status_bg");
		notesFG = kvp.get("color_notes_fg");
		notesBG = kvp.get("color_notes_bg");
		linkAcuity = kvp.get("link_acuity");
	}
	
	public ConfigDAO(HashMap<String, String> kvp) {
		this.kvp = kvp;
	}
	
	@Override
	public String toString() {
		return "\nTime format: " + this.timeFormat
				+ "\nSession length: " + this.sessionLength 
				+ "\nWarning time: " + this.warningTime 
				+ "\nSend messages: " + this.sendMessages
				+ "\nCentre name: " + this.centre
				+ "\nPhone number: " + this.phone;
	}

	public void read() {
		try (Statement stmt = DBConnection.getConn().createStatement();
			ResultSet rs = stmt.executeQuery(read);) {
			while (rs.next()) {
				kvp.put(rs.getString("property"), rs.getString("value"));
			}
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private int result;
	
	public void update() {
		try (PreparedStatement stmt = DBConnection.getConn().prepareStatement(update, Statement.RETURN_GENERATED_KEYS)) {
			for (String key : kvp.keySet()) {
				stmt.setString(1, kvp.get(key));
				stmt.setString(2, key);
				result = stmt.executeUpdate();
			}
			
            System.out.println(result + " properties updated.");
            if (result == 0)
            	throw new ProgramException("An error occured when updating configurations.");
		} catch (SQLException e) {
			System.err.println(e);
		} catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	public String getTimeFormat() { return timeFormat; }
	public int getSessionLength() { return Integer.parseInt(sessionLength); }
	public int getWarningTime() { return Integer.parseInt(warningTime); }
	public String getCentre() { return centre; }
	public String getPhone() { return phone; }
	public boolean getSendMessages() { return Boolean.parseBoolean(sendMessages); }
	public String getTextIn() { return textIn; }
	public String getTextOut() { return textOut; }
	public Color getOverTimeFG() { return Color.decode(overTimeFG); }
	public Color getOverTimeBG() { return Color.decode(overTimeBG); }
	public Color getWarningTimeFG() { return Color.decode(warningTimeFG); }
	public Color getWarningTimeBG() { return Color.decode(warningTimeBG); }
	public Color getSubjectChangeFG() { return Color.decode(subjectChangeFG); }
	public Color getSubjectChangeBG() { return Color.decode(subjectChangeBG); }
	public Color getNoStatusFG() { return Color.decode(noStatusFG); }
	public Color getNoStatusBG() { return Color.decode(noStatusBG); }
	public Color getNotesBG() { return Color.decode(notesBG); }
	public Color getNotesFG() { return Color.decode(notesFG); }
	public boolean getLinkAcuity() { return Boolean.parseBoolean(linkAcuity); }

}
