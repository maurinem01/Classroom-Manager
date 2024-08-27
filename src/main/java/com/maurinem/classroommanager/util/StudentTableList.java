package com.maurinem.classroommanager.util;

import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.maurinem.classroommanager.model.Student;

public class StudentTableList {

	public void listListener(MouseEvent me, Map<Integer, Student> map, JList<Student> list,
			ProgramStudentsListModel listModel, DefaultTableModel tableModel) { // I wanted to call this LISTener but
																				// that's confusing xD
		if (me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1) {
			if (list.getSelectedValue() != null) {
				map.get(list.getSelectedValue().getID()).signOut();
				listModel.removeData(list.getSelectedValue()); // Remove from list of students/(contacts) to send
			}
			if (list.getSelectedIndex() > -1)
				listModel.remove(list.getSelectedIndex()); // Remove visually from JList model
			tableModel.fireTableDataChanged();
		}
	}

	public void addStudent(MouseEvent me, List<Student> studentList, JTable table, ProgramStudentsListModel listModel,
			DefaultTableModel tableModel) {
		if (me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1) {
			Student activeStudent = studentList.get(table.convertRowIndexToModel(table.getSelectedRow()));
			listModel.add(activeStudent);
			activeStudent.signIn();
			tableModel.fireTableDataChanged();
		}

	}

}
