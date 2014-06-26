package com.zuehlke.camp2014.iot.brokers.schild.domain;

public class LocationUpdate {
	String locationUpdateId;
	String userId;
	String plateId;

	public LocationUpdate() {
		this.locationUpdateId = "";
		this.userId = "";
		this.plateId = "";
	}

	public LocationUpdate(String LocationUpdateId, String userId, String plateId) {
		super();
		this.locationUpdateId = LocationUpdateId;
		this.userId = userId;
		this.plateId = plateId;
	}

	public String getLocationUpdateId() {
		return locationUpdateId;
	}

	public void setLocationUpdateId(String LocationUpdateId) {
		this.locationUpdateId = LocationUpdateId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPlateId() {
		return plateId;
	}

	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}

	public String toString() {
		return userId + ", " + plateId;
	}

}
