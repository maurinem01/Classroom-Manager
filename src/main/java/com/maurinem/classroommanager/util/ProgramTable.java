package com.maurinem.classroommanager.util;

import java.awt.Font;
import java.awt.FontMetrics;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

public class ProgramTable extends JTable {

	private static final long serialVersionUID = 1L;

	public ProgramTable(DefaultTableModel model) {
		super(model);
		setDefaultRenderer(Object.class, new ProgramTableCellRenderer());
		setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public void changeSelectionMode(int newSelectionMode) {
		setSelectionMode(newSelectionMode);
	}

	FontMetrics metrics = this.getFontMetrics(this.getFont());
	int fontHeight = metrics.getHeight();

	@Override
	public int getRowHeight() {
		return fontHeight + 5;
	}

	@Override
	public boolean getRowSelectionAllowed() {
		return true;
	}

	@Override
	public boolean getDragEnabled() {
		return false;
	}

	@Override
	public boolean getShowVerticalLines() {
		return false;
	}

	@Override
	public Font getFont() {
		return Config.FONT;
	}

	public int selectionMode() {
		return 0;
	}
}
