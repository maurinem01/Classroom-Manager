package window;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;

import util.Config;
import util.Mailer;

//import util.PDFWriter;

/**
 * This is the program's main screen.
 * 
 * @author Maurine
 */

public class AppLaunch extends Window {

	private static final long serialVersionUID = 1L;

	private JPanel mainPanel;
	private JButton configurationsButton, editStudentsButton, sendTextButton, startClassButton;
	private BufferedImage img;

	public AppLaunch() {
		try {
			img = ImageIO.read(getClass().getResource("MAIN.png"));
		} catch (IOException e) {
			System.err.println(e);
		}

		setContentPane(new JPanel() {
			private static final long serialVersionUID = 1L;

			@Override
			public void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(img, 0, 0, null);
			}
		});

		if (Config.SEND_EMAIL && Config.LINK_ACUITY) {
			File myObj = new File("tokens\\StoredCredential");
			if (myObj.delete()) {
				System.out.println("Deleted the file: " + myObj.getName());
			} else {
				System.out.println("Failed to delete the file.");
			}

			String timestamp = DateTimeFormatter.ofPattern("MMM dd yyyy hh:mma").format(LocalDateTime.now())
					.replace(".", "");
			try {
				new Mailer().sendMail("CLASSROOM MANAGER SESSION START",
						Config.CENTRE_NAME + " re-authenticated on " + timestamp);
			} catch (BindException be) {
				JOptionPane.showMessageDialog(null,
						"Application already running.  First instance must be authenticated.", "Error",
						JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		initComponents();
		Dimension windowSize = new Dimension(800, 700);
		setTitle("Classroom Manager");
		setVisible(true);
		setPreferredSize(new Dimension(windowSize));
		setMaximumSize(new Dimension(windowSize));
		setMinimumSize(new Dimension(windowSize));
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().requestFocusInWindow();
	}

	protected void initComponents() {
		mainPanel = new JPanel();
		mainPanel.setOpaque(false);
		// helpButton = new MainScreenButton("Help");
		configurationsButton = new MainScreenButton("Configurations");
		editStudentsButton = new MainScreenButton("Edit Students");
		startClassButton = new MainScreenButton("Start Class");
		sendTextButton = new MainScreenButton("Send Texts");
		// logButton = new MainScreenButton("Today's Log");

		GroupLayout mainPanelLayout = new GroupLayout(mainPanel);
		mainPanel.setLayout(mainPanelLayout);
		mainPanelLayout.setHorizontalGroup(
				mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(mainPanelLayout.createSequentialGroup()
								.addContainerGap()
								.addGroup(mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
										// .addComponent(helpButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
										// Short.MAX_VALUE)
										.addComponent(sendTextButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(configurationsButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(editStudentsButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
										.addComponent(startClassButton, GroupLayout.DEFAULT_SIZE,
												GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
								// .addComponent(logButton, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE,
								// Short.MAX_VALUE)
								)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		mainPanelLayout.setVerticalGroup(
				mainPanelLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
								.addContainerGap()
								// .addComponent(helpButton)
								// .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(configurationsButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(editStudentsButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(sendTextButton)
								.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								.addComponent(startClassButton)
								// .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
								// .addComponent(logButton)
								.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(layout.createSequentialGroup()
								.addContainerGap(577, Short.MAX_VALUE)
								.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		layout.setVerticalGroup(
				layout.createParallelGroup(GroupLayout.Alignment.LEADING)
						.addGroup(GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
								.addContainerGap(296, Short.MAX_VALUE)
								.addComponent(mainPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE,
										GroupLayout.PREFERRED_SIZE)
								.addContainerGap()));
		pack();

		configurationsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				EventQueue.invokeLater(() -> {
					new Configurations();
				});
			}
		});

		editStudentsButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				EventQueue.invokeLater(() -> {
					new EditStudents();
				});
			}
		});

		startClassButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				EventQueue.invokeLater(() -> {
					new Checkin();
				});
			}
		});

		sendTextButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent me) {
				EventQueue.invokeLater(() -> {
					new SendText();
				});
			}
		});

		// logButton.addMouseListener(new MouseAdapter() {
		// @Override
		// public void mousePressed(MouseEvent me) {
		//// java.awt.EventQueue.invokeLater(() -> {
		//// new LogFilter();
		//// });
		// try {
		// new PDFWriter().writeLog();
		// } catch (FileNotFoundException e) {
		// e.printStackTrace();
		// }
		//
		// }
		// });
	}

	class MainScreenButton extends JButton {
		private static final long serialVersionUID = 1L;

		public MainScreenButton(String text) {
			super(text);
			setBackground(Color.decode("#352D2E"));
			setForeground(Color.WHITE);
			setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
			setFont(new Font("Sans Serif", Font.BOLD, 18));
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent me) {
					setBackground(Color.decode("#B6A299"));
					setForeground(Color.decode("#352D2E"));
				}

				@Override
				public void mouseExited(MouseEvent me) {
					setBackground(Color.decode("#352D2E"));
					setForeground(Color.WHITE);
				}
			});
		}
	}
}