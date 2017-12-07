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

import org.edu.queue.JmsQueueConsumer;
import org.edu.queue.JmsQueueProducer;

@Path("/jms")
@Consumes({ "application/json" })
@Produces({ "application/json" })
public class QueueRestService {

	@Inject
	private JmsQueueProducer producer;
	@Inject
	private JmsQueueConsumer consumer;

	/**
	 * Sends a text message to a jms queue.
	 *
	 * @param message the message from the url param.
	 * @return the {@link Response} object.
	 */
	@GET
	@Path("/send-queue-message")
	public Response getBooks(@QueryParam("message") String message) {
		producer.sendMessage(message);
		return Response.status(200).build();
	}

	/**
	 * Reads a jms message from a jms queue and return a json with its contents.
	 *
	 * @return the {@link Response} object.
	 */
	@GET
	@Path("/read-queue-message")
	public Response readMessage() {
		JsonObject result = Json.createObjectBuilder().add("message-" + System.currentTimeMillis(), consumer
				.read()
		).build();
		return Response.status(200).entity(result).build();
	}
}