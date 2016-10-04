package com.krishagni.catissueplus.core.audit.events;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.UserSummary;

public class RevisionInfo {
	private Long id;

	private UserSummary user;

	private String ipAddress;

	private Date time;

	private String op;

	private String entityType;

	private Long entityId;

	private String entityKey;

	private String entityKeyValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
		this.user = user;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getOp() {
		return op;
	}

	public void setOp(String op) {
		this.op = op;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityKey() {
		return entityKey;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public void setEntityKey(String entityKey) {
		this.entityKey = entityKey;
	}

	public String getEntityKeyValue() {
		return entityKeyValue;
	}

	public void setEntityKeyValue(String entityKeyValue) {
		this.entityKeyValue = entityKeyValue;
	}
}
