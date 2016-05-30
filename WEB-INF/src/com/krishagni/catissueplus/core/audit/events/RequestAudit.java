package com.krishagni.catissueplus.core.audit.events;


public class RequestAudit {
	
	private String objectType;

	private Long objectId;
	
	public RequestAudit(String objectType, Long objectId) {
		this.objectType = objectType;
		this.objectId = objectId;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}
}
