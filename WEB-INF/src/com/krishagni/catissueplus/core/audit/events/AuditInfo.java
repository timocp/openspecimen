package com.krishagni.catissueplus.core.audit.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;


public class AuditInfo {
	private Long objectId;

  private Date createdOn;

  private UserSummary createdBy;

  private Date lastUpdatedOn;

  private UserSummary lastUpdatedBy;

	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}
	
	public UserSummary getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserSummary createdBy) {
		this.createdBy = createdBy;
	}

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
	}

	public UserSummary getLastUpdatedBy() {
		return lastUpdatedBy;
	}

	public void setLastUpdatedBy(UserSummary lastUpdatedBy) {
		this.lastUpdatedBy = lastUpdatedBy;
	}
}
