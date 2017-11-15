package org.edu.web.queue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsQueueConsumer {

	private MessageConsumer consumer;
	private Session session;

	@PostConstruct
	public void init() {
		try {
			ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
			Connection connection = factory.createConnection();
			connection.start();
			Queue queue = InitialContext.doLookup("java:jboss/exported/jms/queue/test");

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			consumer = session.createConsumer(queue);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public String read() {
		try {
			Message received = consumer.receive(2000);
			return received == null ? "message was empty" : received.getStringProperty("messageKey");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
	}
}
