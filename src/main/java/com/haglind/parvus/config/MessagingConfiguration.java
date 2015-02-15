package com.haglind.parvus.config;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import com.haglind.parvus.endpoint.IMessageActivator;

@Configuration
@ImportResource("classpath:/activemq/jms-infrastructure-activemq.xml")
public class MessagingConfiguration {

	@Autowired
	private IMessageActivator messageActivator;

	@Bean
	public DefaultMessageListenerContainer defaultMessageListenerContainer() {
		DefaultMessageListenerContainer myContainer = new DefaultMessageListenerContainer();
		myContainer.setConcurrentConsumers(1);
		myContainer.setMaxConcurrentConsumers(1);
		myContainer.setConnectionFactory(defaultConnectionFactory());
		myContainer.setDestination(defaultDestination());
		myContainer.setSessionTransacted(true);
		myContainer.setMessageListener(messageActivator);
		return myContainer;
	}

	@Bean
	public Destination defaultDestination() {
		//ActiveMQQueue queue = new ActiveMQQueue("parvusqueue");
		ActiveMQTopic topic = new ActiveMQTopic("parvusqueue");
		return topic;
	}

	@Bean
	public ConnectionFactory defaultConnectionFactory() {
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		cf.setBrokerURL("tcp://localhost:61616");
		//cf.setBrokerURL("mqtt://0.0.0.0:1883");
		cf.setClientID("parvus_server");
		
		return cf;
	}
	
	@Bean
	public JmsTemplate defaultJmsTemplate() {
		JmsTemplate jt = new JmsTemplate(defaultConnectionFactory());
		jt.setDefaultDestination(defaultDestination());
		return jt;
	}
}
