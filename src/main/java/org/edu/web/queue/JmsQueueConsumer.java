package org.edu.web.queue;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;
import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is a sample class that creates a JMS consumer and reads a message from a Queue.
 *
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsQueueConsumer {

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

	/**
	 * Reads a text message from the queue.
	 *
	 * @return the message string.
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public String read() {
		try {
			JMSConsumer consumer = context.createConsumer(queue);
			Message received = consumer.receive(2000);
			return received == null ? "message was empty" : received.getStringProperty("messageKey");
		} catch (Exception e) {
			LOGGER.error("Error consuming message {}", e);
		}
		return "message was empty";
	}
}
