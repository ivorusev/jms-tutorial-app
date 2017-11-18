package org.edu.web.topics;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsTopicConsumer {

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(lookup = "java:jboss/exported/jms/topic/test")
	private Topic topic;

	//public void initialize() {
	//	try {
	//		ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
	//		Connection connection = factory.createConnection();
	//		connection.start();
	//
	//		Topic topic = InitialContext.doLookup("java:jboss/exported/jms/topic/test");
	//		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	//
	//		firstConsumer = session.createDurableSubscriber(topic, "1");
	//		secondConsumer = session.createDurableSubscriber(topic, "2");
	//	} catch (NamingException e) {
	//		e.printStackTrace();
	//	} catch (JMSException e) {
	//		e.printStackTrace();
	//	}
	//}

	public String readFromFirstConsumer() {
		try {
			JMSConsumer consumer1 = context.createSharedDurableConsumer(topic, "1");
			JMSConsumer consumer2 = context.createSharedDurableConsumer(topic, "2");
			Message received1 = consumer1.receive(2000);
			Message received2 = consumer2.receive(2000);
			System.out.println(
					received1.getStringProperty("messageKey") + " " + received2.getStringProperty("messageKey"));
			return "message was empty";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//public String readFromSecondConsumer() {
	//	try {
	//		Message received = secondConsumer.receive(2000);
	//		return received == null ? "message was empty" : received.getStringProperty("messageKey");
	//	} catch (JMSException e) {
	//		e.printStackTrace();
	//	}
	//	return null;
	//}
}
