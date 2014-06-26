package com.zuehlke.camp2014.schild.siegfried;

import java.util.Date;

public class IdGenerator {
	private static long lastUpdateId = new Date().getTime();
	
	public static final String COMPONENT_ID = "schild2";
	
	public static String getNext() {
		return new Long(lastUpdateId++).toString();
	}
}
