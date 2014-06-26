package com.zuehlke.camp2014.iot.brokers.schild.domain;

public class UpdateStatus {
	public UpdateStatus(String status) {
		super();
		this.status = status;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	String status;
}
