package com.zuehlke.camp2014.schild.siegfried.domain;

import java.util.List;

import org.elasticsearch.common.base.Objects;

public class Plate {
	String plateId;
	List<String> names;
	public String getPlateId() {
		return plateId;
	}
	public void setPlateId(String plateId) {
		this.plateId = plateId;
	}
	public List<String> getNames() {
		return names;
	}
	public void addName(String names) {
		this.names.add(names);
	}
	public Plate(String plateId, List<String> names) {
		super();
		this.plateId = plateId;
		this.names = names;
	}
	public String toString() {
		return Objects.toStringHelper(this)
		.add("plateId", getPlateId())
		.add("names", getNames())
		.toString();
	}
}
