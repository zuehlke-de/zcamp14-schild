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

	public String getPlateId() {
		return plateId;
	}

	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}

	String plateId;
	
	public String toString() {
		return Objects.toStringHelper(this)
		.add("plateId", plateId)
		.toString();
	}
}
