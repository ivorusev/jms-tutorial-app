package org.edu.queue;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer for simple JMS queue application.
 *
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsQueueProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * {@link JMSContext} comes from the jms 2.0 specification and simplifies the API by wrapping the
	 * {@link javax.jms.Session} and {@link javax.jms.Connection} objects.
	 * <p>
	 * The {@link JMSConnectionFactory} specifies which connection factory to be used. As parameter is used the JNDI
	 * name.
	 * <p>
	 * Alternatively the queue
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	/**
	 * This injects a queue by its JNDI name. Alternatively the queue
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Resource(lookup = "java:jboss/exported/jms/queue/test")
	private Queue queue;

	public void sendMessage(String textMessage) {
		try {
			Message message = context.createMessage();
			message.setStringProperty("messageKey", textMessage);
			context.createProducer().send(queue, message);
		} catch (JMSException e) {
			LOGGER.error("Error sending message {}", e);
		}
	}
}
