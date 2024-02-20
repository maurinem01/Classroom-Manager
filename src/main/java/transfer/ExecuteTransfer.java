package transfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.concurrent.ExecutionException;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//import java.util.concurrent.Future;
//
//import dao.*;

public class ExecuteTransfer {

//	public static void main(String[] args) {
//		ExecutorService service = Executors.newSingleThreadExecutor();
//		
//		// Take lines from a file
//		List<String> people = getRecordsFromFile("C:\\Users\\Maurine\\Downloads\\FGK CONTACTS MAY 18.csv");
//		
//		// Assign these lines to a Contact using the connection and methods from ContactDAO (data access object)
//		ContactDAO dao = new ContactDAO();
//		for(String x : people) {
//			Future<Integer> future = service.submit(new ContactProcessor(x, dao));
//			try {
//				System.out.println("Result of the operation is " + future.get());
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			} catch (ExecutionException e) {
//				e.printStackTrace();
//			}
//		}
//		service.shutdown();
//		System.out.println("Main execution done.");
//	}
	
	/**
	 * This method takes lines from a file and adds them to a List<String>
	 * @param fileName the path to the file containing information
	 * @return the List<String> containing the lines from the file
	 */
	public static List<String> getRecordsFromFile(String fileName) {
		List<String> students = new ArrayList<>();
		try(BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)))) {
			String line;
			while ((line = reader.readLine()) != null) {
				students.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return students;
	}
	
}
