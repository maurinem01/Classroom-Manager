package util;

import java.awt.Color;
import java.awt.Font;
// import java.io.FileInputStream;
// import java.io.IOException;
// import java.util.HashMap;
// import java.util.Map;
// import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

import dao.ConfigDAO;

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

	// private static Map<String, String> credentials;

	/** An instance of Config cannot be instantiated. */
	private Config() {
	}

	// static {
	// credentials = new HashMap<>();
	// Properties prop = new Properties();
	// try (FileInputStream file = new FileInputStream("credentials.properties")) {
	// prop.load(file);
	// for (Map.Entry<Object, Object> entry : prop.entrySet())
	// credentials.put(String.valueOf(entry.getKey()),
	// String.valueOf(entry.getValue()));
	// System.out.println("Loaded credentials");
	// } catch (IOException ex) {
	// System.err.println(ex);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/** Credentials */
	// public static final Map<String, String> getCredentials() { return
	// credentials; }

	/** 12 hour time format */
	public static final String H12 = "hh:mm";
	/** 24 hour time format */
	public static final String H24 = "HH:mm";

	private static ConfigDAO config = new ConfigDAO();

	/**
	 * System's time format. (See {@link dao.ConfigDAO#timeFormat} for more
	 * information)
	 */
	public static final String TIME_FORMAT = config.getTimeFormat();

	/**
	 * System setting for one subject session length. (See
	 * {@link dao.ConfigDAO#sessionLength} for more information)
	 */
	public static final int SESSION_LENGTH = config.getSessionLength();

	/**
	 * System setting for warning time for subject change or dismissal. (See
	 * {@link dao.ConfigDAO#warningTime} for more information)
	 */
	public static final int WARNING_TIME = config.getWarningTime();

	/** Default setting for whether text notifications are sent */
	public static final boolean SEND_MESSAGES = config.getSendMessages();

	public static final boolean LINK_ACUITY = config.getLinkAcuity();

	/** Centre's name to be used in text notifications */
	public static final String CENTRE_NAME = config.getCentre();

	/** Centre's phone number to call to be used in text notifications */
	public static final String CENTRE_PHONE = config.getPhone();

	/** */
	public static final String TEXT_IN = config.getTextIn();

	/** */
	public static final String TEXT_OUT = config.getTextOut();

	/** */
	public static final Color OVER_TIME_FG = config.getOverTimeFG();

	/** */
	public static final Color OVER_TIME_BG = config.getOverTimeBG();

	/** */
	public static final Color WARNING_TIME_FG = config.getWarningTimeFG();

	/** */
	public static final Color WARNING_TIME_BG = config.getWarningTimeBG();

	/** */
	public static final Color SUBJECT_CHANGE_FG = config.getSubjectChangeFG();

	/** */
	public static final Color SUBJECT_CHANGE_BG = config.getSubjectChangeBG();

	/** */
	public static final Color NO_STATUS_FG = config.getNoStatusFG();

	/** */
	public static final Color NO_STATUS_BG = config.getNoStatusBG();

	/** */
	public static final Color NOTES_FG = config.getNotesFG();

	/** */
	public static final Color NOTES_BG = config.getNotesBG();

	public static final Font FONT = new Font("Sans Serif", Font.PLAIN, 14);

	/** System's setting for JPanel borders */
	public static final Border BORDER = BorderFactory.createEtchedBorder();

}
