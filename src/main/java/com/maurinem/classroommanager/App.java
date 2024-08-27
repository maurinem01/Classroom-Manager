package com.maurinem.classroommanager;

import java.awt.EventQueue;
import java.awt.Font;
import java.util.Enumeration;

import javax.swing.UIManager;
import javax.swing.plaf.FontUIResource;

import com.maurinem.classroommanager.util.Config;
import com.maurinem.classroommanager.window.AppLaunch;

/**
 * This class loads the program.
 * 
 * @author Maurine
 */
public class App {

	public static void main(String[] args) {

		// This block of code changes all fonts in windows to be Config.FONT
		Enumeration<Object> keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof FontUIResource) {
				Font font = Config.FONT;
				UIManager.put(key, new FontUIResource(font));
			}
		}

		EventQueue.invokeLater(() -> new AppLaunch());
	}
}
