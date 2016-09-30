package com.krishagni.catissueplus.core.audit.services;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;

public interface AuditService {

	void exportRevisionData(String entity, Long entityId, Date from, Date to);
	
	// Internal APIs
	
	void insertApiCallLog(UserApiCallLog userAuditLog);
	
	long getTimeSinceLastApiCall(Long userId, String token);
}
