package com.zuehlke.camp2014.schild.siegfried.domain;

import com.google.common.base.Objects;

public class Move {
	public Move() {
		this.moveId = "";
		this.userId = "";
		this.plateId = "";
	}
	public Move(String moveId, String userId, String plateId) {
		super();
		this.moveId = moveId;
		this.userId = userId;
		this.plateId = plateId;
	}
	
	public String getMoveId() {
		return moveId;
	}
	public void setMoveId(String moveId) {
		this.moveId = moveId;
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
	String moveId;
	String userId;
	String plateId;
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("moveId", getMoveId())
				.add("userId", getUserId())
				.add("plateId", getPlateId())
				.toString();
	}
}
