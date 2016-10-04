package com.krishagni.catissueplus.core.audit.domain;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum AuditRevErrorCode implements ErrorCode {
	ADMIN_REQ,

	ENTITY_NAME_REQ,

	ENTITY_ID_REQ,

	UNKNOWN_ENTITY,

	FROM_GT_TO_DT,

	FILE_NOT_FOUND,

	MAX_REVS_LMT_EXCEEDED;

	@Override
	public String code() {
		return "AUDIT_REV_" + name();
	}
}
