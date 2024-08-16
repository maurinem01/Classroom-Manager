package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import dao.ContactDAO;
import dao.IndicatorDAO;
import dao.StudentDAO;
import object.Contact;
import object.Indicator;
import object.Student;
import object.Subject;
import util.LeftPaneTableModel;
import util.ProgramException;
import util.ProgramTable;
import util.SorterFilter;

/**
 *
 * @author Maurine
 */
public class EditStudents extends Window {

	private static final long serialVersionUID = 1L;
	private HashMap<Integer, Student> allStudentsMap;
	/**
	 * ArrayList for right pane table -- purpose is to access data by index rather
	 * than key
	 */
	private List<Student> allStudentsList = new ArrayList<Student>();
	private HashMap<Integer, Indicator> indicators;
	private Student activeStudent;
	private Contact c1, c2;

	/// LEFT PANEL ///
	private JPanel leftPanel;
	private JTextField searchTextField;
	private JScrollPane studentsScroller;
	private StudentsTableModel studentsTableModel;
	private JTable studentsTable;

	/// STUDENTS ///
	private JPanel studentInformation;
	private JLabel studentBirthdayLabel, studentFirstNameLabel, studentLastNameLabel,
			studentSubjectLabel, studentNotesExpiryDateLabel, studentNotesExpiryLabel,
			studentNotesLabel, studentTagLabel;
	private JTextField studentFirstNameTextField, studentLastNameTextField,
			studentNotesExpiryTextField, studentNotesTextField, studentTagTextField;
	private DefaultComboBoxModel<String> yearModel, monthModel, dayModel;
	private DefaultComboBoxModel<Indicator> indicatorModel;
	private JComboBox<String> studentBirthdayDay, studentBirthdayMonth, studentBirthdayYear;
	private JComboBox<Indicator> studentTagComboBox;
	private ButtonGroup studentSubject;
	private JRadioButton studentSubjectMath, studentSubjectMathReading, studentSubjectReading;

	/// CONTACTS ///
	private JTabbedPane contactInformation;
	private JPanel contact1, contact2;
	private JLabel cFirstNameLabel1, cFirstNameLabel2;
	private JTextField cFirstNameTextField1, cFirstNameTextField2;
	private JLabel cLastNameLabel1, cLastNameLabel2;
	private JTextField cLastNameTextField1, cLastNameTextField2;
	private JLabel cCellNumberLabel1, cCellNumberLabel2;
	private JTextField cCellNumberTextField1, cCellNumberTextField2;
	private JLabel cEmailLabel1, cEmailLabel2;
	private JTextField cEmailTextField1, cEmailTextField2;
	private JCheckBox cNotificationsCheckBox1, cNotificationsCheckBox2;

	private JButton clearExpNotesButton, newStudentButton, removeStudentButton, saveButton;

	private int selectedYear, selectedMonth;
	private boolean newStudent = true;
	private DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	public EditStudents() {
		initLists();
		initComponents();
	}

	private void initLists() {
		if (allStudentsMap != null) {
			allStudentsMap.clear();
		}
		allStudentsList.clear();
		allStudentsMap = new StudentDAO().readStudentContactMap();

		for (int i : allStudentsMap.keySet())
			allStudentsList.add(allStudentsMap.get(i));

		indicators = new IndicatorDAO().read();
	}

	protected void initComponents() {
		// initial values for month and year jcombobox
		int yearMin = LocalDateTime.now().getYear() - 20;
		selectedMonth = 1;
		selectedYear = yearMin;

		studentInformation = new JPanel();
		studentFirstNameLabel = new JLabel();
		studentFirstNameTextField = new JTextField();
		studentLastNameLabel = new JLabel();
		studentLastNameTextField = new JTextField();

		studentBirthdayLabel = new JLabel();
		studentBirthdayYear = new JComboBox<>();
		studentBirthdayMonth = new JComboBox<>();
		studentBirthdayDay = new JComboBox<>();

		studentSubjectLabel = new JLabel();
		studentSubject = new ButtonGroup();
		studentSubjectMath = new JRadioButton();
		studentSubjectReading = new JRadioButton();
		studentSubjectMathReading = new JRadioButton();

		studentTagLabel = new JLabel();
		studentTagTextField = new JTextField();

		studentNotesLabel = new JLabel();
		studentNotesTextField = new JTextField();
		studentNotesExpiryLabel = new JLabel();
		studentNotesExpiryTextField = new JTextField();
		studentNotesExpiryDateLabel = new JLabel();

		studentTagComboBox = new JComboBox<>();

		contactInformation = new JTabbedPane();

		contact1 = new JPanel();
		cFirstNameLabel1 = new JLabel();
		cFirstNameTextField1 = new JTextField();
		cLastNameLabel1 = new JLabel();
		cLastNameTextField1 = new JTextField();
		cCellNumberLabel1 = new JLabel();
		cCellNumberTextField1 = new JTextField();
		cEmailLabel1 = new JLabel();
		cEmailTextField1 = new JTextField();
		cNotificationsCheckBox1 = new JCheckBox();
		cNotificationsCheckBox1.setSelected(true);

		contact2 = new JPanel();
		cFirstNameLabel2 = new JLabel();
		cFirstNameTextField2 = new JTextField();
		cLastNameLabel2 = new JLabel();
		cLastNameTextField2 = new JTextField();
		cCellNumberLabel2 = new JLabel();
		cCellNumberTextField2 = new JTextField();
		cEmailLabel2 = new JLabel();
		cEmailTextField2 = new JTextField();
		cNotificationsCheckBox2 = new JCheckBox();

		newStudentButton = new JButton("New");
		removeStudentButton = new JButton("Delete");
		saveButton = new JButton("Save");
		clearExpNotesButton = new JButton("Clear Exp Notes");

		leftPanel = new JPanel();
		studentsScroller = new JScrollPane();
		studentsTable = new JTable();
		searchTextField = new JTextField("Search...");

		studentInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Student Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

		studentFirstNameLabel.setText("First Name");
		studentLastNameLabel.setText("Last Name");
		studentBirthdayLabel.setText("Birthday");

		yearModel = new DefaultComboBoxModel<String>();
		yearModel.addElement("Year");
		for (int i = yearMin; i <= LocalDateTime.now().getYear(); i++)
			yearModel.addElement(i + "");

		monthModel = new DefaultComboBoxModel<String>();
		monthModel.addElement("Month");
		for (int i = 1; i <= 12; i++) {
			if (i < 10)
				monthModel.addElement("0" + i);
			else
				monthModel.addElement(i + "");
		}

		dayModel = new DefaultComboBoxModel<String>();
		dayModel.addElement("Day");

		indicatorModel = new DefaultComboBoxModel<Indicator>();
		for (int x : indicators.keySet())
			indicatorModel.addElement(indicators.get(x));

		studentBirthdayYear.setModel(yearModel);
		studentBirthdayMonth.setModel(monthModel);
		studentBirthdayDay.setModel(dayModel);

		studentSubjectLabel.setText("Subject(s)");
		studentSubjectMath.setText("Math");
		studentSubjectReading.setText("Reading");
		studentSubjectMathReading.setText("Math & Reading");

		studentNotesLabel.setText("Note");
		studentNotesExpiryLabel.setText("Exp");
		studentNotesExpiryDateLabel.setText("yyyy/MM/dd");
		studentTagLabel.setText("Tags ");

		studentTagComboBox.setModel(indicatorModel);
		studentTagComboBox.setRenderer(new BasicComboBoxRenderer() {
			/** */
			private static final long serialVersionUID = 1L;

			/**
			 * @param list
			 * @param value
			 * @param index
			 * @param isSelected
			 * @param cellHasFocus
			 * @return
			 */
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				Indicator item = (Indicator) value;
				setForeground(Color.decode(item.getColor()));
				return this;
			}
		});

		GroupLayout studentInformationLayout = new GroupLayout(studentInformation);
		studentInformation.setLayout(studentInformationLayout);
		studentInformationLayout.setHorizontalGroup(
				studentInformationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(studentInformationLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(studentInformationLayout.createSequentialGroup()
												.addComponent(studentBirthdayLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(studentBirthdayYear, GroupLayout.PREFERRED_SIZE, 93,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(studentBirthdayMonth, GroupLayout.PREFERRED_SIZE, 104,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(studentBirthdayDay, 0, GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE))
										.addGroup(studentInformationLayout.createSequentialGroup()
												.addGroup(studentInformationLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(studentFirstNameLabel)
														.addComponent(studentLastNameLabel))
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addGroup(studentInformationLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addComponent(studentFirstNameTextField)
														.addComponent(studentLastNameTextField)))
										.addGroup(studentInformationLayout.createSequentialGroup()
												.addComponent(studentNotesLabel)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(studentNotesTextField, GroupLayout.PREFERRED_SIZE, 325,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(studentInformationLayout.createSequentialGroup()
												.addGroup(studentInformationLayout
														.createParallelGroup(GroupLayout.Alignment.LEADING)
														.addGroup(studentInformationLayout.createSequentialGroup()
																.addComponent(studentSubjectLabel)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentSubjectMath)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentSubjectReading)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentSubjectMathReading))
														.addGroup(studentInformationLayout.createSequentialGroup()
																.addComponent(studentNotesExpiryLabel)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(studentNotesExpiryTextField,
																		GroupLayout.PREFERRED_SIZE, 35,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentNotesExpiryDateLabel,
																		GroupLayout.PREFERRED_SIZE, 85,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(
																		LayoutStyle.ComponentPlacement.UNRELATED)
																.addComponent(studentTagLabel)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentTagTextField,
																		GroupLayout.PREFERRED_SIZE, 35,
																		GroupLayout.PREFERRED_SIZE)
																.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
																.addComponent(studentTagComboBox,
																		GroupLayout.PREFERRED_SIZE, 92,
																		GroupLayout.PREFERRED_SIZE)))
												.addGap(0, 0, Short.MAX_VALUE)))
								.addContainerGap()));
		studentInformationLayout.setVerticalGroup(
				studentInformationLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(studentInformationLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
										.addComponent(studentFirstNameTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(studentFirstNameLabel))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(studentLastNameLabel)
										.addComponent(studentLastNameTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(studentBirthdayLabel)
										.addComponent(studentBirthdayYear, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(studentBirthdayMonth, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(studentBirthdayDay, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(studentSubjectLabel)
										.addComponent(studentSubjectMath)
										.addComponent(studentSubjectReading)
										.addComponent(studentSubjectMathReading))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(studentNotesLabel)
										.addComponent(studentNotesTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(studentInformationLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(studentNotesExpiryLabel)
										.addComponent(studentNotesExpiryTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(studentTagLabel)
										.addComponent(studentTagTextField, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE)
										.addComponent(studentNotesExpiryDateLabel)
										.addComponent(studentTagComboBox, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addContainerGap(12, Short.MAX_VALUE)));

		contactInformation.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(),
				"Contact Information", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

		cFirstNameLabel1.setText("First Name");
		cLastNameLabel1.setText("Last Name");
		cCellNumberLabel1.setText("Cellphone #");
		cEmailLabel1.setText("Email");
		cNotificationsCheckBox1.setText("Notifications");

		GroupLayout contact1Layout = new GroupLayout(contact1);
		contact1.setLayout(contact1Layout);
		contact1Layout.setHorizontalGroup(
				contact1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(contact1Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contact1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, contact1Layout.createSequentialGroup()
												.addGap(0, 0, Short.MAX_VALUE)
												.addComponent(cNotificationsCheckBox1))
										.addGroup(contact1Layout.createSequentialGroup()
												.addComponent(cEmailLabel1)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cEmailTextField1))
										.addGroup(contact1Layout.createSequentialGroup()
												.addComponent(cFirstNameLabel1)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cFirstNameTextField1))
										.addGroup(contact1Layout.createSequentialGroup()
												.addComponent(cCellNumberLabel1)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cCellNumberTextField1, GroupLayout.DEFAULT_SIZE, 266,
														Short.MAX_VALUE))
										.addGroup(contact1Layout.createSequentialGroup()
												.addComponent(cLastNameLabel1)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cLastNameTextField1)))
								.addContainerGap()));
		contact1Layout.setVerticalGroup(
				contact1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(contact1Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contact1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cFirstNameLabel1)
										.addComponent(cFirstNameTextField1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cLastNameLabel1)
										.addComponent(cLastNameTextField1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cCellNumberLabel1)
										.addComponent(cCellNumberTextField1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cEmailLabel1)
										.addComponent(cEmailTextField1, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(cNotificationsCheckBox1)
								.addContainerGap(7, Short.MAX_VALUE)));

		contactInformation.addTab("Contact 1", contact1);
		cFirstNameLabel2.setText("First Name");
		cLastNameLabel2.setText("Last Name");
		cCellNumberLabel2.setText("Cellphone #");
		cEmailLabel2.setText("Email");
		cNotificationsCheckBox2.setText("Notifications");

		GroupLayout contact2Layout = new GroupLayout(contact2);
		contact2.setLayout(contact2Layout);
		contact2Layout.setHorizontalGroup(
				contact2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(contact2Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contact2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(contact2Layout.createSequentialGroup()
												.addComponent(cEmailLabel2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cEmailTextField2))
										.addGroup(contact2Layout.createSequentialGroup()
												.addComponent(cFirstNameLabel2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cFirstNameTextField2))
										.addGroup(contact2Layout.createSequentialGroup()
												.addComponent(cCellNumberLabel2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cCellNumberTextField2, GroupLayout.DEFAULT_SIZE, 266,
														Short.MAX_VALUE))
										.addGroup(contact2Layout.createSequentialGroup()
												.addComponent(cLastNameLabel2)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(cLastNameTextField2))
										.addGroup(GroupLayout.Alignment.TRAILING, contact2Layout.createSequentialGroup()
												.addGap(0, 0, Short.MAX_VALUE)
												.addComponent(cNotificationsCheckBox2)))
								.addContainerGap()));
		contact2Layout.setVerticalGroup(
				contact2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(contact2Layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(contact2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cFirstNameLabel2)
										.addComponent(cFirstNameTextField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cLastNameLabel2)
										.addComponent(cLastNameTextField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cCellNumberLabel2)
										.addComponent(cCellNumberTextField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(contact2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(cEmailLabel2)
										.addComponent(cEmailTextField2, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(cNotificationsCheckBox2)
								.addContainerGap(7, Short.MAX_VALUE)));

		contactInformation.addTab("Contact 2", contact2);

		studentsTableModel = new StudentsTableModel(allStudentsMap);
		studentsTable = new ProgramTable(studentsTableModel);
		studentsScroller.setViewportView(studentsTable);

		TableRowSorter<TableModel> studentsTableRowSorter = new TableRowSorter<TableModel>(studentsTableModel);
		studentsTableRowSorter.setModel(studentsTableModel);
		studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentsTable.setRowSorter(studentsTableRowSorter);
		studentsTable.getRowSorter().toggleSortOrder(0);
		studentsTable.getColumnModel().getColumn(2).setMinWidth(50);
		studentsTable.getColumnModel().getColumn(2).setMaxWidth(50);
		studentsTable.setShowHorizontalLines(false);

		GroupLayout leftPanelLayout = new GroupLayout(leftPanel);
		leftPanel.setLayout(leftPanelLayout);
		leftPanelLayout.setHorizontalGroup(
				leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addComponent(searchTextField, GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE)
						.addComponent(studentsScroller, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE));
		leftPanelLayout.setVerticalGroup(
				leftPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, leftPanelLayout.createSequentialGroup()
								.addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(studentsScroller, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
										.addGroup(layout.createSequentialGroup()
												.addComponent(newStudentButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
														GroupLayout.DEFAULT_SIZE,
														Short.MAX_VALUE)
												.addComponent(clearExpNotesButton))
										.addComponent(leftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
												GroupLayout.PREFERRED_SIZE))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addGroup(layout
														.createParallelGroup(GroupLayout.Alignment.LEADING, false)
														.addComponent(studentInformation, GroupLayout.DEFAULT_SIZE,
																GroupLayout.DEFAULT_SIZE,
																Short.MAX_VALUE)
														.addComponent(contactInformation))
												.addGap(6, 6, 6))
										.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
												.addComponent(removeStudentButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(saveButton)
												.addContainerGap()))));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap()
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
										.addGroup(layout.createSequentialGroup()
												.addComponent(studentInformation, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(contactInformation, GroupLayout.PREFERRED_SIZE, 218,
														GroupLayout.PREFERRED_SIZE))
										.addGroup(layout.createSequentialGroup()
												.addGap(8, 8, 8)
												.addComponent(leftPanel, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE,
														GroupLayout.PREFERRED_SIZE)))
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
										.addComponent(removeStudentButton)
										.addComponent(saveButton)
										.addComponent(newStudentButton)
										.addComponent(clearExpNotesButton))
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		studentSubject.add(studentSubjectMath);
		studentSubject.add(studentSubjectReading);
		studentSubject.add(studentSubjectMathReading);

		pack();
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setTitle("Edit Students");
		getContentPane().requestFocusInWindow();

		emptyFields(); // SET DEFAULT VALUES

		searchTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				searchTextField.setText("");
			}
		});

		searchTextField.getDocument().addDocumentListener(
				new SorterFilter(searchTextField, studentsTableRowSorter));

		studentsTable.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent ke) {
				if (ke.getKeyCode() == KeyEvent.VK_UP || ke.getKeyCode() == KeyEvent.VK_DOWN)
					selectStudent();
			}
		});

		studentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				selectStudent();
			}
		});

		studentBirthdayYear.addActionListener(e -> {
			try {
				selectedYear = Integer.parseInt(yearModel.getSelectedItem().toString());
				updateDayComboBox(selectedYear, selectedMonth);
			} catch (NumberFormatException ex) {
				// ignore number format exception -- empty year and month is expected
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		studentBirthdayMonth.addActionListener(e -> {
			try {
				selectedMonth = Integer.parseInt(monthModel.getSelectedItem().toString());
				updateDayComboBox(selectedYear, selectedMonth);
			} catch (NumberFormatException ex) {
				// ignore number format exception -- empty year and month is expected
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		});

		studentTagComboBox.addActionListener(e -> {
			studentTagComboBox
					.setForeground(Color.decode(((Indicator) studentTagComboBox.getSelectedItem()).getColor()));
		});

		studentNotesExpiryTextField.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				updateDate();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				updateDate();
			}
		});

		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				int subjectSelected = 0;
				String tag = "";
				String birthday = "";
				String notes = "";
				boolean validSave = true;

				int studentIndicatorID = ((Indicator) studentTagComboBox.getSelectedItem()).getId();

				Student activeStudent = new Student();
				Student newStudentEntry = new Student();
				StudentDAO studentDAO = new StudentDAO();

				if (studentsTable.getSelectedRowCount() > 0)
					activeStudent = allStudentsList
							.get(studentsTable.convertRowIndexToModel(studentsTable.getSelectedRow()));

				try {
					if ((studentFirstNameTextField.getText().trim().isEmpty())
							|| (studentLastNameTextField.getText().trim().isEmpty())) {
						validSave = false;
						JOptionPane.showMessageDialog(null, "Enter a valid name.", "Error", JOptionPane.ERROR_MESSAGE);
						throw new ProgramException("Name field must be filled");
					}

					if (studentTagTextField.getText().trim().isEmpty())
						tag = null;
					else if (studentTagTextField.getText().length() == 2)
						tag = studentTagTextField.getText();
					else {
						validSave = false;
						JOptionPane.showMessageDialog(null, "Tag must be 2 characters.", "Error",
								JOptionPane.ERROR_MESSAGE);
						throw new ProgramException("Invalid tag entered");
					}

					// MAKE SURE VALID NOTES EXPIRY
					if (!studentNotesTextField.getText().trim().isEmpty()) {
						notes = studentNotesTextField.getText();
						if (studentNotesExpiryTextField.getText().trim().isEmpty()) {
							validSave = false;
							JOptionPane.showMessageDialog(null, "Enter an expiry for this note.", "Error",
									JOptionPane.ERROR_MESSAGE);
							throw new ProgramException("If note is filled, expiry text field must be filled");
						}
					}

					if (studentBirthdayYear.getSelectedIndex() != 0 && studentBirthdayMonth.getSelectedIndex() != 0
							&& studentBirthdayDay.getSelectedIndex() != 0)
						birthday = studentBirthdayMonth.getSelectedItem() + "-" + studentBirthdayDay.getSelectedItem()
								+ "-"
								+ studentBirthdayYear.getSelectedItem();
					else {
						validSave = false;
						JOptionPane.showMessageDialog(null, "Enter a valid birthday.", "Error",
								JOptionPane.ERROR_MESSAGE);
						throw new ProgramException("Birthday values must be parseable to Integer");
					}

					if (studentSubject.getSelection() != null) {
						if (studentSubjectMath.isSelected())
							subjectSelected = Subject.MATH;
						else if (studentSubjectReading.isSelected())
							subjectSelected = Subject.READING;
						else if (studentSubjectMathReading.isSelected())
							subjectSelected = Subject.ALL;
					} else {
						validSave = false;
						JOptionPane.showMessageDialog(null, "Enter a valid subject.", "Error",
								JOptionPane.ERROR_MESSAGE);
						throw new ProgramException("No subject chosen");
					}

					if (validSave) {
						if (newStudent) {
							newStudentEntry = new Student(tag, studentFirstNameTextField.getText(),
									studentLastNameTextField.getText(), birthday, subjectSelected, notes,
									LocalDateTime.now(),
									studentIndicatorID);
							studentDAO.create(newStudentEntry);
							JOptionPane.showMessageDialog(null,
									"Entry created for " + studentFirstNameTextField.getText() + " "
											+ studentLastNameTextField.getText()
											+ ".",
									"New Entry", JOptionPane.INFORMATION_MESSAGE);
						} else {
							if (!studentNotesExpiryDateLabel.getText().trim().isEmpty()) {
								String dateTime = studentNotesExpiryDateLabel.getText() + " 00:00"; // set expiry to
																									// 00:00 of
																									// notesExpiryDateLabel.getText()
								DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
								activeStudent.setNotesExpiryDate(LocalDateTime.parse(dateTime, dateTimeFormat));
							}
							activeStudent.setTag(tag);
							activeStudent.setFName(studentFirstNameTextField.getText());
							activeStudent.setLName(studentLastNameTextField.getText());
							activeStudent.setBirthday(birthday);
							activeStudent.setSubjectID(subjectSelected);
							activeStudent.setNotes(notes);
							activeStudent.setIndicatorID(studentIndicatorID);
							studentDAO.update(activeStudent);
						}
					}

				} catch (ProgramException e) {
					System.err.println(e);
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (newStudent) {
					createContact(cFirstNameTextField1, cLastNameTextField1, cCellNumberTextField1, cEmailTextField1,
							cNotificationsCheckBox1, newStudentEntry.getID(), Contact.ONE);
					createContact(cFirstNameTextField2, cLastNameTextField2, cCellNumberTextField2, cEmailTextField2,
							cNotificationsCheckBox2, newStudentEntry.getID(), Contact.TWO);
				} else {
					if (activeStudent.getContact(Contact.ONE) == null) {
						createContact(cFirstNameTextField1, cLastNameTextField1, cCellNumberTextField1,
								cEmailTextField1,
								cNotificationsCheckBox1, activeStudent.getID(), Contact.ONE);
					}
					if (activeStudent.getContact(Contact.TWO) == null) {
						createContact(cFirstNameTextField2, cLastNameTextField2, cCellNumberTextField2,
								cEmailTextField2,
								cNotificationsCheckBox2, activeStudent.getID(), Contact.TWO);
					}
					if (activeStudent.getContact(Contact.ONE) != null) {
						updateContact(c1, cFirstNameTextField1, cLastNameTextField1, cCellNumberTextField1,
								cEmailTextField1,
								cNotificationsCheckBox1);
					}
					if (activeStudent.getContact(Contact.TWO) != null) {
						updateContact(c2, cFirstNameTextField2, cLastNameTextField2, cCellNumberTextField2,
								cEmailTextField2,
								cNotificationsCheckBox2);
					}
				}

				if (validSave) {
					if (!newStudent)
						JOptionPane.showMessageDialog(null,
								"Entry updated for " + studentFirstNameTextField.getText() + " "
										+ studentLastNameTextField.getText()
										+ ".",
								"Updated Entry", JOptionPane.INFORMATION_MESSAGE);
					emptyFields();
					refreshStudentList();
				}

			}
		});

		clearExpNotesButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				StudentDAO studentDAO = new StudentDAO();
				for (Student s : allStudentsList) {
					if (!s.getNotes().trim().isEmpty() && !s.getNotesExpiryDate().isAfter(LocalDateTime.now())) {
						s.setNotes("");
						studentDAO.update(s);
					}
				}
				studentsTableModel.fireTableDataChanged();
			}
		});

		newStudentButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				emptyFields();
			}
		});

		removeStudentButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				try {
					if (studentsTable.getSelectedRowCount() > 0) {
						Student selectedStudent = allStudentsList
								.get(studentsTable.convertRowIndexToModel(studentsTable.getSelectedRow()));
						int delete = JOptionPane.showConfirmDialog(null,
								"Are you sure you want to remove " + selectedStudent.getName() + "?",
								"Delete Confirmation",
								JOptionPane.YES_NO_OPTION);
						if (delete == JOptionPane.YES_OPTION) {
							new StudentDAO().delete(selectedStudent.getID()); // MySQL will handle deleting entries from
																				// log and
																				// contact
							JOptionPane.showMessageDialog(null, "Deleted entry for " + selectedStudent.getName() + ".");
						}
					} else {
						JOptionPane.showMessageDialog(null, "Select a student to delete.", "No Selection",
								JOptionPane.WARNING_MESSAGE);
						throw new ProgramException("Student must be selected in order to delete");
					}
				} catch (ProgramException e) {
					System.err.println(e);
				} catch (Exception e) {
					e.printStackTrace();
				}

				emptyFields();
				refreshStudentList();
			}
		});
	}

	private void selectStudent() {
		emptyFields();
		newStudent = false;
		try {
			activeStudent = allStudentsList.get(studentsTable.convertRowIndexToModel(studentsTable.getSelectedRow()));

			studentFirstNameTextField.setText(activeStudent.getFName());
			studentLastNameTextField.setText(activeStudent.getLName());
			studentBirthdayYear.setSelectedItem(activeStudent.birthday()[2]);
			studentBirthdayMonth.setSelectedItem(activeStudent.birthday()[0]);
			studentBirthdayDay.setSelectedItem(activeStudent.birthday()[1]);
			studentTagComboBox.setForeground(Color.decode(indicators.get(activeStudent.getIndicatorID()).getColor()));
			studentTagComboBox.setSelectedItem(indicators.get(activeStudent.getIndicatorID()));
			Enumeration<AbstractButton> elements = studentSubject.getElements();

			while (elements.hasMoreElements()) { // select appropriate subject
				AbstractButton button = elements.nextElement();
				if (button.getActionCommand().equals(activeStudent.getSubject()))
					button.setSelected(true);
			}

			if (activeStudent.getTag() != null) // display tag
				studentTagTextField.setText(activeStudent.getTag());

			if (activeStudent.getNotes() != null && !activeStudent.getNotes().trim().isEmpty()) { // display notes +
																									// expiry
				studentNotesTextField.setText(activeStudent.getNotes());
				studentNotesExpiryTextField
						.setText((ChronoUnit.DAYS.between(LocalDateTime.now(), activeStudent.getNotesExpiryDate())
								+ ((activeStudent.getNotesExpiryDate().isAfter(LocalDateTime.now())) ? 1 : 0)) // make
																												// 2nd
																												// parameter
																												// inclusive
																												// if >
																												// 0
								+ ""); // convert to String
				studentNotesExpiryDateLabel.setText(dateFormat.format(activeStudent.getNotesExpiryDate()));
			} else {
				studentNotesExpiryTextField.setText("");
				studentNotesExpiryDateLabel.setText("");
			}

			if (activeStudent.getContact(Contact.ONE) != null) {
				c1 = activeStudent.getContact(Contact.ONE);
				fillContact(c1, cFirstNameTextField1, cLastNameTextField1, cCellNumberTextField1, cEmailTextField1,
						cNotificationsCheckBox1);
			}

			if (activeStudent.getContact(Contact.TWO) != null) {
				c2 = activeStudent.getContact(Contact.TWO);
				fillContact(c2, cFirstNameTextField2, cLastNameTextField2, cCellNumberTextField2, cEmailTextField2,
						cNotificationsCheckBox2);
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.err.println("Student selection out of bounds.");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private void fillContact(Contact c, JTextField firstNameField, JTextField lastNameField, JTextField phoneField,
			JTextField emailField, JCheckBox notificationBox) {
		firstNameField.setText(c.getFName());
		lastNameField.setText(c.getLName());
		phoneField.setText(c.getPhone());
		emailField.setText(c.getEmail());
		notificationBox.setSelected(c.getAllowNotifications());
	}

	/** Clears all JTextField and JCheckBox Components inside a JPanel */
	private void clear(JPanel panel) {
		if (panel == null)
			return;

		try {
			for (Component c : panel.getComponents()) {
				if (c instanceof JTextField)
					((JTextField) c).setText("");
				if (c instanceof JCheckBox)
					((JCheckBox) c).setSelected(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void emptyFields() {
		newStudent = true;
		clear(studentInformation);
		clear(contact1);
		clear(contact2);
		studentBirthdayYear.setSelectedIndex(0);
		studentBirthdayMonth.setSelectedIndex(0);
		studentBirthdayDay.setSelectedIndex(0);
		studentSubject.clearSelection();
		studentNotesExpiryTextField.setText("7");
		updateDate();
		studentTagComboBox.setSelectedIndex(0);
		cNotificationsCheckBox1.setSelected(true);
	}

	private void updateDate() {
		int days = 0; // default = 0
		try {
			days = Integer.parseInt(studentNotesExpiryTextField.getText());
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "Enter a valid integer for expiry days.", "Error",
					JOptionPane.ERROR_MESSAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		studentNotesExpiryDateLabel.setText(dateFormat.format(LocalDateTime.now().plusDays(days)));
	}

	private void updateContact(Contact contact, JTextField firstNameField, JTextField lastNameField,
			JTextField phoneField, JTextField emailField, JCheckBox notificationsBox) {
		ContactDAO contactDAO = new ContactDAO();
		try {
			if (contact != null) { // If contact already exists
				if ((!firstNameField.getText().trim().isEmpty()) && (!lastNameField.getText().trim().isEmpty())) {
					contact.setFName(firstNameField.getText());
					contact.setLName(lastNameField.getText());
					contact.setPhone(phoneField.getText());
					contact.setEmail(emailField.getText());
					contact.setAllowNotifications(notificationsBox.isSelected());
					contactDAO.update(contact);
				} else if ((firstNameField.getText().trim().isEmpty()) && (lastNameField.getText().trim().isEmpty())
						&& (phoneField.getText().trim().isEmpty()) && (emailField.getText().trim().isEmpty())) {
					contactDAO.delete(contact);
				} else {
					JOptionPane.showMessageDialog(this, "Contact name fields be filled.", "Error",
							JOptionPane.ERROR_MESSAGE);
					throw new ProgramException("Invalid update action");
				}
			}

		} catch (ProgramException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * If the first and last name fields of the contact fields are filled, a new
	 * contact entry will be added to the database
	 */
	private void createContact(JTextField firstNameField, JTextField lastNameField, JTextField phoneField,
			JTextField emailField, JCheckBox notificationsBox, int studentID, int relID) {
		ContactDAO contactDAO = new ContactDAO();
		try {
			if (!(firstNameField.getText().trim().isEmpty()) && !(lastNameField.getText().trim().isEmpty())) {
				String phone = !phoneField.getText().trim().isEmpty() ? phoneField.getText() : "";
				String email = !emailField.getText().trim().isEmpty() ? emailField.getText() : "";
				boolean notifications = notificationsBox.isSelected();
				contactDAO.create(
						new Contact(firstNameField.getText(), lastNameField.getText(), phone, email, notifications,
								studentID, relID));
			}
		} catch (ProgramException e) {
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Invoke for save and delete */
	private void refreshStudentList() {
		initLists();
		studentsTableModel.refreshData(allStudentsMap);
		studentsTableModel.fireTableDataChanged();
	}

	private void updateDayComboBox(int year, int month) {
		int maxDays;
		dayModel.removeAllElements();
		dayModel.addElement("Day");
		if (studentBirthdayMonth.getSelectedIndex() != 0 && studentBirthdayYear.getSelectedIndex() != 0) {
			switch (month) {
				case 4:
				case 6:
				case 9:
				case 11:
					maxDays = 30;
					break;
				case 2:
					if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) // leap year
						maxDays = 29;
					else
						maxDays = 28;
					break;
				default:
					maxDays = 31;
			}

			for (int i = 1; i <= maxDays; i++) {
				if (i < 10)
					dayModel.addElement("0" + i);
				else
					dayModel.addElement(i + "");
			}
		}
	}
}

class StudentsTableModel extends LeftPaneTableModel {

	private static final long serialVersionUID = 1L;

	public StudentsTableModel(HashMap<Integer, Student> allStudentsMap) {
		refreshData(allStudentsMap);
	}

	public void refreshData(HashMap<Integer, Student> allStudentsMap) {
		studentList.clear();
		setRowCount(0);
		for (int i : allStudentsMap.keySet())
			studentList.add(allStudentsMap.get(i));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Student student = studentList.get(rowIndex);
		switch (columnIndex) {
			case 0:
				return student.getFName();
			case 1:
				return student.getLName();
			case 2:
				if (student.getNotes() != null && student.getNotes().length() > 0) {
					if (student.getNotesExpiryDate().isAfter(LocalDateTime.now())) {
						return "YES";
					} else {
						return "EXP";
					}
				} else {
					return "";
				}
				// return student.getNotes() != null && student.getNotes().length() > 0 ? "Yes"
				// : "";
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
				return "Notes";
		}
		return null;
	}

}