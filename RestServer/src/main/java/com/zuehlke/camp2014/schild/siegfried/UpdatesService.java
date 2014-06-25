package com.zuehlke.camp2014.schild.siegfried;
 
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.common.collect.Lists;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateStatus;

@Path("/updates")
public class UpdatesService {
 
	@GET
	@Path("/")
	public Response getAll() {
 
		String output = "Jersey say : Hello world";
 
		return Response.status(200).entity(output).build();
 
	}
	
	@GET
	@Path("/pending")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Update> getPending() {
		
		String[] names = new String[2];
		names[0] = "Dieter";
		names[1] = "Detlef";
		
		return Lists.newArrayList(new Update("1234", "42", names, "pending"));
	}
	
	@PUT
	@Path("/{updateId}/status")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateStatus(UpdateStatus updateStatus) {
		return Response.status(200).build();
	}
}