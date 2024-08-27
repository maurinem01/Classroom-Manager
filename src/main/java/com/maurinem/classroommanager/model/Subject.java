package com.maurinem.classroommanager.model;

/**
 * This defines int values to map what each subject ID means. It is defined in
 * its own class
 * because the window.* package needs access to it.
 * 
 * @author Maurine
 *
 */
public class Subject {

	/** Subject: Math only */
	public static final int MATH = 101;

	/** Subject: Reading only */
	public static final int READING = 102;

	/** Subject: Math & Reading */
	public static final int ALL = 103;

	/** Array containing all possible subject IDs */
	public static final int[] SUBJECTS = { MATH, READING, ALL };

}