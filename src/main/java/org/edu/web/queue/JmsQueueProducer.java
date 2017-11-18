package org.edu.web.queue;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsQueueProducer {

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(lookup = "java:jboss/exported/jms/queue/test")
	private Queue queue;

	public void sendMessage(String textMessage) {
		try {
			Message message = context.createMessage();
			message.setStringProperty("messageKey", textMessage);
			context.createProducer().send(queue, message);
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}
