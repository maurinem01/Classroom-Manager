package com.maurinem.classroommanager.util;

import java.awt.Color;
import java.awt.Font;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import com.maurinem.classroommanager.dao.ConfigDAO;

/**
 * Keep all system configuration properties in one class without needing to
 * connect to database every time a property needs to be retrieved.
 * This class is meant to hold all system configurations in a static manner and
 * cannot be sub-classed.
 * 
 * @author Maurine
 *
 */
public final class Config {

	private static Map<String, String> credentials;

	/** An instance of Config cannot be instantiated. */
	private Config() {
	}

	static {
		credentials = new HashMap<>();
		Properties prop = new Properties();
		try (FileInputStream file = new FileInputStream("credentials.properties")) {
			prop.load(file);
			for (Map.Entry<Object, Object> entry : prop.entrySet())
				credentials.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
		} catch (IOException ex) {
			System.err.println(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Credentials */
	public final static Map<String, String> getCredentials() {
		return credentials;
	}

	/** If the email field is empty, logic for Gmail API will be disabled */
	public final static boolean SEND_EMAIL = credentials.get("alert_email") != null
			&& !credentials.get("alert_email").isBlank();

	/** 12 hour time format */
	public final static String H12 = "hh:mm";
	/** 24 hour time format */
	public final static String H24 = "HH:mm";

	private static ConfigDAO config = new ConfigDAO();

	/**
	 * System's time format. (See {@link dao.ConfigDAO#timeFormat} for more
	 * information)
	 */
	public final static String TIME_FORMAT = config.getTimeFormat();

	/**
	 * System setting for one subject session length. (See
	 * {@link dao.ConfigDAO#sessionLength} for more information)
	 */
	public final static int SESSION_LENGTH = config.getSessionLength();

	/**
	 * System setting for warning time for subject change or dismissal. (See
	 * {@link dao.ConfigDAO#warningTime} for more information)
	 */
	public final static int WARNING_TIME = config.getWarningTime();

	/** Default setting for whether text notifications are sent */
	public final static boolean SEND_MESSAGES = config.getSendMessages();

	public final static boolean LINK_ACUITY = config.getLinkAcuity();

	/** Centre's name to be used in text notifications */
	public final static String CENTRE_NAME = config.getCentre();

	/** Centre's phone number to call to be used in text notifications */
	public final static String CENTRE_PHONE = config.getPhone();

	/** Template for checkin Twilio SMS text */
	public final static String TEXT_IN = config.getTextIn();

	/** Template for checkout Twilio SMS text */
	public final static String TEXT_OUT = config.getTextOut();

	/** Over time text colour */
	public final static Color OVER_TIME_FG = config.getOverTimeFG();

	/** Over time background colour */
	public final static Color OVER_TIME_BG = config.getOverTimeBG();

	/** Warning time text colour */
	public final static Color WARNING_TIME_FG = config.getWarningTimeFG();

	/** Warning time background colour */
	public final static Color WARNING_TIME_BG = config.getWarningTimeBG();

	/** Subject change text colour */
	public final static Color SUBJECT_CHANGE_FG = config.getSubjectChangeFG();

	/** Subject change background colour */
	public final static Color SUBJECT_CHANGE_BG = config.getSubjectChangeBG();

	/** No status text colour */
	public final static Color NO_STATUS_FG = config.getNoStatusFG();

	/** No status background colour */
	public final static Color NO_STATUS_BG = config.getNoStatusBG();

	/** Notes text colour */
	public final static Color NOTES_FG = config.getNotesFG();

	/** Notes background colour */
	public final static Color NOTES_BG = config.getNotesBG();

	public final static Font FONT = new Font("Sans Serif", Font.PLAIN, 14);

	/** System's setting for JPanel borders */
	public final static Border BORDER = BorderFactory.createEtchedBorder();

}
