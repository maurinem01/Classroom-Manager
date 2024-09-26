package com.maurinem.classroommanager.window;

import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class Window extends JFrame {

	/**	 */
	private static final long serialVersionUID = 1L;

	protected final static String BASE = "";
	protected List<Image> icons = new ArrayList<>();

	public Window() {
		icons.add(new ImageIcon(Window.class.getResource(BASE + "ICON16.png")).getImage());
		icons.add(new ImageIcon(Window.class.getResource(BASE + "ICON32.png")).getImage());
		icons.add(new ImageIcon(Window.class.getResource(BASE + "ICON64.png")).getImage());
		icons.add(new ImageIcon(Window.class.getResource(BASE + "ICON128.png")).getImage());
		setIconImages(icons);
	}

	protected abstract void initComponents();

}
