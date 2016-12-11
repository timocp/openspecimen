package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.Consent;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentDao;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ConsentDaoImpl extends AbstractDao<Consent> implements ConsentDao {
	
	@Override
	public Class<?> getType() {
		return Consent.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Consent> getConsents(ConsentListCriteria listCrit) {
		Criteria query = getConsentListQuery(listCrit)
			.addOrder(Order.asc("consent.code"))
			.setMaxResults(listCrit.maxResults());
		
		return query.list();
	}
	
	private Criteria getConsentListQuery(ConsentListCriteria listCrit) {
		Criteria query = getCurrentSession().createCriteria(Consent.class);
		return addSearchConditions(query, listCrit);
	}

	private Criteria addSearchConditions(Criteria query, ConsentListCriteria listCrit) {
		addCodeRestriction(query, listCrit.code());
		addStatementRestriction(query, listCrit.statement());

		return query;
	}
	
	private void addCodeRestriction(Criteria query, String code) {
		if (StringUtils.isNotBlank(code)) {
			query.add(Restrictions.ilike("code", code, MatchMode.ANYWHERE));
		}
	}
	
	private void addStatementRestriction(Criteria query, String code) {
		if (StringUtils.isNotBlank(code)) {
			query.add(Restrictions.ilike("statement", code, MatchMode.ANYWHERE));
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Consent getConsentByCode(String code) {
		List<Consent> result = getCurrentSession()
				.getNamedQuery(GET_CONSENT_BY_CODE)
				.setString("code", code)
				.list();
		
		return result.isEmpty() ? null : result.get(0);
	}
	
	private static final String FQN = Consent.class.getName();
	
	private static final String GET_CONSENT_BY_CODE = FQN + ".getConsentByCode";

}
