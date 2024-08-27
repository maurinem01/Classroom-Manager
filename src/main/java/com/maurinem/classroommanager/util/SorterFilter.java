package com.maurinem.classroommanager.util;

import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class SorterFilter implements DocumentListener {

	JTextField textField;
	TableRowSorter<TableModel> sorter;

	public SorterFilter(JTextField textField, TableRowSorter<TableModel> sorter) {
		this.textField = textField;
		this.sorter = sorter;
	}

	@Override
	public void insertUpdate(DocumentEvent de) {
		filter(de);
	}

	@Override
	public void removeUpdate(DocumentEvent de) {
		filter(de);
	}

	@Override
	public void changedUpdate(DocumentEvent de) {
		filter(de);
	}

	private void filter(DocumentEvent de) {
		String text = textField.getText();
		if (text.trim().length() == 0)
			sorter.setRowFilter(null);
		else
			sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
	}

}
