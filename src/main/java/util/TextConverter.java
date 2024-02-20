package util;

import java.time.LocalDateTime;
import java.util.HashMap;

public class TextConverter {

	private HashMap<String, String> codes;
	
	public TextConverter(String firstName, String lastName) {
		codes = new HashMap<>();
		codes.put("#Centre_Name#", Config.CENTRE_NAME);
		codes.put("#CENTRE_NAME#", Config.CENTRE_NAME.toUpperCase());
		codes.put("#CENTRE_PHONE#", Config.CENTRE_PHONE);
		codes.put("#Student_First#", firstName);
		codes.put("#STUDENT_FIRST#", firstName.toUpperCase());
		codes.put("#Student_Last#", lastName);
		codes.put("#STUDENT_LAST#", lastName.toUpperCase());
		codes.put("#MONTH#", LocalDateTime.now().getMonth().toString());
	} 
	
	public String convertText(String text) {
		String convertedText = text;
		for (String code : codes.keySet())
			convertedText = convertedText.replaceAll(code, codes.get(code));
		return convertedText;
	}
	
}
