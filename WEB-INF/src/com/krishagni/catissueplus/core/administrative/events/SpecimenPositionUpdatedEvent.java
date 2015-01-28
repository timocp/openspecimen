package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenPositionUpdatedEvent extends ResponseEvent {
	private SpecimenPositionDetail position;

	public SpecimenPositionDetail getPosition() {
		return position;
	}

	public void setPosition(SpecimenPositionDetail position) {
		this.position = position;
	}
	
	public static SpecimenPositionUpdatedEvent ok(SpecimenPositionDetail detail) {
		SpecimenPositionUpdatedEvent resp = new SpecimenPositionUpdatedEvent();
		resp.setPosition(detail);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static SpecimenPositionUpdatedEvent badRequest(Exception e) {
		SpecimenPositionUpdatedEvent resp = new SpecimenPositionUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		if (e instanceof ObjectCreationException) {
			resp.setErroneousFields(((ObjectCreationException)e).getErroneousFields());
		}
		
		return resp;
	}
	
	public static SpecimenPositionUpdatedEvent serverError(Exception e) {
		SpecimenPositionUpdatedEvent resp = new SpecimenPositionUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
