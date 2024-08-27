package transfer;

import java.util.StringTokenizer;
import java.util.concurrent.Callable;

import dao.*;
import model.*;

public class StudentProcessor implements Callable<Integer> {

	private String studentRecord;
	private StudentDAO dao;

	public StudentProcessor(String studentRecord, StudentDAO dao) {
		this.studentRecord = studentRecord;
		this.dao = dao;
	}

	@Override
	public Integer call() throws Exception {
		int rows = 0;
		StringTokenizer tokenizer = new StringTokenizer(studentRecord, ",");
		Student student = null;
		System.out.println(Thread.currentThread() + " processing record for: " + studentRecord);
		while (tokenizer.hasMoreTokens()) {
			student = new Student();
			student.setID(Integer.parseInt(tokenizer.nextToken()));
			// student.setStudentNumber(tokenizer.nextToken());
			student.setFName(tokenizer.nextToken());
			student.setLName(tokenizer.nextToken());
			student.setBirthday(tokenizer.nextToken());
			student.setSubjectID(Integer.parseInt(tokenizer.nextToken())); // use Integer.valueOf?
			rows = dao.save(student);
		}
		return rows;
	}

}
