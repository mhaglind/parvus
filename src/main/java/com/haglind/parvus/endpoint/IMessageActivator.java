package com.haglind.parvus.endpoint;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Session;

public interface IMessageActivator {

	void onMessage(BytesMessage message, Session session) throws JMSException;

}