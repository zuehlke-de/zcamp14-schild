package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.Iterator;
import java.util.List;

import com.google.common.collect.Lists;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;

public class UpdatesLogic {
public static List<Update> updates = Lists.newArrayList();

	private static int lastUpdateId = 0;

	public static void triggerUpdate(Plate plate) {
		
		// remove pending updates from the updates list
		Iterator<Update> iter = updates.iterator();
		while (iter.hasNext()) {
			final Update currentUpdate = iter.next();
			if (currentUpdate.getPlateId().equals(plate.getPlateId())) {
				System.out.println("Remove outdated update: "+currentUpdate.toString());
				iter.remove();
			}
		}
		
		// construct the update structure
		final String newUpdateId = new Integer(lastUpdateId++).toString();
		final Update newUpdate = new Update(
				newUpdateId,
				plate.getPlateId(),
				plate.getNames(),
				"pending"
				);
		
		// add the update to the list of pending updates
		updates.add(newUpdate);
		System.out.println("Added new update: "+newUpdate.toString());
	}
}
