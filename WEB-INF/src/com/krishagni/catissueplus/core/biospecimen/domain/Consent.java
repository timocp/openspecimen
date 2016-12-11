package com.krishagni.catissueplus.core.biospecimen.domain;

public class Consent extends BaseEntity {
	private String code;
	
	private String statement;
	
	private String activityStatus;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public void update(Consent other) {
		setCode(other.getCode());
		setStatement(other.getStatement());
		setActivityStatus(other.getActivityStatus());
	}

}
