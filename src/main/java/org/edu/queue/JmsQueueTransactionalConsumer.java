package org.edu.queue;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
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
public class JmsQueueTransactionalConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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
	public String read() throws JMSException {
		Connection connection = null;
		Session session = null;
		try {
			ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(true, Session.SESSION_TRANSACTED);

			if (true) {
				throw new IllegalArgumentException();
			}

			MessageConsumer consumer = session.createConsumer(queue);
			Message message = consumer.receive();
			return message.getStringProperty("messageKey");
		} catch (Exception e) {
			connection.close();
			session.close();
		}
		return null;
	}
}
