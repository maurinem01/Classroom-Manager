package com.maurinem.classroommanager.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.maurinem.classroommanager.util.Config;

public class Log {

	public final static int PRIOR_YELLOW = -15;
	public final static int PRIOR_GREEN = -10;
	public final static int AFTER_GREEN = 10;
	public final static int AFTER_YELLOW = 15;
	public final static int NO_APPOINTMENT = 99;

	private int id;
	private LocalDateTime signIn, signOut, appointmentTime;
	private Appointment appointment;

	private boolean onTime;

	private Student student;

	public Log(Student student) {
		this.student = student;
		this.signIn = LocalDateTime.now();
	}

	public Log(Student student, LocalDate date, LocalTime appointmentTime, LocalTime in, LocalTime out,
			boolean onTime) {
		this.student = student;
		this.appointmentTime = appointmentTime != null ? LocalDateTime.of(date, appointmentTime) : null;
		this.signIn = LocalDateTime.of(date, in);
		this.signOut = LocalDateTime.of(date, out);
		this.onTime = onTime;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setSignIn(LocalDateTime signIn) {
		this.signIn = signIn;
	}

	public void signOut() {
		this.signOut = LocalDateTime.now();
	}

	public int getID() {
		return id;
	}

	public Student getStudent() {
		return student;
	}

	public LocalDateTime getSignIn() {
		return signIn;
	}

	public LocalDateTime getSignOut() {
		return signOut;
	}

	public LocalDateTime getAppointmentTime() {
		return appointmentTime;
	}

	public static String formatTime(LocalDateTime ldt) {
		return ldt.format(DateTimeFormatter.ofPattern("h:mma")).replace(".", "");
	}

	/**
	 * Parse String of xx:xx{am/pm} format in to String[] with [0] = hour, [1] =
	 * minute, and [2] = am/pm.
	 * 
	 * @param str MUST be in xx:xx{am/pm} format
	 * @return
	 */
	public static Object[] getTimeComponents(String str) {
		String ampm = str.length() > 2 ? str.substring(str.length() - 2).toLowerCase() : str;
		str = str.substring(0, str.length() - 2).toLowerCase();
		String[] time = str.split(":");
		Integer[] timeInt = new Integer[2];
		for (int index = 0; index < 2; index++)
			timeInt[index] = Integer.parseInt(time[index]);
		if (ampm.equals("am") && timeInt[0] == 12)
			timeInt[0] = 0;
		if (ampm.equals("pm") && timeInt[0] != 12)
			timeInt[0] += 12;
		return new Object[] { timeInt[0], timeInt[1], ampm };
	}

	public Appointment getAppointment() {
		return Config.LINK_ACUITY ? appointment : null;
	}

	/**
	 * Assigns the Appointment object to this Log, as well as sets the LocalDateTime
	 * appointmentTime used for program calculations
	 * 
	 * @param appointment
	 */
	public void setAppointment(Appointment appointment) {

		this.appointment = appointment != null ? appointment : null;

		if (appointment != null) { // Get time components from Appointment then convert to LocalDateTime
			Object[] appointmentTimeComponents = Log.getTimeComponents(appointment.getTime());
			LocalTime apptTime = LocalTime.of(
					(int) appointmentTimeComponents[0],
					(int) appointmentTimeComponents[1]);
			this.appointmentTime = LocalDateTime.of(LocalDate.now(), apptTime); // default null
		}
	}

	public boolean isOnTime() {
		compareAppointment();
		return this.onTime;
	}

	/**
	 * @return true if same first name, last name, appointment and log time are
	 *         within 15 minutes, same am/pm
	 */
	public int compareAppointment() {
		LocalTime apptTime, logTime;
		boolean sameFirstName, sameLastName, sameAmPm, withinFifteenMins;
		int status;
		long timeBetween;

		logTime = signIn.toLocalTime();
		sameFirstName = false;
		sameLastName = false;
		sameAmPm = false;
		timeBetween = 0;
		withinFifteenMins = false;
		status = NO_APPOINTMENT;

		if (appointment != null) {
			apptTime = appointmentTime.toLocalTime();
			sameFirstName = appointment.getFirstName().toLowerCase().equals(student.getFName().toLowerCase());
			sameLastName = appointment.getLastName().toLowerCase().equals(student.getLName().toLowerCase());
			sameAmPm = getTimeComponents(formatTime(signIn))[2]
					.equals(getTimeComponents(formatTime(appointmentTime))[2]);
			timeBetween = (Duration.between(apptTime, logTime).getSeconds()) / 60;
			withinFifteenMins = Math.abs(timeBetween) <= 15;
			sameAmPm = getTimeComponents(formatTime(signIn))[2]
					.equals(getTimeComponents(formatTime(appointmentTime))[2]);
		}

		this.onTime = sameFirstName && sameLastName && withinFifteenMins && sameAmPm;

		if (sameFirstName && sameLastName && sameAmPm) {
			if (timeBetween <= -15) {
				status = PRIOR_YELLOW;
			} else if (timeBetween < -10 && timeBetween > -15) {
				status = PRIOR_GREEN;
			} else if (timeBetween > 10 && timeBetween < 15) {
				status = AFTER_GREEN;
			} else if (timeBetween >= 15) {
				status = AFTER_YELLOW;
			}
		}

		return status;
	}

	public boolean getOnTime() {
		return onTime;
	}

	@Override
	public String toString() {
		return "Log ID " + id
				+ " | Student: " + student.getID() + " - " + student.getName()
				+ " | Check in: " + signIn;
	}
}
