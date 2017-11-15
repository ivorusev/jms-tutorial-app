package org.edu.web.topics;

import javax.enterprise.context.ApplicationScoped;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import javax.jms.Topic;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsTopicConsumer {

	private MessageConsumer firstConsumer;
	private MessageConsumer secondConsumer;

	private Session session;

	public void initialize() {
		try {
			ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
			Connection connection = factory.createConnection();
			connection.start();

			Topic topic = InitialContext.doLookup("java:jboss/exported/jms/topic/test");
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			firstConsumer = session.createDurableSubscriber(topic, "1");
			secondConsumer = session.createDurableSubscriber(topic, "2");
		} catch (NamingException e) {
			e.printStackTrace();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public String readFromFirstConsumer() {
		try {
			Message received = firstConsumer.receive(2000);
			return received == null ? "message was empty" : received.getStringProperty("messageKey");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String readFromSecondConsumer() {
		try {
			Message received = secondConsumer.receive(2000);
			return received == null ? "message was empty" : received.getStringProperty("messageKey");
		} catch (JMSException e) {
			e.printStackTrace();
		}
		return null;
	}
}
