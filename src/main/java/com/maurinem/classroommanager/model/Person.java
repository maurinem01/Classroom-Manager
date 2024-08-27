package com.maurinem.classroommanager.model;

public class Person implements Comparable<Person> {

	protected String fName, lName;
	protected int id; // pk in db

	public Person() {
	}

	public Person(String fName, String lName) {
		this.fName = fName;
		this.lName = lName;
	}

	public Person(int id, String fName, String lName) {
		this.id = id;
		this.fName = fName;
		this.lName = lName;
	}

	public int getID() {
		return id;
	}

	public String getFName() {
		return fName;
	}

	public String getLName() {
		return lName;
	}

	public String getName() {
		return fName + " " + lName;
	}

	public void setID(int id) {
		this.id = id;
	}

	public void setFName(String fName) {
		this.fName = fName;
	}

	public void setLName(String lName) {
		this.lName = lName;
	}

	@Override
	public String toString() {
		return id + " - " + fName + " " + lName;
	}

	@Override
	public int compareTo(Person person) {
		return toString().compareTo(person.toString());
	}
}