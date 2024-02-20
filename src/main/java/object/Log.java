package object;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Log {

	private int id;
	private LocalDateTime signIn, signOut;
	private Student student;
	
	public Log(Student student) {
		this.student = student;
		this.signIn = LocalDateTime.now();
	}
	
	public Log(Student student, LocalDate date, LocalTime in, LocalTime out) {
		this.student = student;
		this.signIn = LocalDateTime.of(date, in);
		this.signOut = LocalDateTime.of(date, out);
	}
	
	public void setID(int id) { this.id = id; }
	
	public void setSignIn(LocalDateTime signIn) { this.signIn = signIn; }
		
	public void signOut() { this.signOut = LocalDateTime.now(); }
	
	public int getID() { return id; }
	
	public Student getStudent() { return student; }
	
	public LocalDateTime getSignIn() { return signIn; }
	
	public LocalDateTime getSignOut() { return signOut; }
	
	public String formatSignIn() {
		return signIn.format(DateTimeFormatter.ofPattern("h:mma")).replace(".", "");
	}
	
	/**
	 * Parse String of xx:xx{am/pm} format in to String[] with [0] = hour, [1] = minute, and [2] = am/pm.
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
		return new Object[] {timeInt[0], timeInt[1], ampm};
	}
	
	public boolean onTime(Appointment appointment) {
		Object[] appointmentTime = getTimeComponents(appointment.getTime());
		LocalTime appTime = LocalTime.of(
				(int) appointmentTime[0], 
				(int) appointmentTime[1]);
		LocalTime logTime = LocalTime.of(
				(int) getTimeComponents(formatSignIn())[0], 
				(int) getTimeComponents(formatSignIn())[1]);
//		System.out.println(Math.abs((Duration.between(appTime, logTime).getSeconds())/60)); 			// time between in minutes
		return appointment.getFirstName().toLowerCase().equals(student.getFName().toLowerCase()) &&		// same first name
				appointment.getLastName().toLowerCase().equals(student.getLName().toLowerCase()) &&		// same last name
				Math.abs((Duration.between(appTime, logTime).getSeconds())/60) <= 15  &&		// 15 minutes within appointment
				getTimeComponents(formatSignIn())[2].equals(appointmentTime[2]);				// same am/pm
	}
	
	@Override
	public String toString() {
		return "Log ID " + id 
				+ " | Student: " + student.getID() + " - " + student.getName()
				+ " | Check in: " + signIn;
	}
}
