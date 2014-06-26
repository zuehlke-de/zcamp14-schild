package com.zuehlke.camp2014.schild.siegfried;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.common.base.Function;

import com.google.common.collect.Lists;
import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateStatus;
import com.zuehlke.camp2014.schild.siegfried.logic.UpdatesLogic;

@Path("/updates")
public class UpdatesService {
 
	@GET
	@Path("/")
	public List<Update> getAll() {
		return Lists.newArrayList(UpdatesLogic.updates);
	}
	
	@GET
	@Path("/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Update> getPending() {
		
		List<Update> response = Lists.newArrayList();

		/* Getting updates from the cloud */
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory("camp2014").getMessageBufferStore();
		final List<MessageBuffer> messages = store.loadByKey("status", "pending");
		Function<MessageBuffer, Update> convertMessageBufferToUpdate = new Function<MessageBuffer, Update>() {
			@Override
			public Update apply(MessageBuffer input) {
				return new Update(
						new Long(input.getSequence()).toString(), 
						input.message().payload().getDefaultString("plateId", "No value"),
						input.getMessage().payload().getStringValues("names"),
						input.getStatus());
			}
		};
		
		for (MessageBuffer msg : messages) {
			response.add(convertMessageBufferToUpdate.apply(msg));
		}
		
		/* Using memory storage */
//		for (Update update : UpdatesLogic.updates) {
//			if (update.getStatus().equals("pending")) {
//				response.add(update);
//			}
//		}
		
		return response;
	}
	
	@PUT
	@Path("/{updateId}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatus(UpdateStatus updateStatus) {
		
		
		
		return Response.status(200).build();
	}
}