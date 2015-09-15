package com.krishagni.catissueplus.core.audit.events;


public class RequestAudit {
	private String entityType;

	private Long entityId;

	private Integer startAt;

	private Integer maxRecs;

	private Boolean detailed;

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Boolean IsDetailed() {
		return detailed;
	}

	public void setDetailed(Boolean detailed) {
		this.detailed = detailed;
	}

	public Integer getStartAt() {
		return startAt;
	}

	public void setStartAt(Integer startAt) {
		this.startAt = startAt;
	}

	public Integer getMaxRecs() {
		return maxRecs;
	}

	public void setMaxRecs(Integer maxRecs) {
		this.maxRecs = maxRecs;
	}
	
	public Boolean getDetailed() {
		return detailed;
	}
}
