package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ConsentErrorCode implements ErrorCode {
	NOT_FOUND,
	
	CODE_REQUIRED,
	
	STATEMENT_REQUIRED,
	
	DUP_CODE;
	
	@Override
	public String code() {
		return "CONSENT_" + this.name();
	}
}
