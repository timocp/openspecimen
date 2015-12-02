package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.common.errors.ErrorCode;

public enum ShipmentErrorCode implements ErrorCode {
	NOT_FOUND,
	
	NAME_REQUIRED,
	
	DUP_NAME,
	
	SEND_SITE_REQUIRED,
	
	REC_SITE_REQUIRED,
	
	DUPLICATE_SPECIMENS,
	
	SPEC_REC_QUALITY_REQUIRED,
	
	INVALID_SPEC_REC_QUALITY,
	
	NO_SPECIMENS_TO_SHIP,
	
	STATUS_REQUIRED,
	
	INVALID_STATUS,
	
	INVALID_SHIPPED_DATE,
	
	INVALID_RECEIVED_DATE,
	
	ALREADY_SHIPPED,
	
	SPECIMEN_ALREADY_SHIPPED,
	
	CLOSED_SPECIMENS,
	
	UNAVAILABLE_SPECIMENS,
	
	ALREADY_RECEIVED,
	
	INVALID_SPECIMENS,
	
	INVALID_SHIPPED_SPECIMENS,
	
	STATUS_CHANGE_NOT_ALLOWED,
	
	SPEC_NOT_BELONG_TO_SEND_SITE,
	
	SPEC_NOT_BELONG_TO_REC_SITE,
	
	NOTIFY_USER_NOT_BELONG_TO_INST
	;
	
	@Override
	public String code() {
		return "SHIPMENT_" + this.name();
	}
}