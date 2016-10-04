package com.krishagni.catissueplus.core.audit.events;

import java.util.Date;

public class GetRevisionsOp {
	private String entityName;

	private Long entityId;

	private Date from;

	private Date to;

	private int maxRevs = 50;

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Date getFrom() {
		return from;
	}

	public void setFrom(Date from) {
		this.from = from;
	}

	public Date getTo() {
		return to;
	}

	public void setTo(Date to) {
		this.to = to;
	}

	public int getMaxRevs() {
		return maxRevs;
	}

	public void setMaxRevs(int maxRevs) {
		this.maxRevs = maxRevs;
	}
}
