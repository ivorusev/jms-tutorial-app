package org.edu.web.topics;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.edu.web.queue.JmsQueueConsumer;
import org.edu.web.queue.JmsQueueProducer;

@Path("/topic")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class RestService {

	@Inject
	private JmsTopicProducer producer;
	@Inject
	private JmsTopicConsumer consumer;

	@GET
	@Path("/init")
	public void init() {
		consumer.initialize();
	}

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