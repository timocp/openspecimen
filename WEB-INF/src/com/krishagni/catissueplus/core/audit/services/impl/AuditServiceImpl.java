package com.krishagni.catissueplus.core.audit.services.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.errors.AuditErrorCode;
import com.krishagni.catissueplus.core.audit.events.AuditInfo;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AuditServiceImpl implements AuditService {

	private DaoFactory daoFactory;

	private Map<String, String> entityAuditTables = new HashMap<String, String>();

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setEntityAuditTables(Map<String, String> entityAuditTables) {
		this.entityAuditTables = entityAuditTables;
	}

	@Override
	@PlusTransactional
	public void insertApiCallLog(UserApiCallLog userAuditLog) {
		daoFactory.getAuditDao().saveOrUpdate(userAuditLog);
	}

	@Override
	@PlusTransactional
	public long getTimeSinceLastApiCall(Long userId, String token) {
		Date lastApiCallTime = daoFactory.getAuditDao().getLatestApiCallTime(userId, token);
		long timeSinceLastApiCallInMilli = Calendar.getInstance().getTime().getTime() - lastApiCallTime.getTime();
		return TimeUnit.MILLISECONDS.toMinutes(timeSinceLastApiCallInMilli);
	}

	@Override
	@PlusTransactional
	public ResponseEvent<AuditInfo> getAuditInfo(RequestEvent<RequestAudit> req) {
		RequestAudit reqEntity = req.getPayload();
		String entityType = reqEntity.getObjectType();

		String auditTable = entityAuditTables.get(entityType);
		if (auditTable == null) {
			return ResponseEvent.userError(AuditErrorCode.TABLE_NOT_FOUND);
		}

		Long entityId = reqEntity.getObjectId();
		AuditInfo auditInfo = daoFactory.getAuditDao().getAuditInfo(auditTable, entityId);

		return ResponseEvent.response(auditInfo);
	}

	@Override
	public void addAuditTable(String objectType, String tableName) {
		entityAuditTables.put(objectType, tableName);
	}
}
