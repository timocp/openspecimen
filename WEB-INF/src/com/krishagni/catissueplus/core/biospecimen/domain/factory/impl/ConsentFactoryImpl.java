package com.krishagni.catissueplus.core.biospecimen.domain.factory.impl;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Consent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.common.errors.ActivityStatusErrorCode;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;

public class ConsentFactoryImpl implements ConsentFactory {
	
	@Override
	public Consent createConsent(ConsentDetail detail) {
		Consent consent = new Consent();
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		setConsentCode(detail, consent, ose);
		setConsentStatement(detail, consent, ose);
		setActivityStatus(detail, consent, ose);
		
		ose.checkAndThrow();
		return consent;
	}
	
	private void setConsentCode(ConsentDetail detail, Consent consent, OpenSpecimenException ose) {
		String code = detail.getCode();
		if (StringUtils.isBlank(code)) {
			ose.addError(ConsentErrorCode.CODE_REQUIRED);
			return;
		}
		
		consent.setCode(code);
	}
	
	private void setConsentStatement(ConsentDetail detail, Consent consent, OpenSpecimenException ose) {
		String statement = detail.getStatement();
		if (StringUtils.isBlank(statement)) {
			ose.addError(ConsentErrorCode.STATEMENT_REQUIRED);
			return;
		}
		
		consent.setStatement(statement);
	}
	
	private void setActivityStatus(ConsentDetail detail, Consent consent, OpenSpecimenException ose) {
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
