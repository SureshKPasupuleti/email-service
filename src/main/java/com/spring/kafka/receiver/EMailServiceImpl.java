package com.spring.kafka.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;

import org.springframework.stereotype.Service;
import javax.mail.Session;
import javax.mail.*;
import java.util.Properties;
import javax.mail.internet.*;

@Service
public class EMailServiceImpl implements EMailService {

	private static final String HOST = "smtp.gmail.com";

	private static final String FROM_ADDRESS = "noreplykafka@gmail.com";

	private static final String NO_REPLY_123 = "noreply@123";
	private static final String PROTOCOL = "smtp";

	@Autowired
	private JavaMailSender sender;

	@Override
	public boolean getStatus(String toAddress){
		return true;
	}

	@Override
	public boolean sendSMTPMail(String toAddress, String subject, String message) {
		Properties props = System.getProperties();

		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", HOST);
		props.put("", FROM_ADDRESS);
		props.put("mail.smtp.password", NO_REPLY_123);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");

		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(FROM_ADDRESS, NO_REPLY_123);
					}
				});

		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom(new InternetAddress(FROM_ADDRESS));

			mimeMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			mimeMessage.setSubject(subject);

			mimeMessage.setText(message);

			Transport transport = session.getTransport(PROTOCOL);

			transport.connect(HOST, FROM_ADDRESS, NO_REPLY_123);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}

		return true;
	}

}
