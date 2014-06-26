package com.zuehlke.camp2014.iot.brokers.schild.domain;

public class UpdateLocation {
	String userId;
	String plateId;

	public UpdateLocation() {
		this.userId = "";
		this.plateId = "";
	}

	public UpdateLocation(String userId, String plateId) {
		super();
		this.userId = userId;
		this.plateId = plateId;
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
