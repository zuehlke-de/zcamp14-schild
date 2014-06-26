package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.zuehlke.camp2014.iot.core.ComponentFactory;
import com.zuehlke.camp2014.iot.core.lib.MessageFactory;
import com.zuehlke.camp2014.iot.core.store.DynamoDBStore;
import com.zuehlke.camp2014.iot.model.Identifier;
import com.zuehlke.camp2014.iot.model.internal.Command;
import com.zuehlke.camp2014.iot.model.internal.MessageBuffer;
import com.zuehlke.camp2014.schild.siegfried.IdGenerator;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.Update;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateLocation;


@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageListenerDoorplate {

	public static final String BASE_URI = "http://ec2-54-74-5-94.eu-west-1.compute.amazonaws.com:9200";
	
	private Gson gson = new Gson();
	
	private WebTarget target;
	
	public MessageListenerDoorplate() {
		Client c = ClientBuilder.newClient();
        target = c.target(MessageListenerDoorplate.BASE_URI);
	}

	/**
	 * Processes a move event that removes a user from one door plate's persons list
	 * and adds him to another's. The update is stored in ElasticSearch and update 
	 * messages generated in Dynamo DB.
	 * @param move The move object
	 */
	public void processMove(Move move) {
		String movingUser = move.getUserId();
		String newDoorPlateSerial = move.getPlateId();
		String oldRoomJSON = removeUserFromCurrentDoorPlateWithDocType(movingUser, "room");
		String newRoomJSON = addUserToDoorPlateWithIdAndDocType(movingUser, newDoorPlateSerial, "room");
		fireUpdatesForOldAndNewRoom(getUpdateFromRoomJSON(oldRoomJSON), getUpdateFromRoomJSON(newRoomJSON));
	}

	private String removeUserFromCurrentDoorPlateWithDocType(String movingUser, String docType) {
		Map oldDoorPlateDocument = getCurrentDoorPlateDocumentForUserIdWithDocType(movingUser, docType);
		if (oldDoorPlateDocument == null) {
			return null;
		}
		String oldDoorPlateId = (String) oldDoorPlateDocument.get("plateId");
		List personsInOldRoom = (List) oldDoorPlateDocument.get("persons");
		personsInOldRoom.remove(movingUser);
		String oldDoorPlateDocumentUpdateJSON = gson.toJson(oldDoorPlateDocument);
		
		Response updateResponse = target.path("db/" + docType + "/" + oldDoorPlateId).request().put(Entity.json(oldDoorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
		
		return oldDoorPlateDocumentUpdateJSON;
	}
	
	private Map getCurrentDoorPlateDocumentForUserIdWithDocType(String userId, String docType) {
		Response response = target.path("db/" + docType + "/_search").request().post(Entity.json("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"" + docType + ".persons\":\"" + userId + "\"}}]}}}"));
		String responseBody = response.readEntity(String.class);
		Map jsonMap = gson.fromJson(responseBody, Map.class);
		System.out.println(jsonMap);
		List hits =  (List) ((Map) jsonMap.get("hits")).get("hits");
		if (hits.size() >= 1) {
			return (Map) ((Map) hits.get(0)).get("_source");
		}
		
		return null;
	}

	private String addUserToDoorPlateWithIdAndDocType(String userId, String doorPlateId, String docType) {
		Map doorPlateDocument = getTargetDoorPlateDocumentForDoorPlateIdWithDocType(doorPlateId, docType);
		List personsInOldRoom = (List) doorPlateDocument.get("persons");
		personsInOldRoom.add(userId);
		String doorPlateDocumentUpdateJSON = gson.toJson(doorPlateDocument);
		
		Response updateResponse = target.path("db/" + docType + "/" + doorPlateId).request().put(Entity.json(doorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
		return doorPlateDocumentUpdateJSON;
	}
	
	private Map getTargetDoorPlateDocumentForDoorPlateIdWithDocType(String plateId, String docType) {
		Response response = target.path("db/" + docType + "/" + plateId).request().get();
		if (response.getStatus() == 200) {
			String responseBody = response.readEntity(String.class);
			Map result = (Map) gson.fromJson(responseBody, Map.class);
			return (Map) result.get("_source");
		}
		return generateNewRoomWithId(plateId);
	}

	private Map<String, Object> generateNewRoomWithId(String roomId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", "Room " + roomId);
		result.put("persons", new ArrayList<String>());
		result.put("plateId", roomId);
		return result;
	}

	private Update getUpdateFromRoomJSON(String roomJSON) {
		if (roomJSON == null) {
			return null;
		}
		Map roomMap = gson.fromJson(roomJSON, Map.class);
		List persons = (List) roomMap.get("persons");
		return new Update(null, (String) roomMap.get("plateId"), persons, null);
	}
	
	private void sendToCloud(Update update) {
		final String newUpdateId = IdGenerator.getNext();
		final Update newUpdate = new Update(
				newUpdateId,
				update.getPlateId(),
				update.getNames(),
				"pending"
				);
		final String json = gson.toJson(newUpdate);
		
		DynamoDBStore<Identifier, MessageBuffer> store = new ComponentFactory(IdGenerator.COMPONENT_ID).getMessageBufferStore();
		Command oldRoomMessage = MessageFactory.createCommand(
				new Identifier("camp2014_schild", "siegfried"), 
				Sets.<Identifier>newHashSet(new Identifier("camp2014_schild", newUpdate.getPlateId())), 
				newUpdateId, 
				json);
		
		List<MessageBuffer> msgBuffers = MessageFactory.createMessageBuffers(oldRoomMessage, "pending", newUpdateId, Long.parseLong(newUpdateId));
		
		for (MessageBuffer msgBuffer: msgBuffers) {
			store.save(msgBuffer);
		}
		
	}	
	
	private void fireUpdatesForOldAndNewRoom(Update oldRoom, Update newRoom) {
		sendToCloud(oldRoom);
		sendToCloud(newRoom);
	}
	
	/**
	 * Processes a location update for a user and generates a telemetry message accordingly. 
	 * The update is stored in ElasticSearch.
	 * @param move The move object
	 */
	public void processLocationUpdate(UpdateLocation update) {
		String movingUser = update.getUserId();
		String newDoorPlateSerial = update.getPlateId();
		removeUserFromCurrentDoorPlateWithDocType(movingUser, "location");
		addUserToDoorPlateWithIdAndDocType(movingUser, newDoorPlateSerial, "location");
	}
}
