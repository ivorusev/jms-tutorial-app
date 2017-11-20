package org.edu.web.topics;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsTopicProducer {

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(lookup = "java:jboss/exported/jms/topic/test")
	private Topic topic;

	public void sendMessage(String textMessage) {
		try {
			JMSProducer producer = context.createProducer();
			Message message = context.createMessage();
			message.setStringProperty("messageKey", textMessage);
			producer.send(topic, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
