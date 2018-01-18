package com.spring.kafka.receiver;


public interface EMailService {
public boolean sendSMTPMail(String toAddress, String subject, String message);
public boolean getStatus(String toAddress);
}
