package com.krishagni.catissueplus.core.audit.services;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.AuditInfo;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface AuditService {

	// Internal APIs

	public void insertApiCallLog(UserApiCallLog userAuditLog);

	public long getTimeSinceLastApiCall(Long userId, String token);

	public ResponseEvent<AuditInfo> getAuditInfo(RequestEvent<RequestAudit> req);

	public void addAuditTable(String objectType, String tableName);

}
