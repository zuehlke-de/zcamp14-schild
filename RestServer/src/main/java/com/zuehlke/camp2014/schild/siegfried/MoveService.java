package com.zuehlke.camp2014.schild.siegfried;

import java.util.Date;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.store.KafkaMessageWriter;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.Telemetry;
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
		
		/*
		KafkaMessageWriter writer = new ComponentFactory("id").newMessageInboxWriter(true);
		Identifier identifier = new Identifier("schild", "TODO");
		Telemetry msg = new Telemetry(new Date(), identifier, null, null);
		
		writer.save(identifier, msg);
		*/
		
		Siegfried.messageListenerDoorplate.processMove(move);
				
		return move;
	}
}
