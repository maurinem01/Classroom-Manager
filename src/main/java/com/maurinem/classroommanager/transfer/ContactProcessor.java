package com.maurinem.classroommanager.transfer;

import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import com.maurinem.classroommanager.dao.*;
import com.maurinem.classroommanager.model.*;

/**
 * This class reads the line from a String (contactRecord) and assigns the
 * values to a Contact object.
 * Each Contact attribute is separated by a comma (,)
 * 
 * @author Maurine
 *
 */
public class ContactProcessor implements Callable<Integer> {

	private String contactRecord;
	private ContactDAO dao;

	public ContactProcessor(String contactRecord, ContactDAO dao) {
		this.contactRecord = contactRecord;
		this.dao = dao;
	}

	@Override
	public Integer call() throws Exception {
		int rows = 0;
		StringTokenizer tokenizer = new StringTokenizer(contactRecord, ",");
		Contact contact = null;
		System.out.println(Thread.currentThread() + " processing record for: " + contactRecord);
		while (tokenizer.hasMoreTokens()) {
			contact = new Contact();
			contact.setID(Integer.parseInt(tokenizer.nextToken()));
			contact.setFName(tokenizer.nextToken());
			contact.setLName(tokenizer.nextToken());
			contact.setPhone(tokenizer.nextToken());
			contact.setEmail(tokenizer.nextToken());
			contact.setNotifications(Integer.parseInt(tokenizer.nextToken()));
			contact.setStudentID(Integer.parseInt(tokenizer.nextToken()));
			contact.setRelationshipID(Integer.parseInt(tokenizer.nextToken()));
			rows = dao.save(contact);
		}
		return rows;
	}

}
