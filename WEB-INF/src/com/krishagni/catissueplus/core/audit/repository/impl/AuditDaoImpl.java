package com.krishagni.catissueplus.core.audit.repository.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
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
	public List<Map<String, Object>> getRevisions(String entitySql, Long entityId, Date from, Date to, int startAt, int maxRevs) {
		String sql = String.format(GET_ENTITY_REV_SQL, entitySql);

		List<Object[]> rows = getCurrentSession().createSQLQuery(sql)
			.setDate("from", from)
			.setDate("to", to)
			.setLong("entityId", entityId)
			.setFirstResult(startAt)
			.setMaxResults(maxRevs)
			.list();

		return rows.stream().map(row -> {
			int idx = 0;
			Map<String, Object> rev = new HashMap<>();

			rev.put("revId", row[idx++]);
			rev.put("revTs", row[idx++]);
			rev.put("userFirstName", row[idx++]);
			rev.put("userLastName", row[idx++]);
			rev.put("userIpAddr", row[idx++]);
			rev.put("revType", row[idx++]);
			rev.put("entityName", row[idx++]);

			return rev;
		})
		.collect(Collectors.toList());
	}

	private static final String FQN = UserApiCallLog.class.getName();
	
	private static final String GET_LATEST_API_CALL_TIME = FQN + ".getLatestApiCallTime";

	private static final String GET_ENTITY_REV_QUERY_SQL =
			"select " +
			"  sql " +
			"from " +
			"  os_entity_metadata " +
			"where " +
			"  entity_name = :entityName";

	private static final String GET_ENTITY_REV_SQL =
			"select " +
			"  r.rev, r.revtstmp, u.first_name, u.last_name, r.ip_address, rd.rev_type, rd.entity_name " +
			"from " +
			"  os_revisions r " +
			"  inner join os_entity_revision_details rd on rd.rev = r.rev " +
			"  inner join catissue_user u on u.identifier = r.user_id " +
			"  inner join (%s) entities on entities.entity_id = rd.entity_id and entities.entity_name = rd.entity_name " +
			"where " +
			"  r.revtstmp >= :from and r.revtstmp <= :to " +
			"order by " +
			"  r.rev";

}
