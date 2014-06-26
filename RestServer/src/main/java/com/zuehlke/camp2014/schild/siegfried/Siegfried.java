package com.zuehlke.camp2014.schild.siegfried;

import org.glassfish.jersey.server.ResourceConfig;

import com.zuehlke.camp2014.schild.siegfried.logic.MessageListenerDoorplate;

public class Siegfried extends ResourceConfig {
	
	public static MessageListenerDoorplate messageListenerDoorplate = new MessageListenerDoorplate();
	
	public Siegfried() {
        packages("com.zuehlke.camp2014.schild.siegfried");
    }
}
