package org.edu.web.topics;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.jms.JMSConnectionFactory;
import javax.jms.JMSConsumer;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Topic;
import javax.naming.Name;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample {@link Topic} consumer.
 *
 * @author <a href="mailto:ivo.rusev@sirma.bg">Ivo Rusev</a>
 * @since 03/11/2017
 */
@RequestScoped
public class JmsTopicConsumer {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

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

	private List<JMSConsumer> consumers;

	@PostConstruct
	private void init() {
		consumers = new ArrayList<>();
		JMSConsumer consumer1 = context.createSharedDurableConsumer(topic, "1");
		JMSConsumer consumer2 = context.createSharedDurableConsumer(topic, "2");
		JMSConsumer consumer3 = context.createSharedDurableConsumer(topic, "3");
		JMSConsumer consumer4 = context.createSharedDurableConsumer(topic, "4");
		JMSConsumer consumer5 = context.createSharedDurableConsumer(topic, "5");
		consumers.addAll(Arrays.asList(consumer1, consumer2, consumer3, consumer4, consumer5));
	}

	public String readFromFirstConsumer() {
		try {
			List<String> collect = consumers.stream()
					// blocking. If you add timeout -> non-blocking.
					.map(JMSConsumer::receive)
					.filter(Objects::nonNull)
					.map(this::extractSafe)
					.collect(Collectors.toList());
			return collect == null || collect.isEmpty() ? "message was empty" : collect.toString();
		} catch (Exception e) {
			LOGGER.error("Error reading message {}", e);
		}
		return null;
	}

	private String extractSafe(Message message) {
		try {
			return message.getStringProperty("messageKey");
		} catch (JMSException e) {
			LOGGER.error("Error extracting property {}", e);
		}
		return "";
	}

}
