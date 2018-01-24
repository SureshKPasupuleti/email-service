package com.spring.kafka.receiver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring4.SpringTemplateEngine;

import org.thymeleaf.context.Context;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;

import javax.mail.Session;
import javax.mail.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.mail.internet.*;


@Service
public class EMailServiceImpl implements EMailService {

	@Value("${spring.mail.host}")
	private String host;

	@Value("${spring.mail.username}")
	private String username = "noreplykafka@gmail.com";

	@Value("${spring.mail.password}")
	private String password;

	@Value("${spring.mail.protocol}")
	private String protocol;

	@Value("${htmlImage.welcome}")
	private String welcomeImage;

	@Value("${htmlImage.footer}")
	private String footerImage;

	@Value("${emailService.subject}")
	private String subject;

	@Value("${emailService.htmlEmailTemplate}")
	private String htmlEmailTemplate;

	@Value("${emailService.textEmailTemplate}")
	private String textEmailTemplate;

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	SpringTemplateEngine templateEngine;

	private static final ApplicationLogger logger = ApplicationLogger.getInstance();

	@Bean
	public PasswordAuthentication passwordAuthentication() {
		return new PasswordAuthentication(username, password);
	}

	@Override
	public void send(Employer employer,boolean isHtml) {

		if (isHtml) {
			try {
				sendHtmlMail(employer,htmlEmailTemplate);
			} catch (MessagingException e) {
				logger.error("Could not send email to : {} Error = {}", employer.getEmployeeEmailAddress(), e.getMessage());
			}
		} else {
			sendPlainTextMail(employer,textEmailTemplate);
		}

	}


	@Override
	public void sendSimpleMessage(Mail mail) throws MessagingException,IOException{
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message,
				MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.addAttachment("logo.png", new ClassPathResource("memorynotfound-logo.png"));

		Context context = new Context();
		context.setVariables(mail.getModel());
		String html = templateEngine.process("email-template", context);

		helper.setTo(mail.getTo());
		helper.setText(html, true);
		helper.setSubject(mail.getSubject());
		helper.setFrom(mail.getFrom());

		emailSender.send(message);
	}

	@Override
	public boolean sendSMTPMail(String toAddress, String subject, String message) {
		Properties props = System.getProperties();

		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", host);
		props.put("", username);
		props.put("mail.smtp.password", password);
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");


		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						return passwordAuthentication();
					}
				});


		MimeMessage mimeMessage = new MimeMessage(session);

		try {
			mimeMessage.setFrom(new InternetAddress(username));

			mimeMessage.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(toAddress));
			mimeMessage.setSubject(subject);

			mimeMessage.setText(message);

			Transport transport = session.getTransport(protocol);

			transport.connect(host, username, password);
			transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
			transport.close();
			System.out.println("Sent message successfully....");
		} catch (MessagingException mex) {
			mex.printStackTrace();
			return false;
		}

		return true;
	}

	private void sendHtmlMail(Employer employer,String emailTemplate) throws MessagingException {

		Email eParams = getEmailInfo(employer,emailTemplate);
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

			//helper.addAttachment("nisum_welcome.jpg",new ClassPathResource("nisum_welcome.jpg"));
			//helper.addAttachment("nisum_hr_footer.png",new ClassPathResource("nisum_hr_footer.png"));
			helper.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
			helper.setReplyTo(eParams.getFrom());
			helper.setFrom(eParams.getFrom());
			helper.setSubject(eParams.getSubject());
			helper.setText(eParams.getMessage(), true);

			helper.addInline(welcomeImage,new ClassPathResource(welcomeImage));

			helper.addInline(footerImage,new ClassPathResource(footerImage));

			if (eParams.getCc().size() > 0) {
				helper.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
			}

			emailSender.send(message);

	}

	private void sendPlainTextMail(Employer employer,String emailTemplate) {


		Email eParams = getEmailInfo(employer,emailTemplate);

		SimpleMailMessage mailMessage = new SimpleMailMessage();

		eParams.getTo().toArray(new String[eParams.getTo().size()]);
		mailMessage.setTo(eParams.getTo().toArray(new String[eParams.getTo().size()]));
		mailMessage.setReplyTo(eParams.getFrom());
		mailMessage.setFrom(eParams.getFrom());
		mailMessage.setSubject(eParams.getSubject());
		mailMessage.setText(eParams.getMessage());

		if (eParams.getCc().size() > 0) {
			mailMessage.setCc(eParams.getCc().toArray(new String[eParams.getCc().size()]));
		}

		emailSender.send(mailMessage);

	}

	/*private void sendTextMail(Employer employer,String emailTemplate) {

		String from = "noreplykafka@gmail.com";
		String to = "spasupuleti@nisum.com";
		String subject = "Java Mail with Spring Boot";

		EmailTemplate template = new EmailTemplate(emailTemplate);

		Map<String, String> replacements = new HashMap<String, String>();
		replacements.put("user", "Suresh");
		replacements.put("today", String.valueOf(new Date()));

		String message = template.getTemplate(replacements);

		Email email = new Email(from, to, subject, message);

		emailService.send(email);
	}*/

	private Email getEmailInfo(Employer employer,String emailTemplate) {

		EmailTemplate template = new EmailTemplate(emailTemplate);

		Map<String, String> replacements = getReplacementsValues(employer);


		String message = template.getTemplate(replacements);

		Email email = new Email(username, employer.getEmployeeEmailAddress(), subject, message);

		email.setCc(Arrays.asList(employer.getGroupEmailAddress().split(",")));

		return email;
	}

	private Map<String, String> getReplacementsValues(Employer employer){

		String strDateFormat = "yyyy/MM/dd HH:mm:ss";
		DateFormat dateFormat = new SimpleDateFormat(strDateFormat);


		Map<String, String> replacements = new HashMap<>();
		replacements.put("user", employer.getFirstName()+ " " + employer.getLastName());

		LocalDateTime localDateTime = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
		localDateTime = localDateTime.plusDays(2);
		Date replacementDate = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
		replacements.put("today", dateFormat.format(replacementDate));


		if("No".equals(employer.getEnrolledForBenefits()))
			replacements.put("signedOnHarrassementPolicy", "HarrassementPolicy");
		else{
			replacements.put("signedOnHarrassementPolicy", " ");
		}

		if("No".equals(employer.getSignedOnHarrassementPolicy()))
			replacements.put("signedOnEmployeeAgreement", "EmployeeAgreement");
		else{
			replacements.put("signedOnEmployeeAgreement", " ");
		}

		if("No".equals(employer.getSignedOnEmployeeAgreement()))
			replacements.put("enrolledForMedicalInsurance", "EnrolledForMedicalInsurance");
		else{
			replacements.put("enrolledForMedicalInsurance", " ");
		}

		if("No".equals(employer.getEnrolledForMedicalInsurance()))
			replacements.put("enrolledForBenefits", "EnrolledForBenefits");
		else{
			replacements.put("enrolledForBenefits", " ");
		}

		return replacements;
	}


}
