package org.edu.web.rest;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.edu.topic.JmsTopicConsumer;
import org.edu.topic.JmsTopicProducer;

/**
 * Rest services for {@link javax.jms.Topic} demonstration.
 */
@Path("/")
@Consumes({ "application/json" })
@Produces({ "application/json" })
@Transactional
public class TopicRestService {

	@Inject
	private JmsTopicProducer producer;
	@Inject
	private JmsTopicConsumer consumer;

	@GET
	@Path("/send-topic-message")
	public Response getBooks(@QueryParam("message") String message) {
		producer.sendMessage(message);
		return Response.status(200).build();
	}

	@GET
	@Path("/read-topic-message")
	public Response readMessage() {
		JsonObject result = Json.createObjectBuilder().add("message", consumer.readFromFirstConsumer()).build();
		return Response.status(200).entity(result).build();
	}
}