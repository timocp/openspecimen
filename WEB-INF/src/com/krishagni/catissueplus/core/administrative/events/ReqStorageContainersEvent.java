package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ReqStorageContainersEvent extends ResponseEvent {
	
	private String name;

	private int maxResults;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
