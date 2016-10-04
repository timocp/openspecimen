package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Query;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimestampType;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.RevisionInfo;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
import com.krishagni.catissueplus.core.audit.repository.RevisionListCriteria;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class AuditDaoImpl extends AbstractDao<UserApiCallLog> implements AuditDao {

	@Override
	@SuppressWarnings("unchecked")
	public Date getLatestApiCallTime(Long userId, String token) {
		List<Date> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_LATEST_API_CALL_TIME)
				.setLong("userId", userId)
				.setString("authToken", token)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}

	@Override
	public String getEntityRevisionSql(String entityName) {
		Query query = getCurrentSession().createSQLQuery(GET_ENTITY_REV_QUERY_SQL);
		query.setString("entityName", entityName);
		return (String)query.uniqueResult();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<RevisionInfo> getRevisions(RevisionListCriteria crit) {
		Query query = getRevisionsQuery(crit.entitySql(), getRestrictions(crit), getOrderDirection(crit))
			.setLong("rootEntityId", crit.entityId())
			.setFirstResult(crit.startAt())
			.setMaxResults(crit.maxResults());

		if (crit.from() != null && crit.to() != null) {
			query.setDate("from", crit.from());
			query.setDate("to", crit.to());
		}

		List<Object[]> rows = query.list();
		return rows.stream().map(this::getRevision).collect(Collectors.toList());
	}

	private Query getRevisionsQuery(String entitySql, String restrictions, String direction) {
		String sql = String.format(GET_ENTITY_REV_SQL, entitySql, restrictions, direction);
		return getCurrentSession().createSQLQuery(sql)
			.addScalar("rev",            LongType.INSTANCE)
			.addScalar("revTstmp",       TimestampType.INSTANCE)
			.addScalar("userId",         LongType.INSTANCE)
			.addScalar("firstName",      StringType.INSTANCE)
			.addScalar("lastName",       StringType.INSTANCE)
			.addScalar("loginName",      StringType.INSTANCE)
			.addScalar("domainName",     StringType.INSTANCE)
			.addScalar("ipAddr",         StringType.INSTANCE)
			.addScalar("revType",        IntegerType.INSTANCE)
			.addScalar("entityName",     StringType.INSTANCE)
			.addScalar("entityId",       LongType.INSTANCE)
			.addScalar("entityKey",      StringType.INSTANCE)
			.addScalar("entityKeyValue", StringType.INSTANCE);
	}

	private String getRestrictions(RevisionListCriteria crit) {
		StringBuilder restrictions = new StringBuilder();
		if (crit.from() != null && crit.to() != null) {
			restrictions.append("r.revtstmp >= :from and r.revtstmp <= :to");
		}

		if (restrictions.length() > 0) {
			restrictions.insert(0, "where ");
		}

		return restrictions.toString();
	}

	private String getOrderDirection(RevisionListCriteria crit) {
		return crit.latestFirst() ? "desc" : "asc";
	}

	private RevisionInfo getRevision(Object[] row) {
		RevisionInfo revision = new RevisionInfo();

		int idx = 0;
		revision.setId((Long)row[idx++]);
		revision.setTime((Date)row[idx++]);

		UserSummary user = new UserSummary();
		user.setId((Long)row[idx++]);
		user.setFirstName((String)row[idx++]);
		user.setLastName((String)row[idx++]);
		user.setLoginName((String)row[idx++]);
		user.setDomain((String)row[idx++]);
		revision.setUser(user);

		revision.setIpAddress((String)row[idx++]);

		Integer revType = (Integer)row[idx++];
		revision.setOp(OP_NAMES[revType]);

		revision.setEntityType((String)row[idx++]);
		revision.setEntityId((Long)row[idx++]);
		revision.setEntityKey((String)row[idx++]);
		revision.setEntityKeyValue((String)row[idx++]);
		return revision;
	}

	private static final String[] OP_NAMES = {"Create", "Update", "Delete"};

	private static final String FQN = UserApiCallLog.class.getName();
	
	private static final String GET_LATEST_API_CALL_TIME = FQN + ".getLatestApiCallTime";

	private static final String GET_ENTITY_REV_QUERY_SQL =
			"select " +
			"  rev_sql " +
			"from " +
			"  os_audit_entity_metadata " +
			"where " +
			"  entity_name = :entityName";

	private static final String GET_ENTITY_REV_SQL =
			"select " +
			"  r.rev as rev, r.revtstmp as revTstmp, u.identifier as userId, u.first_name as firstName," +
			"  u.last_name as lastName, u.login_name as loginName, u.domain_name as domainName," +
			"  r.ip_address as ipAddr, rd.rev_type as revType, rd.entity_name as entityName, " +
			"  entities.entity_id as entityId, entities.pk_name as entityKey, entities.pk_value as entityKeyValue " +
			"from " +
			"  os_revisions r " +
			"  inner join os_entity_revision_details rd on rd.rev = r.rev " +
			"  inner join catissue_user u on u.identifier = r.user_id " +
			"  inner join (%s) entities on entities.entity_id = rd.entity_id and entities.entity_name = rd.entity_name " +
			"%s " +
			"order by " +
			"  r.rev %s";

}
