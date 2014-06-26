package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.UUID;

import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;

public class MoveLogic {

	public PlateRepo plateRepo = new PlateRepoMemoryOnly();
	
	public void processMoveMessage(Move move) {
		System.out.println("Process message "+move);
		
		for (Plate plate : plateRepo.getPlates()) {
			if (plate.getPlateId().equals(move.getPlateId())) {
				if (!plate.getNames().contains(move.getUserId())) {
					
					DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory("camp2014").getMessageBufferStore();
					
					MessageBuffer messageBuffer = new MessageBuffer();
					
					plate.getNames().add(move.getUserId());
					System.out.println("Updated plate: "+plate.toString());
				}
				
			}
		}
		
		// TODO: Get domain object
		// TODO: Implement logic
		move.setMoveId(UUID.randomUUID().toString());
			
		
	}

}
