package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.domain.nui.ValidationErrors;
import edu.common.dynamicextensions.domain.nui.ValidationStatus;
import edu.common.dynamicextensions.napi.FormData;

public class FormDataEvent extends ResponseEvent {
	private Long formId;
	
	private Long recordId;
	
	private FormData formData;

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public FormData getFormData() {
		return formData;
	}

	public void setFormData(FormData formData) {
		this.formData = formData;
	}
	
	public static FormDataEvent ok(Long formId, Long recordId, FormData formData) {
		FormDataEvent resp = new FormDataEvent();
		resp.setFormId(formId);
		resp.setRecordId(recordId);
		resp.setFormData(formData);
		resp.setStatus(EventStatus.OK);
		return resp;		
	}
	
	public static FormDataEvent notFound(Long formId, Long recordId) {
		FormDataEvent resp = new FormDataEvent();
		resp.setFormId(formId);
		resp.setRecordId(recordId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;		
	}
	
	public static FormDataEvent badRequest(String message) {
		FormDataEvent resp = new FormDataEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		return resp;
	}
	
	public static FormDataEvent badRequest(ValidationErrors errors) {
		FormDataEvent resp = new FormDataEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage("Form data validation failure");
		
		List<ErroneousField> errorFields = new ArrayList<ErroneousField>();
		for (Map.Entry<String, ValidationStatus> error : errors.getErrors().entrySet()) {
			errorFields.add(new FormFieldError(error.getKey(), error.getValue().name()));
		}
		resp.setErroneousFields(errorFields.toArray(new ErroneousField[0]));
		return resp;
	}
	
	public static FormDataEvent serverError(Exception e) {
		FormDataEvent resp = new FormDataEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
	
	
	public static class FormFieldError extends ErroneousField {
		private String error;
		
		public FormFieldError(String field, String error) {
			super(null, field);
			this.error = error;
		}
		
		@Override
		public String getErrorMessage() {
			return error;
		}
	}
}
