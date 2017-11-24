package org.edu.web.topics;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsTopicProducer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	/**
	 * This injects a {@link Topic} by its JNDI name. Alternatively the {@link Topic}
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Resource(lookup = "java:jboss/exported/jms/topic/test")
	private Topic topic;

	public void sendMessage(String textMessage) {
		try {
			JMSProducer producer = context.createProducer();
			Message message = context.createMessage();
			message.setStringProperty("messageKey", textMessage);
			producer.send(topic, message);
		} catch (JMSException e) {
			LOGGER.error("Error reading message ", e);
		}
	}
}
