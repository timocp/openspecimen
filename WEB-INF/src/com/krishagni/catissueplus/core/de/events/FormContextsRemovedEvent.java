package com.krishagni.catissueplus.core.de.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class FormContextsRemovedEvent extends ResponseEvent {
	private Long formId;
	
	private List<FormContextDetail> formCtxts;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<FormContextDetail> getFormCtxts() {
		return formCtxts;
	}

	public void setFormCtxts(List<FormContextDetail> formCtxts) {
		this.formCtxts = formCtxts;
	}

	public static FormContextsRemovedEvent ok(Long formId, List<FormContextDetail> formCtxts) {
		FormContextsRemovedEvent resp = new FormContextsRemovedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setFormId(formId);
		resp.setFormCtxts(formCtxts);
		return resp;
	}

	public static FormContextsRemovedEvent notAuthorized() {
		FormContextsRemovedEvent resp = new FormContextsRemovedEvent();
		resp.setStatus(EventStatus.NOT_AUTHORIZED);
		return resp;
	}
	
	public static FormContextsRemovedEvent badRequest(String message, Throwable t) {
		return errorResp(EventStatus.BAD_REQUEST, message, t);
	}

	public static FormContextsRemovedEvent serverError(String message, Throwable t) {
		return errorResp(EventStatus.INTERNAL_SERVER_ERROR, message, t);
	}

	private static FormContextsRemovedEvent errorResp(EventStatus status, String message, Throwable t) {
		FormContextsRemovedEvent resp = new FormContextsRemovedEvent();
		resp.setStatus(status);
		resp.setMessage(message);
		resp.setException(t);
		return resp;
	}
}
