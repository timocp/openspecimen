package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenPositionEvent extends ResponseEvent {
	private SpecimenPositionDetail position;

	public SpecimenPositionDetail getPosition() {
		return position;
	}

	public void setPosition(SpecimenPositionDetail position) {
		this.position = position;
	}
	
	public static SpecimenPositionEvent ok(SpecimenPositionDetail position) {
		SpecimenPositionEvent resp = new SpecimenPositionEvent();
		resp.setStatus(EventStatus.OK);
		resp.setPosition(position);
		return resp;
	}
	
	public static SpecimenPositionEvent notFound() {
		SpecimenPositionEvent resp = new SpecimenPositionEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
	
	public static SpecimenPositionEvent serverError(Exception e) {
		SpecimenPositionEvent resp = new SpecimenPositionEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;		
	}
}
