package com.zuehlke.camp2014.schild.siegfried;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.logic.MoveLogic;

@Path("/moves")
public class MoveService {
	
	// TODO: Temporary hard-coded logic, will be moved to spark processing via kafka
	private final MoveLogic logic = new MoveLogic();
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Move put(Move move) {
		
		// TODO: Send to queue
		System.out.println("TODO: Send move to kafka: "+move.toString());
		
		// TODO: Get device id
		// TODO: Provide payload
		//Telemetry msg = new Telemetry(new Date(), new Identifier("schild", "TODO"), null);
		logic.processMoveMessage(move);
				
		return move;
	}
}
