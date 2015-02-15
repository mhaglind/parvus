package com.haglind.parvus;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ParvusClient {

//	public static void main(String[] args) {
//
//		ListableBeanFactory context = new ClassPathXmlApplicationContext(
//				"classpath:client/client-config.xml");
//		for (String name : context.getBeanDefinitionNames()) {
//			System.out.println(name + ":" + context.getBean(name));
//		}
//
//		JmsTemplate jms = context.getBean(JmsTemplate.class);
//		jms.send(new MessageCreator() {
//			@Override
//			public Message createMessage(Session session) throws JMSException {
//				TextMessage message = session.createTextMessage();
//				message.setText("fjös");
//				return message;
//			}
//
//		});
//	}
}
