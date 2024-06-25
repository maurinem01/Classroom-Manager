package util;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.DefaultAsyncHttpClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import object.Appointment;

public class AcuityHttpClient {

	private static Map<String, String> env = System.getenv();

	private static String USER = env.get("KUMON_ACUITY_USER_ID");
	private static String TOKEN = env.get("KUMON_ACUITY_API_KEY");
	private static HttpClient httpClient = HttpClient.newHttpClient();
	private static Map<String, String> replace = new HashMap<>();

	static {
		replace.put(" ", "%20");
		replace.put("!", "/!");
		replace.put("^", "/^");
	}

	private AcuityHttpClient() {
	}

	private static final String getBasicAuthenticationHeader() {
		String valueToEncode = USER + ":" + TOKEN;
		return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
	}

	/**
	 * Returns one appointment record for corresponding date and student
	 * 
	 * @param dateTime
	 * @param firstName
	 * @param lastName
	 * @return
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public static Appointment getAppointment(LocalDateTime dateTime, String firstName, String lastName)
			throws IOException, InterruptedException {

		String date = dateTime.getMonth() + " " + dateTime.getDayOfMonth() + ", " + dateTime.getYear();

		String url = "https://acuityscheduling.com/api/v1/appointments?"
				+ "minDate=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + "&"
				+ "maxDate=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + "&"
				+ "firstName=" + URLEncoder.encode(firstName, StandardCharsets.UTF_8) + "&"
				+ "lastName=" + URLEncoder.encode(lastName, StandardCharsets.UTF_8) + "&"
				+ "excludeForms=true&direction=ASC";

		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create(url))
				.header("accept", "application/json")
				.header("authorization", getBasicAuthenticationHeader())
				.build();
		HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());

		ObjectMapper mapper = new ObjectMapper();

		List<Appointment> appointments = mapper.readValue(response.body(), new TypeReference<List<Appointment>>() {
		});

		// If more than one appointment is made in a day, the first record will be
		// returned
		return appointments.size() > 0 ? appointments.get(0) : null;
	}

	static List<Appointment> appointments = new ArrayList<>();

	public static Appointment getAsyncAppointment(LocalDateTime dateTime, String firstName, String lastName) {
		String date = dateTime.getMonth() + " " + dateTime.getDayOfMonth() + ", " + dateTime.getYear();
		ObjectMapper mapper = new ObjectMapper();

		String url = "https://acuityscheduling.com/api/v1/appointments?"
				+ "minDate=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + "&"
				+ "maxDate=" + URLEncoder.encode(date, StandardCharsets.UTF_8) + "&"
				+ "firstName=" + URLEncoder.encode(firstName, StandardCharsets.UTF_8) + "&"
				+ "lastName=" + URLEncoder.encode(lastName, StandardCharsets.UTF_8) + "&"
				+ "excludeForms=true&direction=ASC";

		AsyncHttpClient client = new DefaultAsyncHttpClient();
		client.prepare("GET", url)
				.setHeader("accept", "application/json")
				.setHeader("authorization", getBasicAuthenticationHeader())
				.execute()
				.toCompletableFuture()
				.thenAccept(res -> {
					try {
						appointments = mapper.readValue(res.getResponseBody(), new TypeReference<List<Appointment>>() {
						});
					} catch (JsonProcessingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				})
				.join();

		try {
			client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return appointments != null && appointments.size() > 0 ? appointments.get(0) : null;
	}

	// public static void main(String[] args) {
	// getAsyncAppointment(LocalDateTime.now(), "Draco", "Malfoy");
	// }
}
