package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum CpConsentErrorCode implements ErrorCode {
	CODE_REQUIRED,
	
	STATEMENT_REQUIRED,
	
	DUP_CODE;
	
	@Override
	public String code() {
		return "CP_CONSENT_" + this.name();
	}
}
