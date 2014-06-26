package com.zuehlke.camp2014.schild.siegfried;
 
import java.util.Iterator;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.elasticsearch.common.base.Function;

import scala.reflect.Manifest;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.DomainObject;
import com.zuehlke.camp2014.iot.model.DomainObject.*;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateStatus;

@Path("/updates")
public class UpdatesService {
 
	public List<Update> updates = Lists.newArrayList();
	{
		updates.add(new Update("1", "42", new String[] {"Dieter", "Detlef"}, "pending"));
		updates.add(new Update("2", "43", new String[] {"Carlo"}, "pending"));
	}
	
	@GET
	@Path("/")
	public List<Update> getAll() {
		return Lists.newArrayList(updates);
	}
	
	@GET
	@Path("/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Update> getPending() {
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory("camp2014").getMessageBufferStore();
		
		List<MessageBuffer> result = store.loadByKey("status", "UNKNOWN");
		
		List<Update> response = Lists.newArrayList();
		
//		DomainObject.fr
//		for (MessageBuffer msg: result) {
//			msg.getDomain()
//			
//			response.add(new Update(msg.getSequence(), "TODO", new String[] {"A", "B"}, "TODO"));
//		}
//		return response;
		
//		return Lists.transform(result, new Function<MessageBuffer, Update>() {
//
//			@Override
//			public Update apply(MessageBuffer input) {
//				Update result = new Update(input.getMessageId(), "TODO", new String[] {"A", "B"}, "TODO");
//			}
//			
//		});
		return response;
	}
	
	@PUT
	@Path("/{updateId}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatus(UpdateStatus updateStatus) {
		
		
		
		return Response.status(200).build();
	}
}