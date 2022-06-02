package ru.kazbo.javamail;

import java.util.stream.Stream;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import jakarta.mail.Session;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Message;
import jakarta.mail.Transport;
import jakarta.mail.MessagingException;
import jakarta.mail.BodyPart;
import jakarta.mail.Multipart;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

public class GmailSender {
	
	private String fromEmail, password;
	private Session senderSession;
	
	/**
	 * Creating a object for sending GMAIL-messages
	 * @param email The email from where message will be sent
	 * @param password The password from email
	 */
	public GmailSender(String email, String password) {
		fromEmail = email;
		this.password = password;
		senderSession = Session.getInstance(getGmailProperties(), new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, password);
			}
		});
	}
	
	/**
	 * Send message to other Gmail
	 *	@param sendTo The email-address where be sent message
	 *	@param subject The subject of message
	 *	@param htmlText The HTML-text of message
	 */
	public void sendTextMessage(String sendTo, String subject, String htmlText) throws MessagingException {
		Message msg = new MimeMessage(senderSession);
		msg.setFrom(new InternetAddress(fromEmail));
		msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
		msg.setSubject(subject);
		addMultipartMessage(msg, htmlText);
		Transport.send(msg);
	}
	
	private void addMultipartMessage(final Message msg, String text) throws MessagingException {
		Multipart multipart = new MimeMultipart();
		BodyPart bp = getHTMLMessage(text);
		multipart.addBodyPart(bp);
		msg.setContent(multipart);
	}
	
	private BodyPart getHTMLMessage(String text) throws MessagingException {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setContent(text, "text/html; charset=utf-8");
		return mimeBodyPart;
	}
	
	private BodyPart addFile(String filePath) throws MessagingException {
		MimeBodyPart filesBodyPart = new MimeBodyPart();
		try {
			filesBodyPart.attachFile(new File(filePath));
		} catch(IOException e) {
			e.printStackTrace();
		}
		return filesBodyPart;
	}
	
	private Properties getGmailProperties() {
		Properties prop = new Properties();
		// setting for gmail (smtp.gmail.com)
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "587");
		prop.put("mail.smtp.auth", true);
		prop.put("mail.smtp.starttls.enable", "true");
		return prop;
	}
}