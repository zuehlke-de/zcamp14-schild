package com.zuehlke.doorplate.streamprocessing;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import com.zuehlke.camp2014.iot.brokers.schild.domain.UpdateLocation;
import com.zuehlke.camp2014.iot.brokers.schild.domain.Move;

@SuppressWarnings({"rawtypes", "unchecked"})
public class MessageListenerDoorplate {

	public static final String BASE_URI = "http://localhost:9200";
	
	private DateFormat dateFormat;
	
	private WebTarget target;
	
	public MessageListenerDoorplate() {
		Client c = ClientBuilder.newClient();
        target = c.target(MessageListenerDoorplate.BASE_URI);
        dateFormat = new SimpleDateFormat("");
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
		String oldRoomJSON = removeUserFromCurrentDoorPlate(movingUser);
		String newRoomJSON = addUserToDoorPlateWithId(movingUser, newDoorPlateSerial);
		fireUpdatesForOldAndNewRoom(oldRoomJSON, newRoomJSON);
	}

	private String removeUserFromCurrentDoorPlate(String movingUser) {
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
		
		return oldDoorPlateDocumentUpdateJSON;
	}
	
	private Map getCurrentDoorPlateDocumentForUserId(String userId) {
		Response response = target.path("schild/room/_search").request().post(Entity.json("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"room.persons\":\"" + userId + "\"}}]}}}"));
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map jsonMap = gson.fromJson(responseBody, Map.class);
		System.out.println(jsonMap);
		List hits =  (List) ((Map) jsonMap.get("hits")).get("hits");
		if (hits.size() >= 1) {
			return (Map) ((Map) hits.get(0)).get("_source");
		}
		
		return generateNewRoom();
	}

	private Map<String, Object> generateNewRoom() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("name", "newRoom");
		result.put("persons", new ArrayList<String>());
		result.put("plateId", "" + Math.random() * 100000);
		return result;
	}

	private String addUserToDoorPlateWithId(String userId, String doorPlateId) {
		Map doorPlateDocument = getTargetDoorPlateDocumentForDoorPlateId(doorPlateId);
		List personsInOldRoom = (List) doorPlateDocument.get("persons");
		personsInOldRoom.add(userId);
		Gson gson = new Gson();
		String doorPlateDocumentUpdateJSON = gson.toJson(doorPlateDocument);
		
		Response updateResponse = target.path("schild/room/" + doorPlateId).request().put(Entity.json(doorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
		return doorPlateDocumentUpdateJSON;
	}
	
	private Map getTargetDoorPlateDocumentForDoorPlateId(String plateId) {
		Response response = target.path("schild/room/" + plateId).request().get();
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map result = (Map) gson.fromJson(responseBody, Map.class);
		return (Map) result.get("_source");
	}
	
	private void fireUpdatesForOldAndNewRoom(String oldRoomJSON, String newRoomJSON) {
		// TODO: Store doorplate updates in DynamoDB
	}
	
	/**
	 * Processes a location update for a user and generates a telemetry message accordingly. 
	 * The update is stored in ElasticSearch.
	 * @param move The move object
	 */
	public void processLocationUpdate(UpdateLocation update) {
		String userId = update.getUserId();
		String plateId = update.getPlateId();
		Map userDocument = getUserDeviceDocumentForUserId(userId);
		String userDocId = "blah"; // TODO
		String doorPlateSerial = update.getPlateId();
		String doorPlateDocId = getDoorPlateDeviceIdForSerialNo(doorPlateSerial);
		System.out.println(target.path("schild/telemetrymessage").queryParam("parent", userDocId).request().post(Entity.json(
				"{\"payload\": \"{'plateId':'" + plateId + "'}\"," +
				"\"senderId\": \"userId\"," +
				"\"timestamp\": \"2014/06/25 16:10:00\"}")));

	}
	
	private Map getUserDeviceDocumentForUserId(String userId) {
		return new HashMap<String, Object>();
	}
	
	private String getDoorPlateDeviceIdForSerialNo(String doorPlateSerial) {
		return "1";
	}
}
