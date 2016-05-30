package com.krishagni.catissueplus.core.audit.repository;

import java.util.Date;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.AuditInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface AuditDao extends Dao<UserApiCallLog> {
	
	public Date getLatestApiCallTime(Long userId, String token);

	public AuditInfo getAuditInfo(String tableName, Long entityId);

}

