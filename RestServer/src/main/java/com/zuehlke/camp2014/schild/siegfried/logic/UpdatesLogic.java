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
			if (iter.next().getPlateId().equals(plate.getPlateId())) {
				iter.remove();
			}
		}
		
		// construct the update structure
		final String updateId = new Integer(lastUpdateId++).toString();
		final Update update = new Update(
				updateId,
				plate.getPlateId(),
				plate.getNames().toArray(new String[] {}),
				"pending"
				);
		
		// add the update to the list of pending updates
		updates.add(update);
	}
}
