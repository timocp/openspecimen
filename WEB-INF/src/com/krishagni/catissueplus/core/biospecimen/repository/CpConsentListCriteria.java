package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class CpConsentListCriteria extends AbstractListCriteria<CpConsentListCriteria> {
	private String code;
	
	private String statement;
	
	@Override
	public CpConsentListCriteria self() {
		return this;
	}

	public String code() {
		return code;
	}

	public CpConsentListCriteria code(String code) {
		this.code = code;
		return self();
	}

	public String statement() {
		return statement;
	}

	public CpConsentListCriteria statement(String statement) {
		this.statement = statement;
		return self();
	}

}
