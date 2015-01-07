package com.krishagni.catissueplus.core.de.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class RemoveFormContextEvent extends RequestEvent {
	public static enum RemoveType {
		SOFT_REMOVE,
		HARD_REMOVE
	}
	
	private Long formId;
	
	private RemoveType removeType = RemoveType.SOFT_REMOVE;
	
	private String[] entityTypes;
	
	private Long cpId;
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public RemoveType getRemoveType() {
		return removeType;
	}

	public void setRemoveType(RemoveType formType) {
		this.removeType = formType;
	}

	public String[] getEntityTypes() {
		return entityTypes;
	}

	public void setEntityTypes(String[] entityTypes) {
		this.entityTypes = entityTypes;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}
}
