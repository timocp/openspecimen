package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ConsentStatementListCriteria extends AbstractListCriteria<ConsentStatementListCriteria> {
	private String code;
	
	@Override
	public ConsentStatementListCriteria self() {
		return this;
	}

	public String code() {
		return code;
	}

	public ConsentStatementListCriteria code(String code) {
		this.code = code;
		return self();
	}
}
