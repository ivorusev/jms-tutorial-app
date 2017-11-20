package org.edu.web.rest;

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

@Path("/jms")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class QueueRestService {

	@Inject
	private JmsQueueProducer producer;
	@Inject
	private JmsQueueConsumer consumer;

	@GET
	@Path("/send-queue-message")
	public Response getBooks(@QueryParam("message") String message) {
		producer.sendMessage(message);
		return Response.status(200).build();
	}

	@GET
	@Path("/read-queue-message")
	public Response readMessage() {
		JsonObject result = Json.createObjectBuilder().add("message", consumer.read()).build();
		return Response.status(200).entity(result).build();
	}
}