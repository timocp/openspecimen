package com.krishagni.catissueplus.core.audit.domain;

import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.common.util.AuthUtil;


public class CustomRevisionEntity extends BaseEntity{
	@RevisionNumber
	private int identifier;

	@RevisionTimestamp
	private long timestamp;

	private Long userId;
	
	private String action;

	public int getIdentifier() {
		return identifier;
	}

	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public Long getUserId() {
		if (userId == null) {
			setUserId(AuthUtil.getCurrentUser().getId());
		}
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	
	public String getAction() {
		return action;
	}

	
	public void setAction(String action) {
		this.action = action;
	}
	
	
}
