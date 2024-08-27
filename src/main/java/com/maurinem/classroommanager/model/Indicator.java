package com.maurinem.classroommanager.model;

public class Indicator {

	private int id;
	private String color, definition;

	public Indicator() {
	}

	public Indicator(int id, String color, String definition) {
		setId(id);
		setColor(color);
		setDefinition(definition);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDefinition() {
		return definition;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "● " + definition;
	}
}
