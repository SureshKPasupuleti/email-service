package com.spring.kafka.receiver;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;

import java.io.IOException;

public class KafkaReceiver {

	@Autowired
	EMailService emailService;

	@Autowired
	private ObjectMapper objectMapper;

	@KafkaListener(id="test",topicPattern="employeeEmail")
	public void listen(@Payload String message,
			@Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
			@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
			@Header(KafkaHeaders.RECEIVED_PARTITION_ID) Integer partition
			) {
		System.out.println("received message..."+message+"<<key>>"+key+"<<topic>>"+topic+"<<partition>>"+partition);

		try {
			Employer employer = objectMapper.readValue(message, Employer.class);
			boolean result = emailService.sendSMTPMail(employer.getEmployeeEmailAddress()+","+employer.getGroupEmailAddress(),"TestKafkaEmployer",message);
		}catch(IOException ioEx){
			ioEx.printStackTrace();
		}



	}
}
