package com.krishagni.catissueplus.core.audit.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;

import com.krishagni.catissueplus.core.audit.dao.AuditDao;
import com.krishagni.catissueplus.core.audit.domain.CustomRevisionEntity;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class AuditDaoImpl extends AbstractDao<CustomRevisionEntity> implements AuditDao{
	
	public Class<CustomRevisionEntity> getEntityClass() {
		return CustomRevisionEntity.class;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CustomRevisionEntity getEntityCreatedDetails(String auditTable, Long entityId) {
		String sql = String.format(GET_ENTITY_CREATED_DETAILS_SQL, auditTable);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setLong("entityId", entityId);

		List<Object[]> rows = query.list();
		return rows.size() > 0 ? getRevisionEntity(rows.iterator().next()) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public CustomRevisionEntity getEntityModifiedDetails(String auditTable, Long entityId, Long rev) {
		String whereClause = "";
		if (rev != null) {
			whereClause = "and rev.rev = :rev";
		}
		String sql = String.format(GET_ENTITY_LAST_MODIFIED_DETAILS_SQL, auditTable, whereClause);
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql)
				.setLong("entityId", entityId);

		if (rev != null) {
			query.setLong("rev", rev);
		}

		List<Object[]> rows = query.list();
		return rows.size() > 0 ? getRevisionEntity(rows.iterator().next()) : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Object> getRevisions(String auditTable, Long entityId, Integer startAt, Integer maxRecs) {
		String sql = String.format(GET_REVISIONS_SQL, auditTable, startAt, maxRecs);
		return sessionFactory.getCurrentSession()
				.createSQLQuery(sql)
				.setLong("identifier", entityId)
				.list();
	}
	
	private CustomRevisionEntity getRevisionEntity(Object[] row) {
		CustomRevisionEntity revisionEntity = new CustomRevisionEntity();
		revisionEntity.setIdentifier(Integer.valueOf(row[0].toString()));
		revisionEntity.setUserId(Long.valueOf(row[1].toString()));
		revisionEntity.setTimestamp(Timestamp.valueOf(row[2].toString()).getTime());
		revisionEntity.setAction("0".equals(row[3].toString()) ? "Create" : "Update");
		return revisionEntity;
	}

	public AuditReader getAuditReader() {
		return AuditReaderFactory.get(sessionFactory.getCurrentSession());
	}

	public Long getRevision(String auditTable, Long entityId) {
		String sql = String.format(GET_REVISION_ID_SQL, auditTable);
		return Long.valueOf((sessionFactory.getCurrentSession()
				.createSQLQuery(sql)
				.setLong("entityId", entityId).list().get(0).toString()));
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CustomRevisionEntity getSubjectRoleLastModifiedDetails(Long dsoId, Long rev) {
		String whereClause = "";
		if(rev != null) {
			whereClause = "and rev.rev = :rev";
		}
		
		String sql = String.format(GET_SUBJECT_ROLE_LAST_MODIFIED_SQL, whereClause);
		Query query = sessionFactory.getCurrentSession()
					.createSQLQuery(sql)
					.setLong("dsoId", dsoId);
		
		if(rev != null) {
			query.setLong("rev", rev);
		}
		
		List<Object[]> rows = query.list();
		
		return rows.size() > 0 ? getRevisionEntity(rows.iterator().next()) : null;
	}
	
	private static final String GET_SUBJECT_ROLE_LAST_MODIFIED_SQL =
			" select " +
			"   rev.rev, rev.user_id, rev.revtstmp" +
			" from " +
			"	  os_revisions rev"+
			"   inner join rbac_subject_roles_aud aud on rev.rev = aud.rev %s " +
			" where " +
			"	  aud.dso_id = :dsoId " +
			" order by " +
			"   aud.rev desc limit 1";
			
	private static final String GET_REVISION_ID_SQL =
			
			" select " +
			"	  rev.rev" +
			" from " +
			"	  os_revisions rev " +
			"   inner join %s aud on rev.rev = aud.rev " +
			" where " +
			"	  aud.identifier = :entityId " +
			" order by " +
			"	  rev desc limit 1";

	private static final String GET_ENTITY_CREATED_DETAILS_SQL =
			" select " +
			"   rev.rev, rev.user_id, rev.revtstmp, aud.revtype " +
			" from " +
			"   os_revisions rev" +
			"   inner join %s aud on rev.rev = aud.rev " +
			" where " +
			"   aud.identifier = :entityId and aud.REVTYPE = 0";

	private static final String GET_ENTITY_LAST_MODIFIED_DETAILS_SQL =
			" select " +
			"   rev.rev, rev.user_id, rev.revtstmp, aud.revtype " +
			" from " +
			"   os_revisions rev" +
			"   inner join %s aud on rev.rev = aud.rev " +
			" where " +
			"   aud.identifier = :entityId and aud.REVTYPE = 1 %s " +
			" order by " +
			"   rev.revtstmp desc limit 1 ";

	private static final String GET_REVISIONS_SQL =
			" select " +
			"   distinct rev " +
			" from " +
			"   %s " +
			" where " +
			"   identifier = :identifier  and rev > %s " +
			" order by rev desc limit %s";

}
