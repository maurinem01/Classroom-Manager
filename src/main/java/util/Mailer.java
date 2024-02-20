package util;

import java.util.Properties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.apache.commons.codec.binary.Base64;

import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.Set;

import static com.google.api.services.gmail.GmailScopes.GMAIL_SEND;
import static javax.mail.Message.RecipientType.TO;

public class Mailer {

    private static final String TO_EMAIL = Config.getCredentials().get("alert_email");
    private static final String FROM_EMAIL = "Classroom Manager<" + TO_EMAIL +">";
    private final Gmail service;

    public Mailer() throws Exception {
        NetHttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
        GsonFactory jsonFactory = GsonFactory.getDefaultInstance();
        service = new Gmail.Builder(httpTransport, jsonFactory, getCredentials(httpTransport, jsonFactory))
                .setApplicationName("Kumon Acuity Appointment Alert")
                .build();
    }

    private static Credential getCredentials(final NetHttpTransport httpTransport, GsonFactory jsonFactory)
            throws IOException {
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(jsonFactory, 
        		new InputStreamReader(Mailer.class.getResourceAsStream("gmailClientSecret.json")));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, jsonFactory, clientSecrets, Set.of(GMAIL_SEND))
                .setDataStoreFactory(new FileDataStoreFactory(Paths.get("tokens").toFile()))
                .setAccessType("offline")
                .build();

        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    public void sendMail(String subject, String message) {
        Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        
        try {
			email.setFrom(new InternetAddress(FROM_EMAIL));
	        email.addRecipient(TO, new InternetAddress(TO_EMAIL));
	        email.setSubject(subject);
	        email.setText(message);
			email.writeTo(buffer);
			

	        byte[] rawMessageBytes = buffer.toByteArray();
	        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
	        Message msg = new Message();
	        msg.setRaw(encodedEmail);

	        try {
	            msg = service.users().messages().send("me", msg).execute();
	        } catch (GoogleJsonResponseException e) {
	            GoogleJsonError error = e.getDetails();
	            if (error.getCode() == 403) {
	                System.err.println("Unable to send message: " + e.getDetails());
	            } else {
	                throw e;
	            }
	        }
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}

    }
    
    public void sendAttachment(String subject, String message, String file) {
    	Properties props = new Properties();
        Session session = Session.getDefaultInstance(props, null);
        MimeMessage email = new MimeMessage(session);
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

        Multipart multipart = new MimeMultipart();
        MimeBodyPart mimeBodyPart = new MimeBodyPart();        
        BodyPart bodyPart = new MimeBodyPart();
        
        try {
			mimeBodyPart.attachFile(new File(file));
	        try {
				multipart.addBodyPart(mimeBodyPart);
				bodyPart.setText(message);
				multipart.addBodyPart(bodyPart);

				email.setFrom(new InternetAddress(FROM_EMAIL));
		        email.addRecipient(TO, new InternetAddress(TO_EMAIL));
		        email.setSubject(subject);
		        
				email.setContent(multipart);
		        
				email.writeTo(buffer);
		        
		        byte[] rawMessageBytes = buffer.toByteArray();
		        String encodedEmail = Base64.encodeBase64URLSafeString(rawMessageBytes);
		        Message msg = new Message();
		        msg.setRaw(encodedEmail);
				
		        try {
		            msg = service.users().messages().send("me", msg).execute();
		        } catch (GoogleJsonResponseException e) {
		            GoogleJsonError error = e.getDetails();
		            if (error.getCode() == 403) {
		                System.err.println("Unable to send message: " + e.getDetails());
		            } else {
		                throw e;
		            }
		        }
				System.out.println("SENT CLASS LOG");
			} catch (MessagingException e) {
				// POPUP??
				e.printStackTrace();
			}
		} catch (IOException | MessagingException e) {
			// POPUP???
			e.printStackTrace();
		}        

    }
}
