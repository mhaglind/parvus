package com.haglind.parvus.endpoint;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.haglind.parvus.config.MessagingConfiguration;
import com.haglind.parvus.domain.Message;
import com.haglind.parvus.repository.MessageRepository;

@Service
@Transactional
public class MessageActivator implements
		SessionAwareMessageListener<BytesMessage>, IMessageActivator {

	private final Logger log = LoggerFactory.getLogger(MessageActivator.class);
	
	@Inject
	MessageRepository repo;

	@PostConstruct
	public void init() {
		log.debug("Initializing MessageActivator");
	}
	
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
		log.debug("onMessage: " + message);
		Message msg = new Message();
		byte[] bytes = new byte[ (int)message.getBodyLength()];
		message.readBytes(bytes);
		String payload = new String(bytes);
		msg.setPayload(payload);
		msg.setCreated(new DateTime());
		msg = repo.save(msg);
		log.debug("Persisted: " + msg);
	}
}
