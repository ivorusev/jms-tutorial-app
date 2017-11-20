package org.edu.web.queue;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Queue;

/**
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsQueueConsumer {

	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	@Resource(lookup = "java:jboss/exported/jms/queue/test")
	private Queue queue;

	public String read() {
		try {
			JMSConsumer consumer = context.createConsumer(queue);
			Message received = consumer.receive(2000);
			return received == null ? "message was empty" : received.getStringProperty("messageKey");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
