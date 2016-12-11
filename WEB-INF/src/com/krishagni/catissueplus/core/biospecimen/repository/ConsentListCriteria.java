package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ConsentListCriteria extends AbstractListCriteria<ConsentListCriteria> {
	private String code;
	
	private String statement;
	
	@Override
	public ConsentListCriteria self() {
		return this;
	}

	public String code() {
		return code;
	}

	public ConsentListCriteria code(String code) {
		this.code = code;
		return self();
	}

	public String statement() {
		return statement;
	}

	public ConsentListCriteria statement(String statement) {
		this.statement = statement;
		return self();
	}

}
