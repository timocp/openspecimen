package com.krishagni.catissueplus.core.audit.events;

import java.util.List;
import java.util.Map;


public class AuditDetail {

	private Long revisionId;
	private String action;
	private String user;
	private Long time;
	private List<Map<String, Object>> modifiedAttrs;
	
	public Long getRevisionId() {
		return revisionId;
	}
	
	public void setRevisionId(Long revisionId) {
		this.revisionId = revisionId;
	}
	
	public String getAction() {
		return action;
	}
	
	public void setAction(String action) {
		this.action = action;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public Long getTime() {
		return time;
	}
	
	public void setTime(Long time) {
		this.time = time;
	}
	
	public List<Map<String, Object>> getModifiedAttrs() {
		return modifiedAttrs;
	}
	
	public void setModifiedAttrs(List<Map<String, Object>> modifiedAttrs) {
		this.modifiedAttrs = modifiedAttrs;
	}
	
}
