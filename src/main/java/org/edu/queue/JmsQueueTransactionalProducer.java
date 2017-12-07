package org.edu.queue;

import java.lang.invoke.MethodHandles;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.naming.InitialContext;
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
public class JmsQueueTransactionalProducer {
	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	/**
	 * This injects a queue by its JNDI name. Alternatively the queue
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Resource(lookup = "java:jboss/exported/jms/queue/test")
	private Queue queue;

	public void sendMessage(String textMessage) {
		Connection connection = null;
		Session session = null;
		try {
			ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
			connection = factory.createConnection();
			connection.start();

			session = connection.createSession(true, Session.SESSION_TRANSACTED);
			MessageProducer producer = session.createProducer(queue);
			Message message = session.createMessage();
			message.setStringProperty("messageKey", textMessage);
			producer.send(message);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				connection.close();
				session.close();
			} catch (JMSException e1) {
				e1.printStackTrace();
			}
		}
	}
}
