package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/////////////////////////////////////////////////////////////////////////////////////////
import java.util.concurrent.Executors;
import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
/////////////////////////////////////////////////////////////////////////////////////////

import dao.IndicatorDAO;
import dao.LogDAO;
import dao.StudentDAO;
import object.*;
import util.AcuityHttpClient;
import util.Config;
import util.LeftPaneTableModel;
import util.Mailer;
import util.PDFWriter;
import util.ProgramLogger;
import util.ProgramTable;
import util.SorterFilter;
import util.TextConverter;

/**
 * 
 * @author Maurine
 *
 */
public class Checkin extends Window {
	private static final long serialVersionUID = 1L;

	ProgramLogger studentSystemLog;

	/** Number of columns in {@link CheckedInStudentsTableModel} */
	private final int COLS = 12;

	private boolean sendNotificationTexts = Config.SEND_MESSAGES;

	private JPanel mainPanel, studentListPanel, signedInStudentsPanel, searchPanel, buttonsPanel;

	/** */
	private Map<Integer, Student> allStudentsMap;

	/**
	 * ArrayList for right pane table -- purpose is to access data by index rather
	 * than key
	 */
	private List<Student> allStudentsList;

	/** Data for right pane table accessible by key */
	private HashMap<Integer, Student> checkedInStudentsMap;

	private HashMap<Integer, Log> checkInLogsMap;

	private HashMap<Integer, Indicator> indicators;

	/// MENU ///
	private JMenuBar menuBar;
	private JMenu menu, endClassSubmenu;
	private JMenuItem endClassWithMessages, endClassWithoutMessages, sendText;
	private JCheckBoxMenuItem sendMessagesMenuItem;

	/// LEFT PANE ///
	private JTextField searchTextField;
	private JButton checkinButton;
	private DefaultTableModel allStudentsTableModel;
	private JTable allStudentsTable;
	private JScrollPane allStudentsScroller;
	private JTextArea messages;

	/// RIGHT PANE ///
	/**
	 * There are additional methods outside of overridden methods used throughout
	 * this window. This is initialized
	 * as CheckedInStudentsTableModel rather than DefaultTableModel to avoid the
	 * need for casting.
	 */
	private CheckedInStudentsTableModel checkedInStudentsTableModel;
	private JTable checkedInStudentsTable;
	private JScrollPane checkedInScroller;

	private String account_sid = Config.getCredentials().get("twilio_account_sid");
	private String auth_token = Config.getCredentials().get("twilio_auth_token");
	private String from = Config.getCredentials().get("twilio_from");

	// private boolean sent = false;

	public Checkin() {
		try {
			new PDFWriter().writeNotes();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		Twilio.init(account_sid, auth_token);
		initLists();
		initComponents();

		studentSystemLog = new ProgramLogger("students");

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				endClass();
				// if (!sent) {
				// sent = true;
				if (Config.LINK_ACUITY) { // Create and email log
					if (Config.SEND_EMAIL) {
						try {
							new Mailer().sendAttachment(
									"CLASS LOG FOR " + LocalDateTime.now()
											.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")).toUpperCase(),
									"",
									new PDFWriter().writeLog());
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else { // Create log only
						try {
							new PDFWriter().writeLog();
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
			// }
		});
	}

	private void initLists() {
		allStudentsMap = new StudentDAO().readStudentIDMap();
		allStudentsList = new ArrayList<Student>();
		for (int i : allStudentsMap.keySet())
			allStudentsList.add(allStudentsMap.get(i));
		checkedInStudentsMap = new HashMap<Integer, Student>();
		checkInLogsMap = new HashMap<Integer, Log>();
		indicators = new IndicatorDAO().read();
	}

	private JMenuBar createMenuBar() {
		menuBar = new JMenuBar();

		menu = new JMenu("Menu");
		menu.setMnemonic(KeyEvent.VK_M);
		menuBar.add(menu);

		sendMessagesMenuItem = new JCheckBoxMenuItem("Text notification", Config.SEND_MESSAGES);
		sendMessagesMenuItem.setMnemonic(KeyEvent.VK_T);
		sendMessagesMenuItem.addItemListener(e -> {
			int state = e.getStateChange();
			if (state == ItemEvent.SELECTED) {
				messages.append("Text notifications are on\n");
				sendNotificationTexts = true;
			} else {
				messages.append("Text notifications are off\n");
				sendNotificationTexts = false;
			}
		});
		menu.add(sendMessagesMenuItem);

		menu.addSeparator();

		sendText = new JMenuItem("Send text", KeyEvent.VK_S);
		sendText.addActionListener(e -> {
			java.awt.EventQueue.invokeLater(() -> {
				new SendText();
			});
		});
		menu.add(sendText);

		menu.addSeparator();

		endClassSubmenu = new JMenu("End class");
		endClassSubmenu.setMnemonic(KeyEvent.VK_E);
		endClassWithMessages = new JMenuItem("With text notification", KeyEvent.VK_W);
		endClassWithoutMessages = new JMenuItem("Without text notification", KeyEvent.VK_N);
		endClassWithMessages.addActionListener(e -> {
			sendNotificationTexts = true;
			endClass();
		});
		endClassWithoutMessages.addActionListener(e -> {
			sendNotificationTexts = false;
			endClass();
		});

		menu.add(endClassSubmenu);
		endClassSubmenu.add(endClassWithMessages);
		endClassSubmenu.add(endClassWithoutMessages);

		return menuBar;
	}

	protected void initComponents() {
		int paneHeight = 670;
		int leftWidth = 250;
		int rightWidth = 900;
		int cutoffColumn = 10;

		JPanel spacer = new JPanel();
		messages = new JTextArea(10, 20);
		messages.setEditable(false);
		messages.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
		JScrollPane scrollPane = new JScrollPane(messages);

		messages.append(Config.CENTRE_NAME.toUpperCase() + " - " + Config.CENTRE_PHONE +
				"\nSession length is " + Config.SESSION_LENGTH +
				"\nWarning time is " + Config.WARNING_TIME +
				"\nText notifications are " + (sendNotificationTexts ? "on" : "off") + "\n");

		checkedInStudentsMap = new HashMap<Integer, Student>();

		searchTextField = new JTextField("Search...");
		searchTextField.setPreferredSize(new Dimension(leftWidth, 32));

		checkinButton = new JButton();
		checkinButton.setPreferredSize(new Dimension(20, 20));
		try {
			Image rightArrow = ImageIO.read(getClass().getResource("RIGHT_ARROW.png"));
			checkinButton.setIcon(new ImageIcon(rightArrow));
		} catch (Exception e) {
			e.printStackTrace();
		}

		mainPanel = new JPanel();
		searchPanel = new JPanel();
		studentListPanel = new JPanel();
		signedInStudentsPanel = new JPanel();
		buttonsPanel = new JPanel();

		allStudentsTableModel = new LeftPaneTableModel(allStudentsMap);
		allStudentsTable = new ProgramTable(allStudentsTableModel) {
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

		TableRowSorter<TableModel> allStudentsTableRowSorter = new TableRowSorter<TableModel>(allStudentsTableModel);
		allStudentsTableRowSorter.setModel(allStudentsTableModel);
		allStudentsTable.setRowSorter(allStudentsTableRowSorter);
		allStudentsTable.getRowSorter().toggleSortOrder(0);
		allStudentsTable.setShowHorizontalLines(false);
		allStudentsTable.removeColumn(allStudentsTable.getColumnModel().getColumn(2));

		allStudentsScroller = new JScrollPane(allStudentsTable);
		allStudentsScroller.setPreferredSize(new Dimension(leftWidth, paneHeight));

		checkedInStudentsTableModel = new CheckedInStudentsTableModel(checkedInStudentsMap);
		checkedInStudentsTable = new ProgramTable(checkedInStudentsTableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
				Component comp = super.prepareRenderer(renderer, row, column);
				Color background = this.getBackground();
				Color foreground = this.getForeground();
				int notesCol = 9;
				int statusIdCol = 10;
				int colorCol = 12;

				// toString() requires object to be null
				String studentNotes = (String) this.getModel().getValueAt(convertRowIndexToModel(row), notesCol);
				Color indicatorColor = Color
						.decode((String) this.getModel().getValueAt(convertRowIndexToModel(row), colorCol));

				if (!isCellSelected(row, column)) {
					if (column == notesCol && studentNotes != null && studentNotes.length() > 0) {
						background = Config.NOTES_BG;
						foreground = Config.NOTES_FG;
					} else {
						// status id
						int statusID = (int) this.getModel().getValueAt(convertRowIndexToModel(row), statusIdCol);
						switch (statusID) {
							case Student.OVER_TIME:
								background = Config.OVER_TIME_BG;
								foreground = column == 2 ? indicatorColor : Config.OVER_TIME_FG;
								break;
							case Student.WARNING:
								background = Config.WARNING_TIME_BG;
								foreground = column == 2 ? indicatorColor : Config.WARNING_TIME_FG;
								break;
							case Student.SUBJECT_CHANGE:
								background = Config.SUBJECT_CHANGE_BG;
								foreground = column == 2 ? indicatorColor : Config.SUBJECT_CHANGE_FG;
								break;
							default:
								background = Config.NO_STATUS_BG;
								foreground = column == 2 ? indicatorColor : Config.NO_STATUS_FG;
						}
					}
					if (column == 0 || column == 1 || column == 2 || column == 3) {
						background = Color.WHITE;
					}
					comp.setBackground(background);
					comp.setForeground(foreground);
				}
				return comp;
			}

			@Override
			public Class<?> getColumnClass(int column) {
				return column == 0 || column == 3 ? ImageIcon.class : super.getColumnClass(column);
			}
		};

		// Sorts checkedInStudentsTable by Priority
		TableRowSorter<TableModel> checkedInTableSorter = new TableRowSorter<TableModel>(checkedInStudentsTableModel) {
			@Override
			public boolean isSortable(int column) {
				return (column == COLS - 1);
			}
		};
		checkedInTableSorter.setModel(checkedInStudentsTableModel);
		checkedInStudentsTable.setRowSorter(checkedInTableSorter);
		checkedInStudentsTable.getRowSorter().toggleSortOrder(COLS - 1);
		checkedInStudentsTable.getColumnModel().getColumn(0).setMaxWidth(30);
		checkedInStudentsTable.getColumnModel().getColumn(1).setMaxWidth(30);
		checkedInStudentsTable.getColumnModel().getColumn(2).setMaxWidth(30);
		checkedInStudentsTable.getColumnModel().getColumn(3).setMaxWidth(30);
		checkedInStudentsTable.getColumnModel().getColumn(4).setMinWidth(225);
		checkedInStudentsTable.getColumnModel().getColumn(4).setMaxWidth(225);
		checkedInStudentsTable.getColumnModel().getColumn(5).setMinWidth(100);
		checkedInStudentsTable.getColumnModel().getColumn(5).setMaxWidth(100);
		checkedInStudentsTable.getColumnModel().getColumn(6).setMaxWidth(50);
		checkedInStudentsTable.getColumnModel().getColumn(7).setMaxWidth(50);
		checkedInStudentsTable.getColumnModel().getColumn(8).setMaxWidth(50);

		for (int i = cutoffColumn; i < COLS; i++) // hide everything from cutoffColumn to the right
			checkedInStudentsTable.removeColumn(checkedInStudentsTable.getColumnModel().getColumn(cutoffColumn));

		checkedInScroller = new JScrollPane(checkedInStudentsTable);
		checkedInScroller.setPreferredSize(new Dimension(rightWidth, paneHeight - 5));

		searchPanel.add(searchTextField);
		studentListPanel.setLayout(new BoxLayout(studentListPanel, BoxLayout.Y_AXIS));
		studentListPanel.setPreferredSize(new Dimension(leftWidth, paneHeight));
		studentListPanel.add(searchPanel);
		studentListPanel.add(allStudentsScroller);
		studentListPanel.add(spacer);
		studentListPanel.add(scrollPane);

		buttonsPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		buttonsPanel.setPreferredSize(new Dimension(30, paneHeight));
		buttonsPanel.add(checkinButton, gbc);

		signedInStudentsPanel.setLayout(new BoxLayout(signedInStudentsPanel, BoxLayout.Y_AXIS));
		signedInStudentsPanel.setPreferredSize(new Dimension(rightWidth, paneHeight));
		signedInStudentsPanel.add(checkedInScroller);

		mainPanel.setBorder(Config.BORDER);
		mainPanel.add(studentListPanel);
		mainPanel.add(buttonsPanel);
		mainPanel.add(signedInStudentsPanel);

		add(mainPanel);
		setTitle("Class Session");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setSize(new Dimension(1350, 755));
		setVisible(true);
		setJMenuBar(createMenuBar());
		setLocationRelativeTo(null);
		getContentPane().requestFocusInWindow();

		// Refreshes checkedInStudentsTable
		Timer timer = new Timer(0, (ae) -> checkedInStudentsTableModel.fireTableDataChanged());
		timer.setDelay(5000); // refresh every 5 seconds
		timer.start();

		searchTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				searchTextField.setText("");
			}
		});

		searchTextField.getDocument().addDocumentListener(
				new SorterFilter(searchTextField, allStudentsTableRowSorter));

		checkinButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				checkinBtnMouseClicked(me);
			}
		});

		/*
		 * mousePressed() occurs when the user presses the mouse button.
		 * mouseReleased() occurs when the user releases the mouse button.
		 * mouseClicked() occurs when the user presses and releases the mouse button
		 * A user normally clicks the mouse button when selecting or double clicking an
		 * icon
		 */

		checkedInStudentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me)) {
					int currentRow = checkedInStudentsTable.rowAtPoint(me.getPoint());
					checkedInStudentsTable.setRowSelectionInterval(currentRow, currentRow);
					JPopupMenu checkOutMenu = new JPopupMenu();
					JMenuItem checkOutPopupWithText = new JMenuItem("Check out with text");
					checkOutPopupWithText.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent me) {
							if (!SwingUtilities.isRightMouseButton(me)
									&& checkedInStudentsTable.getSelectedRow() >= 0) {
								boolean originalSetting = sendNotificationTexts;
								sendNotificationTexts = true;
								checkOut();
								sendNotificationTexts = originalSetting;
							}
						}
					});
					JMenuItem checkOutPopupWithoutText = new JMenuItem("Check out without text");
					checkOutPopupWithoutText.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent me) {
							if (!SwingUtilities.isRightMouseButton(me)
									&& checkedInStudentsTable.getSelectedRow() >= 0) {
								boolean originalSetting = sendNotificationTexts;
								sendNotificationTexts = false;
								checkOut();
								sendNotificationTexts = originalSetting;
							}
						}
					});
					checkOutMenu.add(checkOutPopupWithoutText);
					checkOutMenu.add(checkOutPopupWithText);
					checkOutMenu.show(checkedInStudentsTable, me.getPoint().x, me.getPoint().y);
				} else if (me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1) {
					// ONLY ALLOW DOUBLE CLICK IF STUDENT IS IN WARNING
					int rowIndex = checkedInStudentsTable
							.convertRowIndexToModel(checkedInStudentsTable.getSelectedRow());
					if (rowIndex >= 0
							&& (int) checkedInStudentsTableModel.getValueAt(rowIndex, 8) <= Config.WARNING_TIME)
						checkOut();
					else
						messages.append(
								"Right click to check out when time \nleft is more than " + Config.WARNING_TIME + "\n");
				}
			}

		});

		allStudentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (SwingUtilities.isRightMouseButton(me)) {
					int currentRow = allStudentsTable.rowAtPoint(me.getPoint());
					allStudentsTable.setRowSelectionInterval(currentRow, currentRow);
					JPopupMenu checkOutMenu = new JPopupMenu();
					JMenuItem checkOutPopup = new JMenuItem("Check In");

					checkOutPopup.addMouseListener(new MouseAdapter() {
						@Override
						public void mousePressed(MouseEvent me) {
							if (!SwingUtilities.isRightMouseButton(me)) {
								checkIn();
							}
						}
					});
					checkOutMenu.add(checkOutPopup);
					checkOutMenu.show(allStudentsTable, me.getPoint().x, me.getPoint().y);
				} else if (me.getClickCount() == 2 && me.getButton() == MouseEvent.BUTTON1) {
					checkIn();
				}
			}
		});

	}

	private void endClass() {
		checkedInStudentsTable.selectAll();
		if (checkedInStudentsTable.getSelectedRowCount() != 0) {
			checkOut();
		} else {
			messages.append("No more students to check out.\n");
		}
	}

	private void checkinBtnMouseClicked(MouseEvent me) {
		if (allStudentsTable.getSelectedRowCount() != 0)
			checkIn();
		else
			JOptionPane.showMessageDialog(this, "Choose a student to sign in.");
	}

	private void checkIn() {
		ArrayList<Integer> effectiveRows = new ArrayList<>();
		int keyToCheck;

		for (int i : allStudentsTable.getSelectedRows())
			effectiveRows.add(allStudentsTable.convertRowIndexToModel(i));

		LogDAO saveLog = new LogDAO();
		for (int i : effectiveRows) {
			keyToCheck = allStudentsList.get(i).getID();

			if (!checkedInStudentsMap.keySet().contains(keyToCheck)) {
				// creates new Student in checked-in students pane with time information
				checkedInStudentsMap.put(keyToCheck, new Student(allStudentsMap.get(keyToCheck)));

				if (Config.LINK_ACUITY)
					checkedInStudentsMap.get(keyToCheck).checkAppointment();

				allStudentsMap.get(keyToCheck).signIn(); // changes text in sign-in pane to gray

				int studentID = checkedInStudentsMap.get(keyToCheck).getID();
				checkInLogsMap.put(studentID, new Log(allStudentsMap.get(studentID)));
				saveLog.checkIn(checkInLogsMap.get(studentID)); // create check in log on DB

				if (allStudentsMap.get(keyToCheck).isBirthdayToday())
					messages.append("Happy birthday, " + allStudentsMap.get(keyToCheck).getFName() + "!\n");
				// else if (allStudentsMap.get(keyToCheck).birthdayAlert())
				// messages.append(allStudentsMap.get(keyToCheck).getFName() + "'s birthday is
				// on " + allStudentsMap.get(keyToCheck).getBirthday().substring(0, 5) +".\n");

				if (sendNotificationTexts)
					Executors.newSingleThreadExecutor().execute(new SendMessage(keyToCheck, Config.TEXT_IN));

				if (Config.LINK_ACUITY)
					Executors.newSingleThreadExecutor().execute(new AppointmentAlert(checkInLogsMap.get(studentID)));
			}
		}

		allStudentsTableModel.fireTableDataChanged();
		checkedInStudentsTableModel.fireTableDataChanged();
	}

	private void checkOut() {
		int keyToCheck; // key from Checked In Students to check in All Students

		LogDAO saveLog = new LogDAO();
		for (int i : checkedInStudentsTable.getSelectedRows()) {
			keyToCheck = checkedInStudentsTable.getSelectedRowCount() != 1
					? checkedInStudentsTableModel.convertIndexToKey(checkedInStudentsTable.convertRowIndexToModel(0))
					: checkedInStudentsTableModel.convertIndexToKey(checkedInStudentsTable.convertRowIndexToModel(i));

			checkInLogsMap.get(keyToCheck).signOut();
			saveLog.checkOut(checkInLogsMap.get(keyToCheck)); // update check out log on DB
			messages.append("Checked out " + allStudentsMap.get(keyToCheck).getName() + "\n");

			if (sendNotificationTexts)
				Executors.newSingleThreadExecutor().execute(new SendMessage(keyToCheck, Config.TEXT_OUT));

			allStudentsMap.get(keyToCheck).signOut();
			checkedInStudentsMap.remove(keyToCheck);
			allStudentsTableModel.fireTableDataChanged();
			checkedInStudentsTableModel.fireTableDataChanged();
		}
	}

	class SendMessage implements Runnable {
		private int keyToCheck;
		private String message;

		public SendMessage(int keyToCheck, String message) {
			this.keyToCheck = keyToCheck;
			this.message = message;
		}

		@Override
		public void run() {
			try {
				TextConverter textProcessor = new TextConverter(allStudentsMap.get(keyToCheck).getFName(),
						allStudentsMap.get(keyToCheck).getLName());
				for (String phone : allStudentsMap.get(this.keyToCheck).getContactPhones()) {
					if (phone.length() > 9)
						Message.creator(new PhoneNumber(phone), new PhoneNumber(from),
								textProcessor.convertText(message)).create();
				}
			} catch (ApiException e) {
				System.err.println(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class AppointmentAlert implements Runnable {
		/** Log containing student name and check in time */
		private Log log;

		public AppointmentAlert(Log log) {
			this.log = log;
		}

		@Override
		public void run() {
			boolean onTime = false;

			try {
				Appointment appointment = AcuityHttpClient.getAppointment(
						log.getSignIn(),
						log.getStudent().getFName(),
						log.getStudent().getLName());

				if (appointment != null) {
					log.setAppointment(appointment);
					onTime = log.checkAppointment();
				}

				LocalTime logTime = log.getSignIn().toLocalTime();

				if (appointment != null) {
					studentSystemLog.getLogger().info(
							log.getStudent().getName() + " - Signed in: " + logTime.toString().substring(0, 5)
									+ " - Appointment: "
									+ log.getAppointmentTime().toLocalTime().toString().substring(0, 5));
				} else {
					studentSystemLog.getLogger().info(
							log.getStudent().getName() + " - Signed in: " + logTime.toString().substring(0, 5)
									+ " - No appointment scheduled");
				}

				// GENERATE EMAIL
				if (!onTime && Config.SEND_EMAIL) {
					String date = log.getSignIn().getMonth() + " " + log.getSignIn().getDayOfMonth() + ", "
							+ log.getSignIn().getYear();
					String studentName = log.getStudent().getName();
					String appointmentTimeStr = appointment != null ? appointment.getTime() : "Not scheduled";
					String signInTime = Log.formatTime(log.getSignIn());
					String email = appointment != null && appointment.getEmail() != null
							&& !appointment.getEmail().trim().isEmpty()
									? appointment.getEmail()
									: "Not available";
					String phone = appointment != null && appointment.getPhone() != null
							&& !appointment.getPhone().trim().isEmpty()
									? appointment.getPhone()
									: "Not available";

					new Mailer().sendMail(studentName.toUpperCase() + " - NOTICE",
							date + "\n" +
									studentName + " has signed in outside their scheduled appointment."
									+ "\nAppointment time: " + appointmentTimeStr
									+ "\nSign in time: " + signInTime
									+ "\nEmail: " + email
									+ "\nPhone: " + phone);
				}

				// THIS CODE REQUIRES kumon_db.acuity_log FOR NEW FIELDS
				new LogDAO().saveAppt(log);

			} catch (IOException | InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * This model is used for the table holding signed in students.
	 * 
	 * @author Maurine
	 *
	 */
	class CheckedInStudentsTableModel extends DefaultTableModel {

		private static final long serialVersionUID = 1L;
		private HashMap<Integer, Student> studentMap;

		private ImageIcon birthday, birthday2, birthday3, warning;

		public CheckedInStudentsTableModel(HashMap<Integer, Student> studentMap) {
			this.birthday = new ImageIcon(getClass().getResource("GIFT.png"));
			this.birthday2 = new ImageIcon(getClass().getResource("GIFT2.png"));
			this.birthday3 = new ImageIcon(getClass().getResource("GIFT3.png"));
			this.warning = new ImageIcon(getClass().getResource("WARNING.gif"));
			this.studentMap = studentMap;
		}

		@Override
		public int getRowCount() {
			return studentMap == null ? 0 : studentMap.size();
		}

		@Override
		public int getColumnCount() {
			return COLS;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			Student student = convertMapToList().get(rowIndex);
			switch (columnIndex) {
				case 0:
					if (student.isBirthdayToday())
						return birthday2;
					else if (student.daysUntilBirthday() <= 3 && student.daysUntilBirthday() > 0)
						return birthday;
					else if (student.daysUntilBirthday() >= -3 && student.daysUntilBirthday() < 0)
						return birthday3;
					else
						return null;
				case 1:
					return student.getTag().toUpperCase();
				case 2:
					return student.getIndicatorID() != 401 ? "●" : "";
				case 3:
					return !student.isOnTime() ? warning : null;
				case 4:
					return student.getName();
				case 5:
					return student.getSubject().replace("ing", "");
				case 6:
					return student.displayTimeIn();
				case 7:
					return student.displayTimeOut();
				case 8:
					return student.timeRemaining();
				case 9:
					return student.getNotes();
				case 10:
					return student.statusID();
				case 11:
					return student.priority();
				case 12:
					return indicators.get(student.getIndicatorID()).getColor();
			}
			return null;
		}

		@Override
		public String getColumnName(int columnIndex) {
			switch (columnIndex) {
				case 0:
					return "";
				case 1:
					return "";
				case 2:
					return "";
				case 3:
					return "";
				case 4:
					return "Name";
				case 5:
					return "Subject";
				case 6:
					return "In";
				case 7:
					return "Out";
				case 8:
					return "Left";
				case 9:
					return "Notes";
				case 10:
					return "Status ID";
				case 11:
					return "Prioirty";
				case 12:
					return "Colour";
			}
			return null;
		}

		@Override
		public boolean isCellEditable(int row, int column) {
			return false;
		}

		private ArrayList<Student> convertMapToList() {
			ArrayList<Student> convertedStudentList = new ArrayList<>();
			for (int i : studentMap.keySet())
				convertedStudentList.add(studentMap.get(i));
			return convertedStudentList;
		}

		public int convertIndexToKey(int i) {
			return convertMapToList().get(i).getID();
		}
	}

}