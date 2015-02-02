package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ReqStorageContainersEvent extends ResponseEvent {
	
	private String name;
	
	private Long specimenId;
	
	private boolean onlyFreeContainers;

	private int maxResults;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}

	public boolean isOnlyFreeContainers() {
		return onlyFreeContainers;
	}

	public void setOnlyFreeContainers(boolean onlyFreeContainers) {
		this.onlyFreeContainers = onlyFreeContainers;
	}

	public int getMaxResults() {
		return maxResults;
	}

	public void setMaxResults(int maxResults) {
		this.maxResults = maxResults;
	}

}
