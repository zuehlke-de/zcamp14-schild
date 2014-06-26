package com.zuehlke.camp2014.schild.siegfried.logic;

import com.zuehlke.camp2014.schild.siegfried.domain.UpdateLocation;

public class LocationLogic {
	public UpdateLocation processLocationUpdate(String userId, UpdateLocation updateLocation) {
		
		System.out.println("Process location update for user: "+updateLocation);
		
		return updateLocation;
	}
}
