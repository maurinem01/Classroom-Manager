package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import dao.ConfigDAO;
import model.Student;
import util.Config;
import util.ProgramTable;
import util.TextConverter;

/**
 *
 * @author Maurine
 */
public class Configurations extends Window {

	/** */
	private static final long serialVersionUID = 1L;

	private JPanel textNotificationsPanel;
	private JCheckBox sendMessagesCheckBox;
	private JLabel centreNameLabel, centrePhoneLabel, checkInTextLabel, checkOutTextLabel;
	private JTextField centreNameTextField, centrePhoneTextField;
	private JScrollPane checkInTextScrollPane, checkoutTextScrollPane;
	private JTextArea checkInTextArea, checkOutTextArea;
	private JSeparator checkInSeparator, checkOutSeparator;

	private JPanel configurationsPanel;
	private JLabel timeFormatLabel, previewLabel, sessionLengthLabel, minutesLabel, warningTimeLabel, minutesLabel1;
	private JComboBox<String> timeFormatComboBox;
	private DefaultComboBoxModel<String> timeFormatModel;
	private JTextField sessionLengthTextField, warningTimeTextField;

	private JPanel coloursPanel;
	private JLabel overTimeColourLabel, warningTimeColourLabel, changeSubjectColourLabel, noStatusColourLabel,
			notesColourLabel;
	private JButton overTimeColourTextButton, overTimeBGColourButton, warningTimeTextColourButton,
			warningTimeBGColourButton,
			subjectChangeTextColourButton, subjectChangeBGColourButton, noStatusTextColourButton,
			noStatusBGColourButton,
			notesBGColourButton, notesTextColourButton;

	private JScrollPane previewPanel;
	private JTable previewTable;
	private DefaultTableModel previewTableModel;
	private List<Student> previewTableData;

	private JButton saveButton, resetButton;

	private static Color overTimeFG, overTimeBG, warningTimeFG, warningTimeBG, subjectChangeFG, subjectChangeBG,
			noStatusFG, noStatusBG, notesFG, notesBG;

	DateTimeFormatter timeFormat;

	public Configurations() {
		initialValues();
		initComponents();
	}

	private void initialValues() {
		// configurations = new ConfigDAO();
		overTimeFG = Config.OVER_TIME_FG;
		overTimeBG = Config.OVER_TIME_BG;
		warningTimeFG = Config.WARNING_TIME_FG;
		warningTimeBG = Config.WARNING_TIME_BG;
		subjectChangeFG = Config.SUBJECT_CHANGE_FG;
		subjectChangeBG = Config.SUBJECT_CHANGE_BG;
		noStatusFG = Config.NO_STATUS_FG;
		noStatusBG = Config.NO_STATUS_BG;
		notesFG = Config.NOTES_FG;
		notesBG = Config.NOTES_BG;
		// System.out.println("#"+Integer.toHexString(overTimeBG.getRGB()).substring(2).toUpperCase());
	}

	public static Color overTimeBG() {
		return overTimeBG;
	}

	public static Color overTimeFG() {
		return overTimeFG;
	}

	public static Color warningBG() {
		return warningTimeBG;
	}

	public static Color warningFG() {
		return warningTimeFG;
	}

	public static Color subjectChangeBG() {
		return subjectChangeBG;
	}

	public static Color subjectChangeFG() {
		return subjectChangeFG;
	}

	public static Color notesBG() {
		return notesBG;
	}

	public static Color notesFG() {
		return notesFG;
	}

	public static Color noStatusBG() {
		return noStatusBG;
	}

	public static Color noStatusFG() {
		return noStatusFG;
	}

	protected void initComponents() {
		previewTableData = new ArrayList<>();
		previewTableData.add(new Student("Over Time", "Example notes"));
		previewTableData.add(new Student("Over Time", null));
		previewTableData.add(new Student("Warning Time", "Example notes"));
		previewTableData.add(new Student("Warning Time", null));
		previewTableData.add(new Student("Subject Change", "Example notes"));
		previewTableData.add(new Student("Subject Change", null));
		previewTableData.add(new Student("No Status", "Example notes"));
		previewTableData.add(new Student("No Status", null));
		previewTableModel = new PreviewTableModel(previewTableData);

		configurationsPanel = new JPanel();
		timeFormatLabel = new JLabel("Time format");
		timeFormatComboBox = new JComboBox<>();
		////////////////////
		sessionLengthLabel = new JLabel("Session length");
		sessionLengthTextField = new JTextField();
		minutesLabel = new JLabel("minutes");
		warningTimeLabel = new JLabel("Warning time");
		warningTimeTextField = new JTextField();
		minutesLabel1 = new JLabel("minutes");
		previewLabel = new JLabel();

		textNotificationsPanel = new JPanel();
		sendMessagesCheckBox = new JCheckBox("Send text notifications");
		centreNameLabel = new JLabel("Centre name");
		centreNameTextField = new JTextField();
		centrePhoneLabel = new JLabel("Phone number");
		centrePhoneTextField = new JTextField();

		checkInTextLabel = new JLabel("Check in text");
		checkInTextScrollPane = new JScrollPane();
		checkInTextArea = new JTextArea();
		checkInTextArea
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		checkInTextArea.setLineWrap(true);
		checkInTextArea.setWrapStyleWord(true);
		checkInSeparator = new JSeparator();

		checkOutTextLabel = new JLabel("Check out text");
		checkoutTextScrollPane = new JScrollPane();
		checkOutTextArea = new JTextArea();
		checkOutTextArea
				.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		checkOutTextArea.setLineWrap(true);
		checkOutTextArea.setWrapStyleWord(true);
		checkOutSeparator = new JSeparator();

		coloursPanel = new JPanel();
		overTimeColourLabel = new JLabel("Over time");
		overTimeColourTextButton = new JButton("FG");
		overTimeBGColourButton = new JButton("BG");
		warningTimeColourLabel = new JLabel("Warning");
		warningTimeTextColourButton = new JButton("FG");
		warningTimeBGColourButton = new JButton("BG");
		changeSubjectColourLabel = new JLabel("Subject change");
		subjectChangeBGColourButton = new JButton("BG");
		subjectChangeTextColourButton = new JButton("FG");
		noStatusColourLabel = new JLabel("No status");
		noStatusTextColourButton = new JButton("FG");
		noStatusBGColourButton = new JButton("BG");
		notesColourLabel = new JLabel("Notes");
		notesTextColourButton = new JButton("FG");
		notesBGColourButton = new JButton("BG");

		previewPanel = new JScrollPane();
		previewTable = new PreviewTable(previewTableModel);
		saveButton = new JButton("Save");
		resetButton = new JButton("Reset");

		timeFormatModel = new DefaultComboBoxModel<String>();
		timeFormatModel.addElement("12 hour");
		timeFormatModel.addElement("24 hour");
		configurationsPanel.setBorder(BorderFactory.createTitledBorder(Config.BORDER,
				"Configurations", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

		timeFormatComboBox.setModel(timeFormatModel);

		GroupLayout jPanel1Layout = new GroupLayout(configurationsPanel);
		configurationsPanel.setLayout(jPanel1Layout);
		jPanel1Layout.setHorizontalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap().addGroup(jPanel1Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING, false)
						.addGroup(jPanel1Layout.createSequentialGroup().addComponent(warningTimeLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(warningTimeTextField, GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
						.addGroup(jPanel1Layout.createSequentialGroup().addComponent(sessionLengthLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(sessionLengthTextField, GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE))
						.addGroup(jPanel1Layout.createSequentialGroup().addComponent(timeFormatLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(timeFormatComboBox, GroupLayout.PREFERRED_SIZE, 89,
										GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
								.addComponent(minutesLabel).addComponent(minutesLabel1).addComponent(previewLabel))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		jPanel1Layout.setVerticalGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel1Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(timeFormatLabel)
								.addComponent(timeFormatComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addComponent(previewLabel))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(sessionLengthLabel)
								.addComponent(sessionLengthTextField, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(minutesLabel))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(warningTimeLabel)
								.addComponent(warningTimeTextField, GroupLayout.PREFERRED_SIZE,
										GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(minutesLabel1))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		textNotificationsPanel.setBorder(BorderFactory.createTitledBorder(Config.BORDER,
				"Text Notifications", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

		checkInTextArea.setColumns(20);
		checkInTextArea.setRows(4);
		checkInTextScrollPane.setViewportView(checkInTextArea);

		checkOutTextArea.setColumns(20);
		checkOutTextArea.setRows(4);
		checkoutTextScrollPane.setViewportView(checkOutTextArea);

		GroupLayout jPanel2Layout = new GroupLayout(textNotificationsPanel);
		textNotificationsPanel.setLayout(jPanel2Layout);
		jPanel2Layout.setHorizontalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap().addGroup(jPanel2Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel2Layout.createSequentialGroup().addComponent(checkInTextLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).addComponent(checkInSeparator))
						.addGroup(jPanel2Layout.createSequentialGroup().addComponent(centreNameLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(centreNameTextField))
						.addGroup(GroupLayout.Alignment.TRAILING,
								jPanel2Layout.createSequentialGroup().addComponent(centrePhoneLabel)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(centrePhoneTextField, GroupLayout.DEFAULT_SIZE, 214,
												Short.MAX_VALUE))
						.addComponent(checkInTextScrollPane).addComponent(checkoutTextScrollPane)
						.addComponent(sendMessagesCheckBox)
						.addGroup(jPanel2Layout.createSequentialGroup().addComponent(checkOutTextLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(checkOutSeparator)))
						.addContainerGap()));
		jPanel2Layout.setVerticalGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel2Layout.createSequentialGroup().addContainerGap()
						.addComponent(sendMessagesCheckBox).addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(centreNameLabel).addComponent(centreNameTextField,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(centrePhoneLabel).addComponent(centrePhoneTextField,
										GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(checkInTextLabel).addComponent(checkInSeparator,
										GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(checkInTextScrollPane, 85, 85, 85)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel2Layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
								.addComponent(checkOutTextLabel).addComponent(checkOutSeparator,
										GroupLayout.PREFERRED_SIZE, 10, GroupLayout.PREFERRED_SIZE))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(checkoutTextScrollPane, 85, 85, 85)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		coloursPanel.setBorder(BorderFactory.createTitledBorder(Config.BORDER, "Colours",
				TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION));

		GroupLayout jPanel3Layout = new GroupLayout(coloursPanel);
		coloursPanel.setLayout(jPanel3Layout);
		jPanel3Layout.setHorizontalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap().addGroup(jPanel3Layout
						.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(jPanel3Layout.createSequentialGroup().addComponent(overTimeColourLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(overTimeColourTextButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(overTimeBGColourButton,
										GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel3Layout.createSequentialGroup().addComponent(warningTimeColourLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(warningTimeTextColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(warningTimeBGColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel3Layout.createSequentialGroup().addComponent(changeSubjectColourLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(subjectChangeTextColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(subjectChangeBGColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(jPanel3Layout.createSequentialGroup().addComponent(noStatusColourLabel)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE,
										Short.MAX_VALUE)
								.addComponent(noStatusTextColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(noStatusBGColourButton, GroupLayout.PREFERRED_SIZE, 60,
										GroupLayout.PREFERRED_SIZE))
						.addGroup(GroupLayout.Alignment.TRAILING,
								jPanel3Layout.createSequentialGroup().addComponent(notesColourLabel)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(notesTextColourButton, GroupLayout.PREFERRED_SIZE, 60,
												GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(notesBGColourButton, GroupLayout.PREFERRED_SIZE, 60,
												GroupLayout.PREFERRED_SIZE)))
						.addContainerGap()));
		jPanel3Layout.setVerticalGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(jPanel3Layout.createSequentialGroup().addContainerGap()
						.addGroup(jPanel3Layout
								.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(overTimeColourLabel)
								.addComponent(overTimeColourTextButton).addComponent(overTimeBGColourButton))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(warningTimeColourLabel).addComponent(warningTimeTextColourButton)
								.addComponent(warningTimeBGColourButton))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(changeSubjectColourLabel).addComponent(subjectChangeTextColourButton)
								.addComponent(subjectChangeBGColourButton))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(noStatusColourLabel).addComponent(noStatusTextColourButton)
								.addComponent(noStatusBGColourButton))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(jPanel3Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
								.addComponent(notesColourLabel).addComponent(notesTextColourButton)
								.addComponent(notesBGColourButton))
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		previewPanel.setViewportView(previewTable);

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(previewPanel)
								.addGroup(layout.createSequentialGroup()
										.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
												.addComponent(coloursPanel, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(configurationsPanel, GroupLayout.DEFAULT_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(textNotificationsPanel, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
								.addGroup(GroupLayout.Alignment.TRAILING,
										layout.createSequentialGroup().addGap(0, 0, Short.MAX_VALUE)
												.addComponent(resetButton)
												.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
												.addComponent(saveButton)))
						.addContainerGap()));
		layout.setVerticalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				.addGroup(layout.createSequentialGroup().addContainerGap()
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
								.addComponent(textNotificationsPanel, GroupLayout.DEFAULT_SIZE,
										GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								.addGroup(layout.createSequentialGroup()
										.addComponent(configurationsPanel, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
										.addComponent(coloursPanel, GroupLayout.PREFERRED_SIZE,
												GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addComponent(previewPanel, GroupLayout.DEFAULT_SIZE, 142, Short.MAX_VALUE)
						.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
						.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(saveButton)
								.addComponent(resetButton))
						.addContainerGap()));

		sessionLengthTextField.setText(Config.SESSION_LENGTH + "");
		warningTimeTextField.setText(Config.WARNING_TIME + "");
		sendMessagesCheckBox.setSelected(Config.SEND_MESSAGES);
		centreNameTextField.setText(Config.CENTRE_NAME);
		centrePhoneTextField.setText(Config.CENTRE_PHONE);
		checkInTextArea.setText(Config.TEXT_IN); // right click preview
		checkOutTextArea.setText(Config.TEXT_OUT); // right click preview
		timeFormatComboBox.setSelectedIndex(Config.TIME_FORMAT.equals(Config.H12) ? 0 : 1);
		updateTimePreview();

		pack();
		setTitle("Configurations");
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().requestFocusInWindow();

		overTimeColourTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				overTimeFG = changeColour("Over Time Text", overTimeFG);
				previewTableModel.fireTableDataChanged();
			}
		});

		overTimeBGColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				overTimeBG = changeColour("Over Time Background", overTimeBG);
				previewTableModel.fireTableDataChanged();
			}
		});

		warningTimeTextColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				warningTimeFG = changeColour("Warning Time Text", warningTimeFG);
				previewTableModel.fireTableDataChanged();
			}
		});

		warningTimeBGColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				warningTimeBG = changeColour("Warning Time Background", warningTimeBG);
				previewTableModel.fireTableDataChanged();
			}
		});

		subjectChangeTextColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				subjectChangeFG = changeColour("Subject Change Text", subjectChangeFG);
				previewTableModel.fireTableDataChanged();
			}
		});

		subjectChangeBGColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				subjectChangeBG = changeColour("Subject Change Background", subjectChangeBG);
				previewTableModel.fireTableDataChanged();
			}
		});

		noStatusTextColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				noStatusFG = changeColour("No Status Text", noStatusFG);
				previewTableModel.fireTableDataChanged();
			}
		});

		noStatusBGColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				noStatusBG = changeColour("No Status Background", noStatusBG);
				previewTableModel.fireTableDataChanged();
			}
		});

		notesTextColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				notesFG = changeColour("Notes Text", notesFG);
				previewTableModel.fireTableDataChanged();
			}
		});

		notesBGColourButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				notesBG = changeColour("Notes Background", notesBG);
				previewTableModel.fireTableDataChanged();
			}
		});

		checkInTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				previewPopup(me, "Preview Check In Text", checkInTextArea);
			}
		});

		checkOutTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				previewPopup(me, "Preview Check Out Text", checkOutTextArea);
			}
		});

		timeFormatComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateTimePreview();
			}
		});

		saveButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				saveBtnMouseClicked(me);
			}
		});

		resetButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				resetBtnMouseClicked(me);
			}
		});
	}

	private void updateTimePreview() {
		timeFormat = DateTimeFormatter.ofPattern(timeFormatComboBox.getSelectedIndex() == 0 ? Config.H12 : Config.H24);
		previewLabel.setText(timeFormat.format(LocalDateTime.now()));
	}

	private void saveBtnMouseClicked(MouseEvent me) {
		boolean valid = true;

		HashMap<String, String> kvp = new HashMap<>();
		kvp.put("time_format", timeFormatComboBox.getSelectedIndex() == 0 ? Config.H12 : Config.H24);

		try {
			Integer.parseInt(sessionLengthTextField.getText());
			Integer.parseInt(warningTimeTextField.getText());
			kvp.put("session_length", sessionLengthTextField.getText());
			kvp.put("warning_time", warningTimeTextField.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(this, "Enter a numerical value (minutes) in time fields.", "Invalid Input",
					JOptionPane.WARNING_MESSAGE);
			valid = false;
			sessionLengthTextField.setText(Config.SESSION_LENGTH + "");
			warningTimeTextField.setText(Config.WARNING_TIME + "");
			System.err.println(e);
		} catch (Exception e) {
			e.printStackTrace();

		}

		kvp.put("send_messages", sendMessagesCheckBox.isSelected() ? "true" : "false");
		kvp.put("centre_name", centreNameTextField.getText());
		kvp.put("centre_phone", centrePhoneTextField.getText());
		kvp.put("text_in", checkInTextArea.getText());
		kvp.put("text_out", checkOutTextArea.getText());
		kvp.put("color_over_time_fg", "#" + Integer.toHexString(overTimeFG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_over_time_bg", "#" + Integer.toHexString(overTimeBG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_warning_time_fg", "#" + Integer.toHexString(warningTimeFG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_warning_time_bg", "#" + Integer.toHexString(warningTimeBG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_subject_change_fg",
				"#" + Integer.toHexString(subjectChangeFG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_subject_change_bg",
				"#" + Integer.toHexString(subjectChangeBG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_no_status_fg", "#" + Integer.toHexString(noStatusFG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_no_status_bg", "#" + Integer.toHexString(noStatusBG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_notes_fg", "#" + Integer.toHexString(notesFG.getRGB()).substring(2).toUpperCase());
		kvp.put("color_notes_bg", "#" + Integer.toHexString(notesBG.getRGB()).substring(2).toUpperCase());

		ConfigDAO configurations = new ConfigDAO(kvp);

		// configurations.setTimeFormat(timeFormatComboBox.getSelectedIndex() == 0 ?
		// Config.H12 : Config.H24);
		// try {
		// Integer.parseInt(sessionLengthTextField.getText());
		// Integer.parseInt(warningTimeTextField.getText());
		// configurations.setSessionLength(sessionLengthTextField.getText());
		// configurations.setWarningTime(warningTimeTextField.getText());
		// } catch (NumberFormatException e) {
		// JOptionPane.showMessageDialog(this, "Enter a numerical value (minutes) in
		// time fields.", "Invalid Input", JOptionPane.WARNING_MESSAGE);
		// valid = false;
		// sessionLengthTextField.setText(Config.SESSION_LENGTH + "");
		// warningTimeTextField.setText(Config.WARNING_TIME + "");
		// System.err.println(e);
		// } catch (Exception e) {
		// e.printStackTrace();
		//
		// }
		// configurations.setSendMessages(sendMessagesCheckBox.isSelected());
		// configurations.setCentre(centreNameTextField.getText());
		// configurations.setPhone(centrePhoneTextField.getText());
		// configurations.setTextIn(checkInTextArea.getText());
		// configurations.setTextOut(checkOutTextArea.getText());
		// configurations.setOverTimeFG(overTimeFG);
		// configurations.setOverTimeBG(overTimeBG);
		// configurations.setWarningTimeFG(warningTimeFG);
		// configurations.setWarningTimeBG(warningTimeBG);
		// configurations.setSubjectChangeFG(subjectChangeFG);
		// configurations.setSubjectChangeBG(subjectChangeBG);
		// configurations.setNoStatusFG(noStatusFG);
		// configurations.setNoStatusBG(noStatusBG);
		// configurations.setNotesFG(notesFG);
		// configurations.setNotesBG(notesBG);

		if (valid)
			configurations.update();
		JOptionPane.showMessageDialog(null,
				String.format("<html><body style='width: %1spx' align='center'>%1s", 300,
						"Configurations updated.  Restart program to apply changes."),
				"Update Succeeded", JOptionPane.PLAIN_MESSAGE);
	}

	private void resetBtnMouseClicked(MouseEvent me) {
		initialValues();
		timeFormat = DateTimeFormatter.ofPattern(Config.TIME_FORMAT);
		previewLabel = new JLabel(timeFormat.format(LocalDateTime.now()));
		sessionLengthTextField.setText(Config.SESSION_LENGTH + "");
		warningTimeTextField.setText(Config.WARNING_TIME + "");
		sendMessagesCheckBox.setSelected(Config.SEND_MESSAGES);
		centreNameTextField.setText(Config.CENTRE_NAME);
		centrePhoneTextField.setText(Config.CENTRE_PHONE);
		checkInTextArea.setText(Config.TEXT_IN); // right click preview
		checkOutTextArea.setText(Config.TEXT_OUT); // right click preview
		timeFormatComboBox.setSelectedIndex(Config.TIME_FORMAT.equals(Config.H12) ? 0 : 1);
		previewTableModel.fireTableDataChanged();
	}

	private void previewPopup(MouseEvent me, String title, JTextArea textArea) {
		if (SwingUtilities.isRightMouseButton(me)) {
			JPopupMenu previewMenu = new JPopupMenu();
			JMenuItem previewPopupWithText = new JMenuItem("Preview");
			previewPopupWithText.addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent me) {
					if (!SwingUtilities.isRightMouseButton(me)) {
						JOptionPane.showMessageDialog(null,
								String.format("<html><body style='width: %1spx' align='center'>%1s", 300,
										new TextConverter("Firstname", "Lastname").convertText(textArea.getText())),
								title, JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
			previewMenu.add(previewPopupWithText);
			previewMenu.show(textArea, me.getPoint().x, me.getPoint().y);
		}
	}

	private Color changeColour(String title, Color oldColour) {
		Color newColour = JColorChooser.showDialog(this, title, oldColour);
		return newColour != null ? newColour : oldColour;
	}
}

/**
 * Table holding signed in students
 * 
 * @author Maurine
 *
 */
class PreviewTableModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;
	private List<Student> rows;

	public PreviewTableModel(List<Student> rows) {
		this.rows = rows;
	}

	@Override
	public int getRowCount() {
		return rows == null ? 0 : rows.size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
			case 0:
				return rows.get(rowIndex).getFName();
			case 1:
				return rows.get(rowIndex).getNotes();
		}
		return null;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch (columnIndex) {
			case 0:
				return "Name";
			case 1:
				return "Notes";
		}
		return null;
	}

	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}
}

/**
 * 
 * @author Maurine
 *
 */
class PreviewTable extends ProgramTable {

	private static final long serialVersionUID = 1L;

	public PreviewTable(DefaultTableModel model) {
		super(model);
	}

	@Override
	public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
		Component comp = super.prepareRenderer(renderer, row, column);
		Color background = this.getBackground();
		Color foreground = this.getForeground();
		String studentNotes = (String) this.getModel().getValueAt(convertRowIndexToModel(row), 1); // toString()
																									// requires object
																									// to not be null
		String status = this.getModel().getValueAt(convertRowIndexToModel(row), 0).toString(); // name

		if (column == 1 && studentNotes != null && studentNotes.length() > 0) {
			background = Configurations.notesBG();
			foreground = Configurations.notesFG();
		} else {
			if (status.equals("Over Time")) {
				background = Configurations.overTimeBG();
				foreground = Configurations.overTimeFG();
			} else if (status.equals("Warning Time")) {
				background = Configurations.warningBG();
				foreground = Configurations.warningFG();
			} else if (status.equals("Subject Change")) {
				background = Configurations.subjectChangeBG();
				foreground = Configurations.subjectChangeFG();
			} else {
				background = Configurations.noStatusBG();
				foreground = Configurations.noStatusFG();
			}
		}
		comp.setBackground(background);
		comp.setForeground(foreground);

		return comp;
	}
}