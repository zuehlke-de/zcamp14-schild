package com.zuehlke.doorplate.streamprocessing;

import com.zuehlke.camp2014.iot.brokers.schild.domain.Move;
import com.zuehlke.camp2014.iot.brokers.schild.domain.Update;
import com.zuehlke.camp2014.iot.brokers.schild.domain.UpdateStatus;
import com.zuehlke.camp2014.iot.model.internal.Message;

public class MessageListenerDoorplate {

	public static final String BASE_URI = "http://localhost:9200"; 

	public void passMessage(Message message) {
		
	}

	public void processMove(Move move) {
		
	}
	
	public void processUpdate(Update update) {
		
	}
	
	public void processStatusForUpdateWithId(UpdateStatus status, String updateId) {
		
	}
}
