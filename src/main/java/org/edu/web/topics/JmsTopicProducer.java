package org.edu.web.topics;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.JMSProducer;
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
public class JmsTopicProducer {

	//private MessageProducer producer;
	//private Session session;

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(lookup = "java:jboss/exported/jms/topic/test")
	private Topic topic;

	//@PostConstruct
	//public void initialize() {
	//	try {
	//		ConnectionFactory factory = InitialContext.doLookup("java:jboss/DefaultJMSConnectionFactory");
	//		Connection connection = factory.createConnection();
	//		Topic topic = InitialContext.doLookup("java:jboss/exported/jms/topic/test");
	//
	//		session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	//		producer = session.createProducer(topic);
	//	} catch (NamingException e) {
	//		e.printStackTrace();
	//	} catch (JMSException e) {
	//		e.printStackTrace();
	//	}
	//}

	public void sendMessage(String textMessage) {
		try {
			JMSProducer producer = context.createProducer();
			Message message = context.createMessage();
			message.setStringProperty("messageKey", textMessage);
			producer.send(topic, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
