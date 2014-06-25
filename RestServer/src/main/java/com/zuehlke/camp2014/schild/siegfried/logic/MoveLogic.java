package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.List;

import org.elasticsearch.common.collect.Lists;

import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;

public class MoveLogic {

	private static int moveIdCounter = 0;
	public static List<Plate> plates = Lists.newArrayList();
	{
		plates.add(new Plate("42", Lists.<String>newArrayList()));
		plates.add(new Plate("43", Lists.<String>newArrayList()));
	}

	public void processMoveMessage(Move move) {
		System.out.println("Process message "+move);

		for (Plate plate : plates) {
			if (plate.getPlateId().equals(move.getPlateId())) {
				if (!plate.getNames().contains(move.getUserId())) {
					
					System.out.println(plate.toString());
					// Update the in-memory plate object
					plate.getNames().add(move.getUserId());
					System.out.println(plate.toString());
					
					UpdatesLogic.triggerUpdate(plate);

					move.setMoveId(new Integer(moveIdCounter++).toString());

				}
			}

		}

	}
}
