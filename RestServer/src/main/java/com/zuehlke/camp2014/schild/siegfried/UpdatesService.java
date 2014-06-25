package com.zuehlke.camp2014.schild.siegfried;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
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
	public Iterable<Update> getPending() {
		
		return Iterables.filter(updates, new Predicate<Update>() {

			public boolean apply(Update input) {
				return "pending".equals(input.getStatus());
			}
			
		});
		
	}
	
	@PUT
	@Path("/{updateId}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatus(UpdateStatus updateStatus) {
		
		
		
		return Response.status(200).build();
	}
}