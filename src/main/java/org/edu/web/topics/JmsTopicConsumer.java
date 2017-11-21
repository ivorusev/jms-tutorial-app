package org.edu.web.topics;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Name;

/**
 * Sample {@link Topic} consumer.
 *
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@ApplicationScoped
public class JmsTopicConsumer {

	/**
	 * {@link JMSContext} comes from the jms 2.0 specification and simplifies the API by wrapping the
	 * {@link javax.jms.Session} and {@link javax.jms.Connection} objects.
	 * <p>
	 * The {@link JMSConnectionFactory} specifies which connection factory to be used. As parameter is used the JNDI
	 * name.
	 * <p>
	 * Alternatively the queue
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Inject
	@JMSConnectionFactory("java:jboss/DefaultJMSConnectionFactory")
	private JMSContext context;

	/**
	 * This injects a {@link Topic} by its JNDI name. Alternatively the {@link Topic}
	 * can be looked up by using: {@link javax.naming.InitialContext#doLookup(Name)}
	 */
	@Resource(lookup = "java:jboss/exported/jms/topic/test")
	private Topic topic;

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

}
