package com.haglind.parvus.config;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final Logger log = LoggerFactory.getLogger(MessagingConfiguration.class);
	
	@Autowired
	private IMessageActivator messageActivator;

	@Bean
	public DefaultMessageListenerContainer defaultMessageListenerContainer() {
		log.debug("Creating MessageListenerContainer");
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
		String queueName = "parvusqueue";
		log.debug("Creating topic: " + queueName);
		//ActiveMQQueue queue = new ActiveMQQueue("parvusqueue");
		ActiveMQTopic topic = new ActiveMQTopic(queueName);
		return topic;
	}

	@Bean
	public ConnectionFactory defaultConnectionFactory() {
		String brokerUrl = "tcp://localhost:61616";
		log.debug("Creating connection factory for " + brokerUrl);
		ActiveMQConnectionFactory cf = new ActiveMQConnectionFactory();
		cf.setBrokerURL(brokerUrl);
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
