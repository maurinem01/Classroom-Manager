package util;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class ProgramLogger {
	private String fileDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hhmma-")).toUpperCase().replace(".", "");

	private String filename;
	private FileHandler handler;
	private Logger logger;	
	
	public ProgramLogger(String type) {
		filename = "system\\" + type + "\\" + fileDate + type + ".log";
		
		try {
			handler = new FileHandler(filename, true);
		} catch (SecurityException | IOException e) {
			e.printStackTrace();
		}
		
		logger = Logger.getLogger("util");
		logger.addHandler(handler);
	}
	
	public Logger getLogger() {
		return logger;
	}
}
