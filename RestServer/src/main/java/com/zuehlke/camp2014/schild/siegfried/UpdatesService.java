package com.zuehlke.camp2014.schild.siegfried;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import jersey.repackaged.com.google.common.base.Preconditions;

import org.elasticsearch.common.base.Function;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.lib.MessageFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.Command;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateStatus;
import com.zuehlke.camp2014.schild.siegfried.logic.UpdatesLogic;

@Path("/updates")
public class UpdatesService {
 
	private Function<MessageBuffer, Update> convertMessageBufferToUpdate = new Function<MessageBuffer, Update>() {
		@Override
		public Update apply(MessageBuffer input) {
			return new Update(
					new Long(input.getSequence()).toString(), 
					input.message().payload().getDefaultString("plateId", "No value"),
					input.getMessage().payload().getStringValues("names"),
					input.getStatus());
		}
	};
	
	private Gson gson = new Gson();
	
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
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory(IdGenerator.COMPONENT_ID).getMessageBufferStore();
		final List<MessageBuffer> messages = store.loadByKey("status", "pending");
		
		
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
	@Produces(MediaType.APPLICATION_JSON)
	public List<Update> updateStatus(@PathParam("updateId") String updateId, UpdateStatus updateStatus) {
		
		Preconditions.checkNotNull(updateId);
		
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory(IdGenerator.COMPONENT_ID).getMessageBufferStore();
		
		List<MessageBuffer> messageBuffers = store.loadByKey("messageId", updateId);
		if (messageBuffers.isEmpty()) {
			return Lists.<Update>newArrayList();
		}
		
		final List<Update> modifiedUpdates = Lists.newArrayList();
		
		for (MessageBuffer msg : messageBuffers) {
			final Update update = convertMessageBufferToUpdate.apply(msg);
			update.setStatus(updateStatus.getStatus());
			
			final String json = gson.toJson(update);
			
			Command updatedMessage = MessageFactory.createCommand(
					new Identifier("camp2014_schild", "siegfried"), 
					Sets.<Identifier>newHashSet(new Identifier("camp2014_schild", update.getPlateId())), 
					update.getUpdateId(), 
					json);
			
			List<MessageBuffer> msgBuffers = MessageFactory.createMessageBuffers(updatedMessage, updateStatus.getStatus(), update.getUpdateId(), Long.parseLong(update.getUpdateId()));
			
			for (MessageBuffer msgBuffer: msgBuffers) {
				store.save(msgBuffer);
			}
			
			modifiedUpdates.add(update);
		}
		
		return modifiedUpdates;
	}
}