package com.maurinem.classroommanager.model;

public class Contact extends Person {

	public static final int ONE = 301;
	public static final int TWO = 302;
	/** Used to iterate through student's contacts */
	public static final int[] ALL = { ONE, TWO };

	private String phone, email;
	private boolean allowNotifications;
	private int studentID;
	private int relationshipID;

	public Contact() {
	}

	/** Contact information retrieved from db */
	public Contact(int ID, String firstName, String lastName, String phone,
			String email, boolean allowNotifications, int studentID, int relationshipID) {
		super(ID, firstName, lastName);
		setValues(phone, email, allowNotifications, studentID, relationshipID);
	}

	/** Used for creating a contact -- the DB will deal with creating an ID */
	public Contact(String firstName, String lastName, String phone,
			String email, boolean allowNotifications, int studentID, int relationshipID) {
		super(firstName, lastName);
		setValues(phone, email, allowNotifications, studentID, relationshipID);
	}

	private void setValues(String phone, String email, boolean allowNotifications,
			int studentID, int relationshipID) {
		this.phone = phone;
		this.email = email;
		this.allowNotifications = allowNotifications;
		this.studentID = studentID;
		this.relationshipID = relationshipID;
	}

	public String getPhone() {
		return phone;
	}

	public String getEmail() {
		return email;
	}

	public int getStudentID() {
		return studentID;
	}

	public int getRelationshipID() {
		assert (relationshipID == Contact.ONE || relationshipID == Contact.TWO)
				: "Relationship ID must be one of Contact.ONE or Contact.TWO";
		return relationshipID;
	}

	public boolean getAllowNotifications() {
		return this.allowNotifications;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setRelationshipID(int relationshipID) {
		this.relationshipID = relationshipID;
	}

	public void setAllowNotifications(boolean allowNotifications) {
		this.allowNotifications = allowNotifications;
	}

	@Override
	public String toString() {
		return getName() + "<" + phone + ">";
	}

	@Override
	public int hashCode() {
		return 20000 + id;
	}

	///// METHODS BELOW HERE ARE USED FOR ContactProcessor /////

	/** Used for ContactProcessor */
	private int tempNotifications;

	/** Used for ContactProcessor */
	public void setNotifications(int notifications) {
		this.tempNotifications = notifications;
	}

	/** Used for ContactProcessor */
	public int getNotifications() {
		return tempNotifications;
	}

	public void setStudentID(int studentID) {
		this.studentID = studentID;
	}

}
