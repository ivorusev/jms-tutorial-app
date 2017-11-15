package org.edu.web.queue;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
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
public class JmsQueueProducer {

	private MessageProducer producer;
	private Session session;

	@PostConstruct
	public void init() {
		try {
			ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
			Connection connection = factory.createConnection();
			Queue queue = InitialContext.doLookup("java:jboss/exported/jms/queue/test");

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
			producer = session.createProducer(queue);
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendMessage(String textMessage) {
		try {
			Message message = session.createMessage();
			message.setStringProperty("messageKey", textMessage);
			producer.send(message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
