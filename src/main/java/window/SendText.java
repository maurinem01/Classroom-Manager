package window;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LayoutStyle;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.twilio.Twilio;
import com.twilio.exception.ApiException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import dao.StudentDAO;
import object.Person;
import object.Student;
import util.Config;
import util.LeftPaneTableModel;
import util.ProgramTable;
import util.ProgramStudentsListModel;
import util.SorterFilter;
import util.StudentTableList;
import util.TextConverter;

/**
 *
 * @author Maurine
 */
public class SendText extends Window {

	private static final long serialVersionUID = 1L;

	private JList<Student> recipientsList;
    private JTable studentsTable;
    private JTextArea messageTextArea;
    private JScrollPane messageScroller;
    private JScrollPane recipientsScroller;
    private JTextField searchTextField;
    private JButton sendButton;
    private JScrollPane studentsScroller;
    
    private DefaultTableModel studentsTableModel;
    
    private Map<Integer, Student> allStudentsMap = new HashMap<>();
    private List<Student> allStudentsList = new ArrayList<>();
    private ProgramStudentsListModel recipientStudentsListModel;
    
	private String account_sid = Config.getCredentials().get("twilio_account_sid");
	private String auth_token = Config.getCredentials().get("twilio_auth_token");
	private String from = Config.getCredentials().get("twilio_from");
    
	public SendText() {
		Twilio.init(account_sid, auth_token);		
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
    	System.out.println("SendMessage.initComponents()");
    	
        studentsScroller = new JScrollPane();
        studentsTable = new JTable();
        searchTextField = new JTextField();
        
        recipientsScroller = new JScrollPane();
        recipientsList = new JList<>();
        
        messageScroller = new JScrollPane();
        messageTextArea = new JTextArea(5, 20);
        messageTextArea.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5), null));
        messageTextArea.setLineWrap(true);
        messageTextArea.setWrapStyleWord(true);
        messageScroller.setViewportView(messageTextArea);
        
        sendButton = new JButton("Send");

        recipientStudentsListModel = new ProgramStudentsListModel();
        recipientsList.setModel(recipientStudentsListModel);
        recipientsList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        recipientsList.setCellRenderer(new DefaultListCellRenderer() {
			private static final long serialVersionUID = 1L;

			@Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        		setText(value instanceof Person ? ((Person) value).getName() : null);
        		setBackground(Color.WHITE); 
        		return this;
        	}
        });
        recipientsScroller.setViewportView(recipientsList);
        
        studentsScroller.setViewportView(studentsTable);
        searchTextField.setText("Search...");

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(studentsScroller, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(searchTextField, GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE))
                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addGroup(GroupLayout.Alignment.TRAILING, layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                        .addComponent(messageScroller, GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE)
                        .addComponent(recipientsScroller))
                    .addComponent(sendButton, GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(searchTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(studentsScroller, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(recipientsScroller, GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(messageScroller, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sendButton)))
                .addContainerGap())
        );
        
        // copy pasted stuff
        studentsTableModel = new LeftPaneTableModel(allStudentsMap);
        studentsTable = new ProgramTable(studentsTableModel) {
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
        studentsScroller.setViewportView(studentsTable);
        
        TableRowSorter<TableModel> studentsTableRowSorter = new TableRowSorter<TableModel>(studentsTableModel);
		studentsTableRowSorter.setModel(studentsTableModel);
		studentsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		studentsTable.setRowSorter(studentsTableRowSorter);
		studentsTable.getRowSorter().toggleSortOrder(0);
		studentsTable.setShowHorizontalLines(false);
		studentsTable.removeColumn(studentsTable.getColumnModel().getColumn(2));
        
        pack();
        setVisible(true);
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Send Message");
        getContentPane().requestFocusInWindow();
        
        StudentTableList sl = new StudentTableList();
        
        searchTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) { searchTextField.setText(""); }
		});
        
        searchTextField.getDocument().addDocumentListener(
        		new SorterFilter(searchTextField, studentsTableRowSorter));
        
        studentsTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) { 
				sl.addStudent(me, allStudentsList, studentsTable, recipientStudentsListModel, studentsTableModel);
			}
		}); 
        
        sendButton.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mousePressed(MouseEvent me) {
        		sendBtnMouseClicked(me);
        	}
        });
        
        recipientsList.addMouseListener(new MouseAdapter() {
        	@Override
			public void mouseClicked(MouseEvent me) {
        		sl.listListener(me, allStudentsMap, recipientsList, recipientStudentsListModel, studentsTableModel);
        	}
        });
        
        messageTextArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) { previewPopup(me, "Preview Text", messageTextArea); }
		});
    }
	
	private void sendBtnMouseClicked(MouseEvent me) {
		HashMap<Integer, Student> students = recipientStudentsListModel.getStudents();
		for (int i : students.keySet()) {
			Executors.newSingleThreadExecutor().execute(new SendTextMessage(students.get(i), messageTextArea.getText()));
		}
		messageTextArea.setText("");
	}
    
    private void previewPopup(MouseEvent me, String title, JTextArea textArea) {
		if (SwingUtilities.isRightMouseButton(me)) {
			JPopupMenu previewMenu = new JPopupMenu();
			JMenuItem previewPopupWithText = new JMenuItem("Preview");
			previewPopupWithText.addMouseListener(new MouseAdapter( ) {
				@Override
				public void mousePressed(MouseEvent me) {
					if (!SwingUtilities.isRightMouseButton(me)) {
						JOptionPane.showMessageDialog(null, String.format("<html><body style='width: %1spx' align='center'>%1s", 300, new TextConverter("Firstname", "Lastname").convertText(textArea.getText())), title, JOptionPane.PLAIN_MESSAGE);
					}
				}
			});
			previewMenu.add(previewPopupWithText);
			previewMenu.show(textArea, me.getPoint().x, me.getPoint().y);				
		}
	}

 	// This is only useful to SendText
	class SendTextMessage implements Runnable {
		private Student student;
		private String message;
		public SendTextMessage(Student student, String message) {
			this.student = student;
			this.message = message;
		}
		@Override
		public void run() {
			try {
				System.out.println("SENT MESSAGE TO " + student.getName());
				TextConverter textProcessor = new TextConverter(student.getFName(), student.getLName());
				for (String phone : student.getContactPhones()) {
					if (phone.length() > 9)
						Message.creator(new PhoneNumber(phone), new PhoneNumber(from), textProcessor.convertText(message)).create();
				}
			} catch (ApiException e) {
				System.err.println(e);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
