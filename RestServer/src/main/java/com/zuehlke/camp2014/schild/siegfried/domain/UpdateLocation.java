package com.zuehlke.camp2014.schild.siegfried.domain;

import com.google.common.base.Objects;

public class UpdateLocation {
	public UpdateLocation() {
		super();
		this.plateId = "";
	}
	
	public UpdateLocation(String plateId) {
		super();
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

	String plateId;
	String userId;
	
	public String toString() {
		return Objects.toStringHelper(this)
		.add("plateId", plateId)
		.add("userId", userId)
		.toString();
	}
}
