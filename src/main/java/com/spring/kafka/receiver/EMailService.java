package com.spring.kafka.receiver;


import javax.mail.MessagingException;
import java.io.IOException;

public interface EMailService {
public boolean sendSMTPMail(String toAddress, String subject, String message);
public void sendSimpleMessage(Mail mail) throws MessagingException,IOException;
public void send(Employer employer,boolean isHtml);
}
