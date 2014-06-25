package com.zuehlke.camp2014.schild.siegfried.domain;

import com.google.common.base.Objects;

public class Update {
	public Update(String updateId, String plateId, String[] names, String status) {
		super();
		this.updateId = updateId;
		this.plateId = plateId;
		this.names = names;
		this.status = status;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	public String[] getNames() {
		return names;
	}
	public void setNames(String[] names) {
		this.names = names;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	String updateId;
	String plateId;
	String[] names;
	String status;
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("updateId", getUpdateId())
				.add("plateId", getPlateId())
				.add("names", getNames().toString())
				.add("status", getStatus())
				.toString();
	}
}
