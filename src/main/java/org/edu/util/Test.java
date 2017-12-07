package org.edu.util;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSRuntimeException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 27/11/2017
 */
public class Test {

	public void sendMessageJMS20(ConnectionFactory connectionFactory, Queue queue,
			String text) {
		try (JMSContext context = connectionFactory.createContext()) {
			context.createProducer().send(queue, text);
		} catch (JMSRuntimeException ex) {
			// handle exception (details omitted)
		}
	}

	public static void main(String[] args) throws NamingException, JMSException {
		ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
		Connection connection = factory.createConnection();
		connection.start();
		Queue queue = InitialContext.doLookup("java:jboss/exported/jms/queue/test");

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		MessageConsumer consumer = session.createConsumer(queue);
		Message m = consumer.receive();
		if (m instanceof TextMessage) {
			TextMessage message = (TextMessage) m;
			System.out.println("Reading message: " + message.getText());
		} else {
			// Handle error
		}
	}

}
