package com.krishagni.catissueplus.core.audit.repository.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.AuditInfo;
import com.krishagni.catissueplus.core.audit.repository.AuditDao;
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
	@SuppressWarnings("unchecked")
	public AuditInfo getAuditInfo(String tableName, Long entityId) {
		String sql = String.format(GET_COMMON_AUDIT_DETAIL_SQL, tableName, tableName);
		List<Object[]> rows = sessionFactory.getCurrentSession()
				.createSQLQuery(sql)
				.setLong("entityId", entityId)
				.list();

		return rows.isEmpty() ? null : getRevisionEntity(rows.iterator().next());
	}

	private AuditInfo getRevisionEntity(Object[] row) {
		AuditInfo detail = new AuditInfo();
		UserSummary creator = new UserSummary();
		creator.setId(Long.valueOf(String.valueOf(row[0])));
		creator.setFirstName(String.valueOf(row[1]));
		creator.setLastName(String.valueOf(row[2]));
		creator.setDomain(String.valueOf(row[3]));
		creator.setEmailAddress(String.valueOf(row[4]));

		detail.setCreatedBy(creator);
		detail.setCreatedOn(getTimeStamp(row[5]));

		UserSummary modifier = new UserSummary();
		modifier.setId(Long.valueOf(String.valueOf(row[0])));
		modifier.setFirstName(String.valueOf(row[1]));
		modifier.setLastName(String.valueOf(row[2]));
		modifier.setDomain(String.valueOf(row[3]));
		modifier.setEmailAddress(String.valueOf(row[4]));

		detail.setLastUpdatedBy(modifier);
		detail.setLastUpdatedOn(getTimeStamp(row[5]));

		return detail;
	}

	private Date getTimeStamp(Object timestamp) {
		if(timestamp == null){
			return null;
		}
		return new Date(Timestamp.valueOf(String.valueOf(timestamp)).getTime());
	}

	private static final String GET_COMMON_AUDIT_DETAIL_SQL = 
			" select " + 
			"   creator.c_id, creator.c_first_name, creator.c_last_name, creator.c_domain_name, creator.c_email_address, creator.createdOn, " +
			"   modifier.m_id, modifier.m_first_name, modifier.m_last_name, modifier.m_domain_name, modifier.m_email_address, modifier.modifiedOn " +
			" from " +
			"   (select " +
			"      aud.identifier, u.identifier as c_id, u.first_name as c_first_name, u.last_name as c_last_name, domain_name as c_domain_name, " +
			"      email_address as c_email_address, institution_id as c_institution_id, rev.revtstmp as createdOn " +
			"    from " +
			"      os_revisions rev " +  
      "      inner join %s aud on rev.rev = aud.rev " +
      "      inner join catissue_user u on u.identifier = rev.user_id " +
      "    where " +
      "      aud.identifier = :entityId and aud.revtype = 0) creator " +
      "   left join " + 
	    "   (select " +
      "      aud.identifier, u.identifier as m_id, u.first_name as m_first_name, u.last_name as m_last_name, domain_name as m_domain_name, " +
	    "      email_address as m_email_address, institution_id as m_institution_id, rev.revtstmp as modifiedOn " +
	    "    from " +
      "      os_revisions rev " +  
      "      inner join %s aud on rev.rev = aud.rev " + 
      "      inner join catissue_user u on u.identifier = rev.user_id " +
      "    where " +
      "      aud.identifier = :entityId and aud.revtype = 1 " +
      "    order by " +
      "      rev.revtstmp desc limit 1) modifier " +
      "   on creator.identifier = modifier.identifier ";
	
	private static final String FQN = UserApiCallLog.class.getName();

	private static final String GET_LATEST_API_CALL_TIME = FQN + ".getLatestApiCallTime";

}
