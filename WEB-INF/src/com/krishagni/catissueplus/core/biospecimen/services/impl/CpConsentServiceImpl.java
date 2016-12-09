package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.CpConsent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpConsentErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpConsentFactory;
import com.krishagni.catissueplus.core.biospecimen.events.CpConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CpConsentListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.CpConsentService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class CpConsentServiceImpl implements CpConsentService {
	private DaoFactory daoFactory;
	
	private CpConsentFactory consentFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setConsentFactory(CpConsentFactory consentFactory) {
		this.consentFactory = consentFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<CpConsentDetail>> getCpConsents(RequestEvent<CpConsentListCriteria> req) {
		try {
			User user = AuthUtil.getCurrentUser();
			if (!user.isInstituteAdmin() && !user.isAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			List<CpConsent> consents = daoFactory.getCpConsentDao().getCpConsents(req.getPayload());
			return ResponseEvent.response(CpConsentDetail.from(consents));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<CpConsentDetail> createCpConsent(RequestEvent<CpConsentDetail> req) {
		try {
			User user = AuthUtil.getCurrentUser();
			if (!user.isInstituteAdmin() && !user.isAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			CpConsent consent = consentFactory.createCpConsent(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConsentCode(consent.getCode(), ose);
			ose.checkAndThrow();

			daoFactory.getCpConsentDao().saveOrUpdate(consent);
			return ResponseEvent.response(CpConsentDetail.from(consent));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConsentCode(String code, OpenSpecimenException ose) {
		CpConsent consent = daoFactory.getCpConsentDao().getCpConsentByCode(code);
		if (consent != null) {
			ose.addError(CpConsentErrorCode.DUP_CODE, code);
		}
	}

}
