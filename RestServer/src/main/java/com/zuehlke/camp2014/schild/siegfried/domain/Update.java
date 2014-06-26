package com.zuehlke.camp2014.schild.siegfried.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class Update {
	public Update(String updateId, String plateId, List<String> names, String status) {
		super();
		this.updateId = updateId;
		this.plateId = plateId;
		this.names = Lists.newArrayList(names);
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
		this.names.clear();
		this.names.addAll(names);
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	String updateId;
	String plateId;
	ArrayList<String> names;
	String status;
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("updateId", getUpdateId())
				.add("plateId", getPlateId())
				.add("names", Lists.newArrayList(getNames()))
				.add("status", getStatus())
				.toString();
	}
}
