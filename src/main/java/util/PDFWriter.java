package util;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Table;

import dao.LogDAO;
import dao.StudentDAO;
import object.Log;
import object.Student;

public class PDFWriter {

	private String fileDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-hhmma-")).toUpperCase().replace(".", "");
	private DateTimeFormatter format = DateTimeFormatter.ofPattern("h:mma");
	
	public void writeNotes() throws FileNotFoundException {
		HashMap<Integer, Student> students = new StudentDAO().readStudentIDMap();
		
		String dest = "system\\pdf\\notes\\" + fileDate + "NOTES.pdf"; 
		PdfWriter writer = new PdfWriter(dest);
		PdfDocument pdfDocument = new PdfDocument(writer);		
		Document document = new Document(pdfDocument); 
		
		pdfDocument.setDefaultPageSize(PageSize.LETTER);
		document.add(new Paragraph("NOTES FOR " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MMM dd h:mma")).replace(".", "").toUpperCase()));
		List<Student> studentsWithNotes = new ArrayList<>();
		for (int i : students.keySet()) {
			if (students.get(i).getNotes().length() > 0) {
				studentsWithNotes.add(students.get(i));
			}
		}
		
		Collections.sort(studentsWithNotes, new Comparator<Student>() {
		    @Override
		    public int compare(Student s1, Student s2) {
		        return s1.getName().compareToIgnoreCase(s2.getName());
		    }
		});
		
		float name = 200f;
		float note = 300f;
		float[] columnWidth = {name, note, name};
		Table table = new Table(columnWidth);

		for (Student s : studentsWithNotes) {
			table.addCell(new Cell().add(s.getName()));	
			table.addCell(new Cell().add(s.getNotes()));
			table.addCell(new Cell().add(""));
		}

		document.add(table);
		document.close();
		
		if (Desktop.isDesktopSupported()) {
		    try {
		        File myFile = new File(dest);
		        Desktop.getDesktop().open(myFile);
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
		}
	}
	
	public String writeLog() throws FileNotFoundException {
		List<Log> logs = new LogDAO().read();
		
		String logFileName = "system\\pdf\\logs\\" + fileDate + "LOG.pdf"; 
		PdfWriter writer = new PdfWriter(logFileName);
		PdfDocument pdfDocument = new PdfDocument(writer);		
		Document document = new Document(pdfDocument); 
		
		pdfDocument.setDefaultPageSize(PageSize.LETTER);
		
		document.add(new Paragraph("LOG FOR " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy MMM dd h:mma")).replace(".", "").toUpperCase()));
		
		float name = 250f;
		float time = 100f;
		float[] columnWidth = {name, time, time, time};
		Table table = new Table(columnWidth);
		
		table.addCell(new Cell().add("Name").setBackgroundColor(Color.LIGHT_GRAY));
		table.addCell(new Cell().add("Time in").setBackgroundColor(Color.LIGHT_GRAY));
		table.addCell(new Cell().add("Time out").setBackgroundColor(Color.LIGHT_GRAY));
		table.addCell(new Cell().add("Duration").setBackgroundColor(Color.LIGHT_GRAY));
		
		for (Log l : logs) {
			table.addCell(new Cell().add(l.getStudent().getName()));	
			table.addCell(new Cell().add(l.getSignIn().format(format).replace(".", "")));
			table.addCell(new Cell().add(l.getSignOut().format(format).replace(".", "")));
			table.addCell(new Cell().add(Duration.between(l.getSignIn(), l.getSignOut()).toMinutes() + ""));
		}

		document.add(table);
		document.close();
		
		return logFileName;
	}
	
}
