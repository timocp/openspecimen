package com.krishagni.catissueplus.core.audit.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.RevisionInfo;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface AuditDao extends Dao<UserApiCallLog> {
	Date getLatestApiCallTime(Long userId, String token);

	String getEntityRevisionSql(String entityName);

	List<RevisionInfo> getRevisions(RevisionListCriteria crit);
}

