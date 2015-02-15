package com.haglind.parvus.endpoint;

import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import org.joda.time.DateTime;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haglind.parvus.domain.Message;
import com.haglind.parvus.repository.MessageRepository;

@Service
@Transactional
public class MessageActivator implements
		SessionAwareMessageListener<BytesMessage>, IMessageActivator {

	@Inject
	MessageRepository repo;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.haglind.parvus.endpoint.IMessageActivator#onMessage(javax.jms.TextMessage
	 * , javax.jms.Session)
	 */
	@Override
	public void onMessage(BytesMessage message, Session session)
			throws JMSException {
		
		System.out.println(message);
		Message msg = new Message();
		byte[] bytes = new byte[ (int)message.getBodyLength()];
		message.readBytes(bytes);
		msg.setPayload(bytes.toString());
		msg.setCreated(new DateTime());
		repo.save(msg);
	}
}
