package com.zuehlke.camp2014.schild.siegfried.domain;

import java.util.List;

import com.google.common.base.Objects;

public class Update {
	public Update(String updateId, String plateId, List<String> names, String status) {
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
	public List<String> getNames() {
		return names;
	}
	public void setNames(List<String> names) {
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
	List<String> names;
	String status;
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("updateId", getUpdateId())
				.add("plateId", getPlateId())
				.add("names", getNames())
				.add("status", getStatus())
				.toString();
	}
	
	private  String convertToString(List<String> input) {
		if (input.size() > 0) {
		    StringBuilder nameBuilder = new StringBuilder();
		    for (String n : input) {
		        nameBuilder.append(n).append("',");
		    }

		    nameBuilder.deleteCharAt(nameBuilder.length() - 1);

		    return nameBuilder.toString();
		} else {
		    return "";
		}
	}
	
	private  String convertToString(String[] input) {
		if (input.length > 0) {
		    StringBuilder nameBuilder = new StringBuilder();
		    for (String n : input) {
		        nameBuilder.append(n).append("',");
		    }

		    nameBuilder.deleteCharAt(nameBuilder.length() - 1);

		    return nameBuilder.toString();
		} else {
		    return "";
		}
	}
}
