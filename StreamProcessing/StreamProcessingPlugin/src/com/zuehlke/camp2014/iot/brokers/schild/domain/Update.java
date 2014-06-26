package com.zuehlke.camp2014.iot.brokers.schild.domain;

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
}
