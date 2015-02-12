package com.haglind.parvus.endpoint;

import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.joda.time.DateTime;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Component;

import com.haglind.parvus.domain.Message;
import com.haglind.parvus.repository.MessageRepository;

@Component
public class MessageActivator implements SessionAwareMessageListener<TextMessage> {

	@Inject
	MessageRepository repo;

	@Override
	public void onMessage(TextMessage message, Session session) throws JMSException {
		Message msg = new Message();
		msg.setPayload(message.getText());
		msg.setCreated(new DateTime());
		repo.save(msg);
	}

}
