package ru.kazbo.javamail;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import jakarta.mail.MessagingException;

class TestGmailSender {
	
	static GmailSender sender;
	
	@BeforeAll
	static void initSender() {
		final String login = "your_gmail@gmail.com";
		final String password = "yourHardPassword123456789";
		sender = new GmailSender(login, password);
	}
	
	@Test
	void sendMessage() throws MessagingException {
		String sendTo = "send_to@email.com";
		String subject = "Hello, my fried! I Have letter for you";
		String htmlMessage = "<h1>Hello, friend!</h1><p>Sub text</p>";
		sender.sendTextMessage(sendTo, subject, htmlMessage);
	}
}