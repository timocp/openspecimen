package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentStatementDao;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentStatementListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ConsentStatementDaoImpl extends AbstractDao<ConsentStatement> implements ConsentStatementDao {
	
	@Override
	public Class<?> getType() {
		return ConsentStatement.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<ConsentStatement> getStatements(ConsentStatementListCriteria listCrit) {
		return getStatementListCriteria(listCrit)
			.addOrder(Order.asc("code"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults())
			.list();
	}
	
	private Criteria getStatementListCriteria(ConsentStatementListCriteria listCrit) {
		Criteria query = getCurrentSession().createCriteria(ConsentStatement.class);
		addCodeRestriction(query, listCrit.code());
		addStatementRestriction(query, listCrit.query());
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
	public ConsentStatement getByCode(String code) {
		return (ConsentStatement)getCurrentSession().getNamedQuery(GET_BY_CODE)
			.setString("code", code)
			.uniqueResult();
	}
	
	private static final String FQN = ConsentStatement.class.getName();
	
	private static final String GET_BY_CODE = FQN + ".getByCode";

}
