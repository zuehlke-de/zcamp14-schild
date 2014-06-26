package com.zuehlke.doorplate.streamprocessing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.zuehlke.camp2014.iot.brokers.schild.domain.Move;
import com.zuehlke.camp2014.iot.brokers.schild.domain.Update;
import com.zuehlke.camp2014.iot.brokers.schild.domain.UpdateStatus;
import com.zuehlke.camp2014.iot.model.internal.Message;

public class MessageListenerDoorplate {

	public static final String BASE_URI = "http://localhost:9200"; 
	
	private WebTarget target;
	
	public MessageListenerDoorplate() {
		Client c = ClientBuilder.newClient();
        target = c.target(MessageListenerDoorplate.BASE_URI);
	}

	public void processMove(Move move) {
		String movingUser = move.getUserId();
		String newDoorPlateSerial = move.getPlateId();
		removeUserFromCurrentDoorPlate(movingUser);
		addUserToDoorPlateWithId(movingUser, newDoorPlateSerial);
		
		// TODOs:
		// Put Updates for old and new data in Dynamo DB
//		System.out.println(target.path("schild/telemetrymessage").queryParam("parent", "1").request().post(Entity.json("{\"payload\": \"[{'id':'mfu', 'name':'Masanori Fujita'}, {'id':'sat', 'name':'Michael Sattler'}]\"," +
//				"\"senderId\": \"somebody\"," +
//				"\"timestamp\": \"2014/06/25 16:10:00\"}")));
	}

	private void removeUserFromCurrentDoorPlate(String movingUser) {
		Map oldDoorPlateDocument = getCurrentDoorPlateDocumentForUserId(movingUser);
		String oldDoorPlateId = (String) oldDoorPlateDocument.get("plateId");
		List personsInOldRoom = (List) oldDoorPlateDocument.get("persons");
		personsInOldRoom.remove(movingUser);
		Gson gson = new Gson();
		String oldDoorPlateDocumentUpdateJSON = gson.toJson(oldDoorPlateDocument);
		
		Response updateResponse = target.path("schild/room/" + oldDoorPlateId).request().put(Entity.json(oldDoorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
	}
	
	private Map getCurrentDoorPlateDocumentForUserId(String userId) {
		Response response = target.path("schild/room/_search").request().post(Entity.json("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"room.persons\":\"" + userId + "\"}}]}}}"));
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map jsonMap = gson.fromJson(responseBody, Map.class);
		System.out.println(jsonMap);
//		System.out.println("Hits: " + ((Map) jsonMap.get("hits")).get("total"));
		List hits =  (List) ((Map) jsonMap.get("hits")).get("hits");
//		System.out.println("Hits array: " + hits);
		if (hits.size() >= 1) {
			return (Map) ((Map) hits.get(0)).get("_source");
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", "newRoom");
		result.put("persons", new ArrayList<String>());
		result.put("plateId", "" + Math.random() * 100000);
		return result;
	}

	private void addUserToDoorPlateWithId(String userId, String doorPlateId) {
		Map doorPlateDocument = getTargetDoorPlateDocumentForDoorPlateId(doorPlateId);
		List personsInOldRoom = (List) doorPlateDocument.get("persons");
		personsInOldRoom.add(userId);
		Gson gson = new Gson();
		String doorPlateDocumentUpdateJSON = gson.toJson(doorPlateDocument);
		
		Response updateResponse = target.path("schild/room/" + doorPlateId).request().put(Entity.json(doorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
	}
	
	private Map getTargetDoorPlateDocumentForDoorPlateId(String plateId) {
		Response response = target.path("schild/room/" + plateId).request().get();
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map result = (Map) gson.fromJson(responseBody, Map.class);
		return (Map) result.get("_source");
	}
	
	private String getCurrentDoorPlateDeviceIdForUserId(String userId) {
		// TODO: Mock
		return getDoorPlateDeviceIdForSerialNo("42");
	}
	
	private String getDoorPlateDeviceIdForSerialNo(String doorPlateSerial) {
		// TODO: Mock
		return doorPlateSerial == "42" ? "1" : "2";
	}
	
	public void processUpdate(Update update) {
		
	}
	
	public void processStatusForUpdateWithId(UpdateStatus status, String updateId) {
		
	}
}
