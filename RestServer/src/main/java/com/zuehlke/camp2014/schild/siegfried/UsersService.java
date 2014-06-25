package com.zuehlke.camp2014.schild.siegfried;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.zuehlke.camp2014.schild.siegfried.domain.UpdateLocation;
import com.zuehlke.camp2014.schild.siegfried.logic.LocationLogic;

@Path("/users")
public class UsersService {

	final LocationLogic locationLogic = new LocationLogic();
	
	@POST
	@Path("/{userId}/location")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public UpdateLocation updateLocation(@PathParam("userId") String userId, UpdateLocation updateLocation) {
		
		locationLogic.processLocationUpdate(userId, updateLocation);
				
		return updateLocation;
	}
}
