package com.krishagni.catissueplus.core.audit.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface AuditDao extends Dao<UserApiCallLog> {
	Date getLatestApiCallTime(Long userId, String token);

	String getEntityRevisionSql(String entityName);

	List<Map<String, Object>> getRevisions(String entitySql, Long entityId, Date from, Date to, int startAt, int maxRevs);
}

