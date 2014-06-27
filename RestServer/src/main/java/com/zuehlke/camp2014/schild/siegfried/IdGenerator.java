package com.zuehlke.camp2014.schild.siegfried;


public class IdGenerator {
	private static long lastUpdateId = 1;
	
	public static final String COMPONENT_ID = "schild2";
	
	public static String getNext() {
		return new Long(lastUpdateId++).toString();
	}
}
