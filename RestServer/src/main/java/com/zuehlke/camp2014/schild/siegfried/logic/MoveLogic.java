package com.zuehlke.camp2014.schild.siegfried.logic;

//import com.zuehlke.camp2014.iot.core.ComponentFactory;
//import com.zuehlke.camp2014.iot.core.store.KafkaMessageWriter;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;

public class MoveLogic {

	public void processMoveMessage(Move move) {
		System.out.println("Process message "+move);
		
		//KafkaMessageWriter writer = new ComponentFactory("id").newMessageInboxWriter(true);
		
		// TODO: Get domain object
		// TODO: Implement logic
		//move.setMoveId(UUID.randomUUID().toString());
			
		
	}

}
