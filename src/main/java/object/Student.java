package object;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.concurrent.Executors;

import util.AcuityHttpClient;
import util.Config;

public class Student extends Person {

	/*
	 * These IDs identifies the priority of the student on the checkedInList in
	 * windows.Checkin.
	 */
	public static final int OVER_TIME = 200;
	public static final int WARNING = 300;
	public static final int SUBJECT_CHANGE = 400;
	public static final int NO_STATUS = 500;

	/*
	 * Organizes priorities within main groups but not used for sorting in
	 * windows.Checkin. Negative number puts it to higher priority
	 */
	private static final int WITH_NOTES = -50;

	private LocalDateTime timeIn, timeOut;

	private String birthday, tag, notes;
	private String[] contactPhones;
	private int subjectID, indicatorID;
	private boolean signedIn, isOnTime;

	private DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern(Config.TIME_FORMAT);
	private DateTimeFormatter birthdayFormat = DateTimeFormatter.ofPattern("MM-dd");
	private HashMap<Integer, Contact> contacts;

	private LocalDateTime notesExpiryDate;

	/** This will be used for creating new Student from StudentProcessor */
	public Student() {
	}

	/** Used in StudentDAO */
	public Student(int id, String tag, String fName, String lName, String birthday, int subjectID,
			String notes, LocalDateTime notesExpiryDate, HashMap<Integer, Contact> contacts, int indicatorID) {
		super(id, fName, lName);
		setValues(tag, birthday, subjectID, notes, notesExpiryDate, null, indicatorID);
		this.contacts = contacts;
	}

	/** Used in StudentDAO */
	public Student(int id, String tag, String fName, String lName, String birthday, int subjectID,
			String notes, LocalDateTime notesExpiryDate, String contactPhones, int indicatorID) {
		super(id, fName, lName);
		setValues(tag, birthday, subjectID, notes, notesExpiryDate, null, indicatorID);
		this.contactPhones = contactPhones.split(",");
	}

	/** used to get student ID to link to contact in EditStudents */
	public Student(String tag, String fName, String lName, String birthday, int subjectID,
			String notes, LocalDateTime notesExpiryDate, int indicatorID) {
		super(fName, lName);
		setValues(tag, birthday, subjectID, notes, notesExpiryDate, null, indicatorID);
	}

	public Student(int id, String fName, String lName) {
		super(id, fName, lName);
		setValues(tag, null, subjectID, null, null, null, indicatorID);
	}

	/** used for examples */
	public Student(String fName, String notes) {
		super(fName, "");
		this.notes = notes;
	}

	/** Used for checked in students table */
	public Student(Student s) {
		super(s.id, s.fName, s.lName);
		setValues(s.tag, s.birthday, s.subjectID, s.notes, s.notesExpiryDate, s.contactPhones, s.indicatorID);

		this.timeIn = LocalDateTime.now();
		this.timeOut = subjectID == Subject.ALL ? LocalDateTime.now().plusMinutes(Config.SESSION_LENGTH * 2)
				: LocalDateTime.now().plusMinutes(Config.SESSION_LENGTH);
	}

	private void setValues(String tag, String birthday, int subjectID, String notes,
			LocalDateTime notesExpiryDate, String contactPhones[], int indicatorID) {
		isOnTime = true;
		setTag(tag);
		setBirthday(birthday);
		setSubjectID(subjectID);
		setNotes(notes);
		setNotesExpiryDate(notesExpiryDate);
		setContactPhones(contactPhones);
		setIndicatorID(indicatorID);
	}

	public int getSubjectID() {
		return subjectID;
	}

	public String getSubject() {
		switch (this.subjectID) {
			case Subject.MATH:
				return "Math";
			case Subject.READING:
				return "Reading";
			case Subject.ALL:
				return "Math & Reading";
			default: // THIS SHOULD NEVER HAPPEN
				assert (false) : "Type must be one of Subject.MATH, Subject.READING, or Subject.MATH_READING";
				return null;
		}
	}

	public void setNotesExpiryDate(LocalDateTime notesExpiryDate) {
		this.notesExpiryDate = notesExpiryDate;
	}

	public LocalDateTime getNotesExpiryDate() {
		return notesExpiryDate;
	}

	public String displayTimeIn() {
		return timeFormat.format(timeIn);
	}

	public String displayTimeOut() {
		return timeFormat.format(timeOut);
	}

	public int timeRemaining() {
		return (int) Duration.between(LocalDateTime.now(), timeOut).toMinutes();
	}

	public int statusID() {
		if (this.timeRemaining() <= 0) {
			return OVER_TIME;
		} else if (this.timeRemaining() <= Config.WARNING_TIME) {
			return WARNING;
		} else if ((this.timeRemaining() <= Config.SESSION_LENGTH + Config.WARNING_TIME) &&
				this.subjectID == Subject.ALL) {
			return SUBJECT_CHANGE;
		} else {
			return NO_STATUS;
		}
	}

	public String priority() {
		int statusID = statusID();
		if (notes != null && !notes.trim().isEmpty())
			statusID += WITH_NOTES;
		return statusID + " " + displayTimeOut();
	}

	public void setSubjectID(int subjectID) {
		this.subjectID = subjectID;
	}

	public void signIn() {
		signedIn = true;
	}

	public void signOut() {
		signedIn = false;
	}

	public boolean isActive() {
		return signedIn;
	}

	public void setContactPhones(String[] contactPhones) {
		this.contactPhones = contactPhones;
	}

	public String[] getContactPhones() {
		return contactPhones;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getTag() {
		return tag != null ? tag : "";
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getNotes() {
		return notes != null ? notes : "";
	}

	public String[] birthday() {
		return birthday.split("-");
	}

	public String getPhones() {
		String phones = "";
		for (String phone : this.contactPhones)
			phones += phone + " ";
		return phones;
	}

	public Contact getContact(int relationshipID) {
		assert (relationshipID == Contact.ONE || relationshipID == Contact.TWO)
				: "Relationship ID must be one of Contact.ONE or Contact.TWO";
		return contacts.get(relationshipID);
	}

	public boolean isBirthdayToday() {
		return birthday != null && birthday.length() > 0
				? birthday.substring(0, 5).equals(birthdayFormat.format(LocalDateTime.now()))
				: false;
		// return birthday != null && birthday.length() > 0 &&
		// birthday.substring(0,5).equals(birthdayFormat.format(LocalDateTime.now()));
		// // More elegant but less legible????
	}

	public int daysUntilBirthday() {
		// String birthdayStr = birthday != null && birthday.length() > 0 ?
		// birthday.substring(0,5) : "";

		int year = LocalDateTime.now().getYear();
		int month = birthday != null && birthday.length() > 0 ? Integer.parseInt(birthday.substring(0, 2)) : 0;
		int day = birthday != null && birthday.length() > 0 ? Integer.parseInt(birthday.substring(3, 5)) : 0;
		LocalDate birthdayDate = LocalDate.of(year, month, day);
		;
		return (int) java.time.temporal.ChronoUnit.DAYS.between(birthdayDate, LocalDate.now());
	}

	public void checkAppointment() {
		Executors.newSingleThreadExecutor().execute(() -> {
			Log log;
			Appointment appointment;
			Student student = this;
			try {
				log = new Log(this);
				appointment = AcuityHttpClient.getAppointment(timeIn, fName, lName);
				if (appointment != null) {
					log.setAppointment(appointment);
				}
				student.setOnTime(appointment != null && log.checkAppointment());
			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public String toString() {
		return getName() + " - " + getSubject();
	}

	/////////////////////////////////////////////////////////////////////
	/** Used for CSV -> DB */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/** Used for CSV -> DB */
	public String getBirthday() {
		return birthday;
	}
	/////////////////////////////////////////////////////////////////////

	@Override
	public int hashCode() {
		return 10000 + id;
	}

	public int getIndicatorID() {
		return indicatorID;
	}

	public void setIndicatorID(int indicatorID) {
		this.indicatorID = indicatorID;
	}

	public boolean isOnTime() {
		return isOnTime;
	}

	public void setOnTime(boolean isOnTime) {
		this.isOnTime = isOnTime;
	}
}
