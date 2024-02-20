package util;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.DefaultTableModel;

import object.Student;

/**
 * The contents of this model need to be accessed by index and by key.
 * @author Maurine
 *
 */
public class LeftPaneTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	
	/** Allows table to iterate by index */
	protected ArrayList<Student> studentList = new ArrayList<>();
	
	public LeftPaneTableModel() { }
	
	public LeftPaneTableModel(Map<Integer, Student> studentMap) {
		for (int i : studentMap.keySet())
			studentList.add(studentMap.get(i));
	}
	
	@Override
	public int getRowCount() { return studentList == null ? 0 : studentList.size(); }

	@Override
	public int getColumnCount() { return 3; }

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Student student = studentList.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return student.getFName();
		case 1:
			return student.getLName();
		case 2:
			return student.isActive();
		}
		return null;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "First Name";
		case 1:
			return "Last Name";
		case 2:
			return "Signed In";
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int column) { return false; }
}