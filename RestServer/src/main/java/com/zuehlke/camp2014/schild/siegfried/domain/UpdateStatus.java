package com.zuehlke.camp2014.schild.siegfried.domain;

import com.google.common.base.Objects;

public class UpdateStatus {
	public UpdateStatus(String status) {
		super();
		this.status = status;
	}

	public UpdateStatus() {
		super();
		this.status = "";
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	String status;
	
	public String toString() {
		return Objects.toStringHelper(this)
				.add("status", getStatus())
				.toString();
	}
}
