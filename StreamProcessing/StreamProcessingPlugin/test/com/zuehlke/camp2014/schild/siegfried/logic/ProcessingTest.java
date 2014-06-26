package com.zuehlke.camp2014.schild.siegfried.logic;

import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.gson.Gson;
import com.zuehlke.camp2014.schild.siegfried.domain.Move;
import com.zuehlke.camp2014.schild.siegfried.domain.UpdateLocation;

public class ProcessingTest extends Assert {
	
	private WebTarget target;
	private MessageListenerDoorplate listener;
	

	@Before
	public void setUp() throws Exception {
		Client c = ClientBuilder.newClient();
        target = c.target(MessageListenerDoorplate.BASE_URI);
        
		String testDevice1 = "{\"groups\": [{\"name\": \"Eschborn\",\"groupId\": \"1\"},{\"name\": \"2. OG\",\"groupId\": \"1.1\"}]," +
				"\"productTypes\": [{\"id\": \"1\",\"name\": \"doorplate\"}]," +
				"\"serialnumber\": \"3342\"}";
		Entity<String> json = Entity.json(testDevice1);
		Response response = target.path("db/device/2337").request().put(json);
		System.out.println(response);
        
		System.out.println(target.path("db/device/2338").request().put(Entity.json("{\"groups\": [{\"name\": \"Eschborn\",\"groupId\": \"1\"},{\"name\": \"4. OG\",\"groupId\": \"1.2\"}]," +
				"\"productTypes\": [{\"id\": \"1\",\"name\": \"doorplate\"}]," +
				"\"serialnumber\": \"3343\"}")));
        
		System.out.println(target.path("db/device/2339").request().put(Entity.json("{\"groups\": []," +
				"\"productTypes\": [{\"id\": \"2\",\"name\": \"mobile\"}]," +
				"\"serialnumber\": \"hinz\"}")));
        
		System.out.println(target.path("db/device/2340").request().put(Entity.json("{\"groups\": []," +
				"\"productTypes\": [{\"id\": \"2\",\"name\": \"mobile\"}]," +
				"\"serialnumber\": \"kunz\"}")));
		
		System.out.println(target.path("db/room/3342").request().put(Entity.json("{\"name\": \"Chagall\",\"plateId\":\"3342\",\"persons\": [\"hinz\",\"kunz\"]}")));
		System.out.println(target.path("db/room/3343").request().put(Entity.json("{\"name\": \"Miro\",\"plateId\":\"3343\",\"persons\": []}")));
		
//		System.out.println(target.path("db/telemetrymessage").queryParam("parent", "1").request().post(Entity.json("{\"payload\": \"[{'id':'mfu', 'name':'Masanori Fujita'}, {'id':'sat', 'name':'Michael Sattler'}]\"," +
//				"\"senderId\": \"somebody\"," +
//				"\"timestamp\": \"2014/06/25 16:10:00\"}")));
		
		listener = new MessageListenerDoorplate();
		Thread.sleep(1500L);
	}

	@After
	public void tearDown() throws Exception {
		System.out.println(target.path("db/device/2337").request().delete());
		System.out.println(target.path("db/device/2338").request().delete());
		System.out.println(target.path("db/device/2339").request().delete());
		System.out.println(target.path("db/device/2340").request().delete());
		System.out.println(target.path("db/room/3342").request().delete());
		System.out.println(target.path("db/room/3343").request().delete());
		System.out.println(target.path("db/telemetrymessage/_query").queryParam("q", "senderId:somebody").request().delete());

		target = null;
		listener = null;
	}

	@Test
	public void testProcessMove() {
		Move move = new Move();
		move.setMoveId("1");
		move.setUserId("hinz");
		move.setPlateId("3343");
		System.out.println("Processing move");
		listener.processMove(move);
    	
		assertPersonInRoom("kunz", "3342");
		assertPersonInRoom("hinz", "3343");
		
		System.out.println("Move processed");
	}

	private void assertPersonInRoom(String person, String doorPlateSerial) {
		Response response = target.path("db/room/_search").request().post(Entity.json("{\"query\":{\"bool\":{\"must\":[{\"term\":{\"room.persons\":\"" + person + "\"}}]}}}"));
		assertPersonCheckedIn(response, person);
	}

	@SuppressWarnings("rawtypes")
	private void assertPersonCheckedIn(Response response, String name) {
		String responseBody = response.readEntity(String.class);
		Gson gson = new Gson();
		Map jsonMap = gson.fromJson(responseBody, Map.class);
		System.out.println(jsonMap);
//		System.out.println("Hits: " + ((Map) jsonMap.get("hits")).get("total"));
		List hits =  (List) ((Map) jsonMap.get("hits")).get("hits");
//		System.out.println("Hits array: " + hits);
		assertTrue(hits.size() >= 1);
		Map source = (Map) ((Map) hits.get(0)).get("_source");
		List personsList = (List) source.get("persons");
//		System.out.println("Payload: " + payloadArray);
		assertTrue(personsList.contains(name));
	}

	public void testProcessLocationUpdate() {
//		UpdateLocation update = new UpdateLocation();
//		update.setPlateId("43");
//		update.setUserId("sat");
//		
//		System.out.println("Processing location update");
//		
//		listener.processLocationUpdate(update);
//		
//		assertPersonCurrentlyInRoom(update.getUserId(), update.getPlateId());
//		
//		System.out.println("Location update processed");
	}

	private void assertPersonCurrentlyInRoom(String person, String doorPlateSerial) {
		Response response = target.path("db/telemetrymessage/_search").request().post(Entity.json("{\"query\": {" +
				"\"bool\": {\"must\": [" +
				"{\"has_parent\" : {\"parent_type\" :\"device\", \"query\" : {\"match\" : {\"serialnumber\" : \"" + doorPlateSerial + "\"}}} }," +
				"{\"range\" : {\"timestamp\" : {\"gt\" : \"2014/06/25 16:10:00\"}}}]}}}"));
		System.out.println(response);
		fail("Not yet implemented");
//		assertPersonCheckedIn(response, person);
	}
}
