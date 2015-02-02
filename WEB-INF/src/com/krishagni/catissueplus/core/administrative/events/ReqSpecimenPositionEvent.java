package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ReqSpecimenPositionEvent extends RequestEvent {
	private Long posId;
	
	private Long specimenId;

	public Long getPosId() {
		return posId;
	}

	public void setPosId(Long posId) {
		this.posId = posId;
	}

	public Long getSpecimenId() {
		return specimenId;
	}

	public void setSpecimenId(Long specimenId) {
		this.specimenId = specimenId;
	}
	
	

}
