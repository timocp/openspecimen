package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class AllocateSpecimenPositionEvent extends RequestEvent {
	private SpecimenPositionDetail position;

	public SpecimenPositionDetail getPosition() {
		return position;
	}

	public void setPosition(SpecimenPositionDetail position) {
		this.position = position;
	}	
}
