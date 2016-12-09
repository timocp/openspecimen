package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CpConsent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpConsentErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpConsentFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CpConsentDetail;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class CpConsentFactoryImpl implements CpConsentFactory {
	
	@Override
	public CpConsent createCpConsent(CpConsentDetail detail) {
		CpConsent consent = new CpConsent();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setConsentCode(detail, consent, ose);
		setConsentStatement(detail, consent, ose);
		setActivityStatus(detail, consent, ose);
		
		ose.checkAndThrow();
		return consent;
	}
	
	private void setConsentCode(CpConsentDetail summary, CpConsent consent, OpenSpecimenException ose) {
		String code = summary.getCode();
		if (StringUtils.isBlank(code)) {
			ose.addError(CpConsentErrorCode.CODE_REQUIRED);
			return;
		}
		
		consent.setCode(code);
	}
	
	private void setConsentStatement(CpConsentDetail detail, CpConsent consent, OpenSpecimenException ose) {
		String statement = detail.getStatement();
		if (StringUtils.isBlank(statement)) {
			ose.addError(CpConsentErrorCode.STATEMENT_REQUIRED);
			return;
		}
		
		consent.setStatement(statement);
	}
	
	private void setActivityStatus(CpConsentDetail detail, CpConsent consent, OpenSpecimenException ose) {
		String activityStatus = detail.getActivityStatus();
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		if (!Status.isValidActivityStatus(activityStatus)) {
			ose.addError(ActivityStatusErrorCode.INVALID);
			return;
		}

		consent.setActivityStatus(activityStatus);
	}
	
	

}
