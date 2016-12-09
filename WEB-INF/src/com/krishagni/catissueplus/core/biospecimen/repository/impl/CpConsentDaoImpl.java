package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.CpConsent;
import com.krishagni.catissueplus.core.biospecimen.repository.CpConsentDao;
import com.krishagni.catissueplus.core.biospecimen.repository.CpConsentListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class CpConsentDaoImpl extends AbstractDao<CpConsent> implements CpConsentDao {
	
	@Override
	public Class<?> getType() {
		return CpConsent.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<CpConsent> getCpConsents(CpConsentListCriteria listCrit) {
		Criteria query = getCpConsentListQuery(listCrit)
			.addOrder(Order.asc("consent.code"))
			.setMaxResults(listCrit.maxResults());
		
		return query.list();
	}
	
	private Criteria getCpConsentListQuery(CpConsentListCriteria listCrit) {
		Criteria query = getCurrentSession().createCriteria(CpConsent.class, "consent");
		return addSearchConditions(query, listCrit);
	}

	private Criteria addSearchConditions(Criteria query, CpConsentListCriteria listCrit) {
		addCodeRestriction(query, listCrit.code());
		addStatementRestriction(query, listCrit.statement());

		return query;
	}
	
	private void addCodeRestriction(Criteria query, String code) {
		if (StringUtils.isNotBlank(code)) {
			query.add(Restrictions.ilike("consent.code", code, MatchMode.ANYWHERE));
		}
	}
	
	private void addStatementRestriction(Criteria query, String code) {
		if (StringUtils.isNotBlank(code)) {
			query.add(Restrictions.ilike("consent.statement", code, MatchMode.ANYWHERE));
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public CpConsent getCpConsentByCode(String code) {
		List<CpConsent> result = getCurrentSession()
				.getNamedQuery(GET_CP_CONSENT_BY_CODE)
				.setString("code", code)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	private static final String FQN = CpConsent.class.getName();
	
	private static final String GET_CP_CONSENT_BY_CODE = FQN + ".getCpConsentByCode";

}
