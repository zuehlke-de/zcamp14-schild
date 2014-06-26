package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.Iterator;

import com.zuehlke.camp2014.schild.siegfried.PlatesService;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;

public class MoveLogic {

	private static int moveIdCounter = 0;
	
	public void processMoveMessage(Move move) {
		System.out.println("Process move "+move);

		for (final Plate plate : PlatesService.plates) {
			
			// find matching plate to add name
			if (plate.getPlateId().equals(move.getPlateId())) {
				System.out.println("Found matching plate: "+plate.toString());
				if (!plate.getNames().contains(move.getUserId())) {
					
					
					// Update the in-memory plate object
					plate.getNames().add(move.getUserId());
					System.out.println(plate.toString());
					
					UpdatesLogic.triggerUpdate(plate);

					move.setMoveId(new Integer(moveIdCounter++).toString());

				}
			}

			// find plate that the user is currently registered for
//			Iterator<String> iter = plate.getNames().iterator();
//			while (iter.hasNext()) {
//				if (iter.next().equals(move.getUserId())) {
//					System.out.println("Found matching plate user is registered for: "+plate.toString());
//					
//					// Update the in-memory plate object
//					plate.getNames().remove(move.getUserId());
//					
//					UpdatesLogic.triggerUpdate(plate);
//				}
//			}
		}
	}
}
