package com.krishagni.catissueplus.core.audit.dao;

import java.util.List;

import org.hibernate.envers.AuditReader;

import com.krishagni.catissueplus.core.audit.domain.CustomRevisionEntity;
import com.krishagni.catissueplus.core.common.repository.Dao;


public interface AuditDao extends Dao<CustomRevisionEntity>{
	public CustomRevisionEntity getEntityCreatedDetails(String auditTable, Long entityId);

	public CustomRevisionEntity getEntityModifiedDetails(String auditTable, Long entityId, Long rev);

	public List<Object> getRevisions(String auditTable, Long entityId, Integer startAt, Integer maxRecs);

	public CustomRevisionEntity getSubjectRoleLastModifiedDetails(Long dsoId, Long rev);
	
	public AuditReader getAuditReader();

	public Long getRevision(String auditTable, Long entityId);
}
