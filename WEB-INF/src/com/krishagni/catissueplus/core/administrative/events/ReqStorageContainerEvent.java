package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqStorageContainerEvent extends RequestEvent {
	private Long id;
	
	private boolean includeOccupiedPositions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public boolean isIncludeOccupiedPositions() {
		return includeOccupiedPositions;
	}

	public void setIncludeOccupiedPositions(boolean includeOccupiedPositions) {
		this.includeOccupiedPositions = includeOccupiedPositions;
	}
}
