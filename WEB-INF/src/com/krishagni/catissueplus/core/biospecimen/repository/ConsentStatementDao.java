package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ConsentStatementDao extends Dao<ConsentStatement> {
	List<ConsentStatement> getStatements(ConsentStatementListCriteria crit);
	
	Long getStatementsCount(ConsentStatementListCriteria crit);
	
	ConsentStatement getByCode(String code);

	ConsentStatement getByStatement(String statement);
}
