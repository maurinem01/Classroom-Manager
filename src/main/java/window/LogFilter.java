package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import dao.LogDAO;
import dao.StudentDAO;
import object.Person;
import object.Student;
import util.LeftPaneTableModel;
import util.ProgramTable;
import util.ProgramStudentsListModel;
import util.SorterFilter;
import util.StudentTableList;

/**
 *
 * @author Maurine
 */
public class LogFilter extends Window {

	private static final long serialVersionUID = 1L;

	private JScrollPane allStudentsScrollPane;
	private JTable allStudentsTable;
	private JButton deleteButton;
	private JLabel deleteLabel1;
	private JLabel deleteLabel2;
	private JPanel deletePane;
	private JTextField deleteTextField;
	private JTextField filterDateFromTextField;
	private JLabel filterDateLabel1;
	private JLabel filterDateLabel2;
	private JTextField filterDateToTextField;
	private JPanel filterPane;
	private JLabel filterStudentsLabel;
	private JComboBox<String> filterTagComboBox;
	private DefaultComboBoxModel<String> filterTagComboBoxModel;
	private JLabel filterTagLabel;
	private JList<Student> filterStudentsList;
    private ProgramStudentsListModel filterStudentsListModel;

	private JScrollPane filterStudentsScrollPane;
	private JTextField searchTextField;
	private JButton viewButton;
	
	private Map<Integer, Student> allStudentsMap;
	private List<Student> allStudentsList = new ArrayList<>();
	private LeftPaneTableModel studentsTableModel;
	
	private LogDAO ld = new LogDAO();
	
    public LogFilter() {
    	initLists();
        initComponents();
    }

    private void initLists() {
		System.out.println("SendMessage.initLists()");
		allStudentsMap = new StudentDAO().readStudentContactMap();
		System.out.println(allStudentsMap.size()  + " students loaded");
		for (int i : allStudentsMap.keySet())
			allStudentsList.add(allStudentsMap.get(i));
	}
    
    protected void initComponents() {
    	System.out.println("LogFilter.initComponents()");
    	
        allStudentsScrollPane = new JScrollPane();
        searchTextField = new JTextField();
        studentsTableModel = new LeftPaneTableModel(allStudentsMap);
        
        // copy pasted
        allStudentsTable = new ProgramTable(studentsTableModel) {
        	private static final long serialVersionUID = 1L;

        	@Override
        	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        		Component comp = super.prepareRenderer(renderer, row, column);
        		Color foreground = this.getForeground();
        		
        		if (this.getModel().getValueAt(convertRowIndexToModel(row), 2) != null) {
        			String signedIn = this.getModel().getValueAt(convertRowIndexToModel(row), 2).toString();
        			if (signedIn.equals("true"))
        				foreground = new Color(200, 200, 200);
        		}
        		comp.setForeground(foreground);
        		
        		return comp;
        	}	
        };
        allStudentsScrollPane.setViewportView(allStudentsTable);

        TableRowSorter<TableModel> studentsTableRowSorter = new TableRowSorter<TableModel>(studentsTableModel);
		studentsTableRowSorter.setModel(studentsTableModel);
		allStudentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		allStudentsTable.setRowSorter(studentsTableRowSorter);
		allStudentsTable.getRowSorter().toggleSortOrder(0);
		allStudentsTable.setShowHorizontalLines(false);
		allStudentsTable.removeColumn(allStudentsTable.getColumnModel().getColumn(2));
        
        filterPane = new JPanel();
        filterStudentsLabel = new JLabel();
        filterStudentsScrollPane = new JScrollPane();
        filterStudentsList = new JList<>();
        filterDateLabel1 = new JLabel();
        filterDateLabel2 = new JLabel();
        filterDateFromTextField = new JTextField();
        filterDateToTextField = new JTextField();
        
        filterTagLabel = new JLabel();
        filterTagComboBox = new JComboBox<>();
        viewButton = new JButton("View Logs");
        
        deletePane = new JPanel();
        deleteLabel1 = new JLabel();
        deleteTextField = new JTextField();
        deleteLabel2 = new JLabel();
        deleteButton = new JButton("Delete Logs");

        filterPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filter", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

        filterStudentsLabel.setText("Include the following student(s):");
        filterStudentsListModel = new ProgramStudentsListModel();
        filterStudentsList.setModel(filterStudentsListModel);
        filterStudentsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        filterStudentsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		setText(value instanceof Person ? ((Person) value).getName() : null);
        		setBackground(Color.WHITE); 
        		return this;
        	}
        });
        filterStudentsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        filterStudentsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		setText(value instanceof Person ? ((Person) value).getName() : null);
        		setBackground(Color.WHITE); 
        		return this;
        	}
        });
        filterStudentsScrollPane.setViewportView(filterStudentsList);
        
        filterDateLabel1.setText("Date");
        filterDateFromTextField.setText("yyyy-mm-dd");
        filterDateLabel2.setText("to");
        filterDateToTextField.setText("yyyy-mm-dd");

        filterTagLabel.setText("Tag");
        filterTagComboBoxModel = new DefaultComboBoxModel<>();
        filterTagComboBoxModel.addElement("All");
        for (String s : ld.getTags())
        	filterTagComboBoxModel.addElement(s);
        filterTagComboBox.setModel(filterTagComboBoxModel);

        GroupLayout filterPaneLayout = new GroupLayout(filterPane);
        filterPane.setLayout(filterPaneLayout);
        filterPaneLayout.setHorizontalGroup(
            filterPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(filterPaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(filterStudentsScrollPane)
                    .addGroup(filterPaneLayout.createSequentialGroup()
                        .addGroup(filterPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                            .addComponent(filterStudentsLabel)
                            .addGroup(filterPaneLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                .addGroup(GroupLayout.Alignment.LEADING, filterPaneLayout.createSequentialGroup()
                                    .addComponent(filterTagLabel)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(filterTagComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(viewButton))
                                .addGroup(GroupLayout.Alignment.LEADING, filterPaneLayout.createSequentialGroup()
                                    .addComponent(filterDateLabel1)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(filterDateFromTextField, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(filterDateLabel2)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(filterDateToTextField, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        filterPaneLayout.setVerticalGroup(
            filterPaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(filterPaneLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(filterStudentsLabel)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterStudentsScrollPane, GroupLayout.PREFERRED_SIZE, 86, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(filterPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(filterDateLabel1)
                    .addComponent(filterDateFromTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterDateToTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterDateLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(filterPaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(viewButton)
                    .addComponent(filterTagLabel)
                    .addComponent(filterTagComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        deletePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Delete", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

        deleteLabel1.setText("Delete logs older than");
        deleteTextField.setText("30");
        deleteLabel2.setText("days.");

        GroupLayout deletePaneLayout = new GroupLayout(deletePane);
        deletePane.setLayout(deletePaneLayout);
        deletePaneLayout.setHorizontalGroup(
            deletePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(deletePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(deleteLabel1)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteTextField, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteLabel2)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(GroupLayout.Alignment.TRAILING, deletePaneLayout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(deleteButton)
                .addContainerGap())
        );
        deletePaneLayout.setVerticalGroup(
            deletePaneLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(deletePaneLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(deletePaneLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(deleteLabel1)
                    .addComponent(deleteTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                    .addComponent(deleteLabel2))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(deleteButton)
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        searchTextField.setText("Search...");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(allStudentsScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(searchTextField, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addComponent(filterPane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(deletePane, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(allStudentsScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(filterPane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(deletePane, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Attendance Log");
        getContentPane().requestFocusInWindow();

        StudentTableList sl = new StudentTableList();
        
        searchTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) { searchTextField.setText(""); }
		});
        
        searchTextField.getDocument().addDocumentListener(
        		new SorterFilter(searchTextField, studentsTableRowSorter));
        
        allStudentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) { 
				sl.addStudent(me, allStudentsList, allStudentsTable, filterStudentsListModel, studentsTableModel);
			}
		}); 

        filterStudentsList.addMouseListener(new MouseAdapter() {
        	@Override
			public void mouseClicked(MouseEvent me) {
        		sl.listListener(me, allStudentsMap, filterStudentsList, filterStudentsListModel, studentsTableModel);
        	}
        });
        
        viewButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) { 
				boolean valid = false;
				Pattern dateFormat = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
				if (dateFormat.matcher(filterDateFromTextField.getText()).matches() && dateFormat.matcher(filterDateToTextField.getText()).matches()) {
					valid = true;
				} else {
					valid = false;
					JOptionPane.showMessageDialog(null, "Date must match format yyyy-mm-dd.", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
				String[] fromDate = filterDateFromTextField.getText().split("-");
				String[] toDate = filterDateToTextField.getText().split("-");
					
				int fromYear, toYear, fromMonth, toMonth, fromDay, toDay;
				
				try {
					fromYear = Integer.parseInt(fromDate[0]);
					toYear = Integer.parseInt(toDate[0]);
					fromMonth = Integer.parseInt(fromDate[1]);
					toMonth = Integer.parseInt(toDate[1]);
					fromDay = Integer.parseInt(fromDate[2]);
					toDay = Integer.parseInt(toDate[2]);
					
					if ( fromMonth < 1 || fromMonth > 12 || toMonth < 1 || toMonth > 12) {
						JOptionPane.showMessageDialog(null, "Month must be between 1 an 12.", "Error", JOptionPane.ERROR_MESSAGE);			
						valid = false;
					} else if (fromDay < 1 || fromDay > maxDays(fromYear, fromMonth)) {
						JOptionPane.showMessageDialog(null, "First day must be between 1 and " + maxDays(fromYear, fromMonth) + ".", "Error", JOptionPane.ERROR_MESSAGE);
						valid = false;
					} else if (toDay < 1 || toDay > maxDays(toYear, toMonth)) {
						JOptionPane.showMessageDialog(null, "Second day must be between 1 and " + maxDays(toYear, toMonth) + ".", "Error", JOptionPane.ERROR_MESSAGE);
						valid = false;
					} else {
						valid = true;
					}
					
					LocalDate to = LocalDate.of(toYear, toMonth, toDay);
					LocalDate from = LocalDate.of(fromYear, fromMonth, fromDay);
					
					if (!to.isBefore(from)) {
						valid = true;
					} else {
						JOptionPane.showMessageDialog(null, "Second date cannot be before first date.", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (NumberFormatException e) {
					System.err.println(e);
				}
				
				if (valid) {
					System.out.println("YAY, YOU PRESSED VIEW");
				}
			}
		});
    } 
    
    
    private int maxDays(int year, int month) {
    	switch (month) {
		case 4:
		case 6:
		case 9:
		case 11:
			return 30;
		case 2:
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) // leap year
				return 29;
			else
				return 28;
		default:
			return 31;	
		}
    }
}
