package com.zuehlke.camp2014.schild.siegfried.logic;

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

import jersey.repackaged.com.google.common.collect.Lists;

import org.elasticsearch.common.collect.Sets;

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
		
		
		//fireUpdatesForOldAndNewRoom(oldRoomJSON, newRoomJSON);
		
		fireUpdatesForOldAndNewRoom(
				new Update(null, "42", Lists.newArrayList("mfu"), "pending"), 
				new Update(null, "99", Lists.newArrayList("zgkaba"), "pending")
				);
	}

	private String removeUserFromCurrentDoorPlate(String movingUser) {
		Map oldDoorPlateDocument = getCurrentDoorPlateDocumentForUserId(movingUser);
		String oldDoorPlateId = (String) oldDoorPlateDocument.get("plateId");
		List personsInOldRoom = (List) oldDoorPlateDocument.get("persons");
		personsInOldRoom.remove(movingUser);
		Gson gson = new Gson();
		String oldDoorPlateDocumentUpdateJSON = gson.toJson(oldDoorPlateDocument);
		
		Response updateResponse = target.path("db/room/" + oldDoorPlateId).request().put(Entity.json(oldDoorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
		
		return oldDoorPlateDocumentUpdateJSON;
	}
	
	private Map getCurrentDoorPlateDocumentForUserId(String userId) {
		Response response = target.path("db/room/_search").request().post(Entity.json("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"room.persons\":\"" + userId + "\"}}]}}}"));
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
		
		Response updateResponse = target.path("db/room/" + doorPlateId).request().put(Entity.json(doorPlateDocumentUpdateJSON));
		if (updateResponse.getStatus() != 200) {
			System.out.println(updateResponse);
		}
		return doorPlateDocumentUpdateJSON;
	}
	
	private Map getTargetDoorPlateDocumentForDoorPlateId(String plateId) {
		Response response = target.path("db/room/" + plateId).request().get();
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map result = (Map) gson.fromJson(responseBody, Map.class);
		return (Map) result.get("_source");
	}
	
	private void sendToCloud(Update update) {
		final String newUpdateId = IdGenerator.getNext();
		final Update newUpdate = new Update(
				newUpdateId,
				update.getPlateId(),
				update.getNames(),
				"pending"
				);
		Gson gson = new Gson();
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
		String userId = update.getUserId();
		String plateId = update.getPlateId();
		Map userDocument = getUserDeviceDocumentForUserId(userId);
		String userDocId = "blah"; // TODO
		String doorPlateSerial = update.getPlateId();
		String doorPlateDocId = getDoorPlateDeviceIdForSerialNo(doorPlateSerial);
		System.out.println(target.path("db/telemetrymessage").queryParam("parent", userDocId).request().post(Entity.json(
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
