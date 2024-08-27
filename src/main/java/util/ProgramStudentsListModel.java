package util;

import java.util.HashMap;

import javax.swing.DefaultListModel;

import model.Contact;
import model.Student;

public class ProgramStudentsListModel extends DefaultListModel<Student> {

	private static final long serialVersionUID = 1L;

	private HashMap<Integer, Student> studentsMap = new HashMap<>();

	public void add(Student s) {
		String contactPhones = "";
		add(0, s);
		studentsMap.put(s.getID(), s);
		for (int i : Contact.ALL) {
			Contact c = s.getContact(i);
			if (c != null && c.getPhone() != null && !c.getPhone().trim().isEmpty()) {
				contactPhones += c.getPhone() + ",";
			}
		}
		s.setContactPhones(contactPhones.split(","));
	}

	public void removeData(Student s) {
		if (s != null)
			studentsMap.remove(s.getID());
	}

	public HashMap<Integer, Student> getStudents() {
		return studentsMap;
	}
}
