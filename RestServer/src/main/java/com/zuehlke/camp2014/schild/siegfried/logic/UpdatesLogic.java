package com.zuehlke.camp2014.schild.siegfried.logic;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.elasticsearch.common.collect.Sets;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.lib.MessageFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.Command;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.IdGenerator;
import com.zuehlke.camp2014.schild.siegfried.domain.Plate;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;

public class UpdatesLogic {
public static List<Update> updates = Lists.newArrayList();

	

	public static void triggerUpdate(Plate plate) {
		
		System.out.println("Create update for plate: "+plate.toString());
		
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
		final String newUpdateId = IdGenerator.getNext();
		final Update newUpdate = new Update(
				newUpdateId,
				plate.getPlateId(),
				plate.getNames(),
				"pending"
				);
		
		Gson gson = new Gson();
		String json = gson.toJson(newUpdate);
		
		
		System.out.println("JSON object to be sent: "+json);
		
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory("camp2014").getMessageBufferStore();
		Command msg = MessageFactory.createCommand(
				new Identifier("camp2014_schild", "siegfried"), 
				Sets.newHashSet(new Identifier("camp2014_schild", "42")), 
				newUpdateId, 
				json);
		
		List<MessageBuffer> msgBuffers = MessageFactory.createMessageBuffers(msg, "pending", newUpdateId, Long.parseLong(newUpdateId));
		
		for (MessageBuffer msgBuffer: msgBuffers) {
			store.save(msgBuffer);
		}
		
		// add the update to the list of pending updates
//			updates.add(newUpdate);
//			System.out.println("Added new update: "+newUpdate.toString());
		
	}
}
