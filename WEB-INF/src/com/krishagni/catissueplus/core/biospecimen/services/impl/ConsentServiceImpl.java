package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.Consent;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ConsentFactory;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentListCriteria;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.biospecimen.services.ConsentService;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class ConsentServiceImpl implements ConsentService {
	private DaoFactory daoFactory;
	
	private ConsentFactory consentFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setConsentFactory(ConsentFactory consentFactory) {
		this.consentFactory = consentFactory;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<ConsentDetail>> getConsents(RequestEvent<ConsentListCriteria> req) {
		try {
			List<Consent> consents = daoFactory.getConsentDao().getConsents(req.getPayload());
			return ResponseEvent.response(ConsentDetail.from(consents));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentDetail> getConsent(RequestEvent<Long> req) {
		try {
			Consent consent = getConsent(req.getPayload());
			return ResponseEvent.response(ConsentDetail.from(consent));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentDetail> createConsent(RequestEvent<ConsentDetail> req) {
		try {
			User user = AuthUtil.getCurrentUser();
			if (!user.isInstituteAdmin() && !user.isAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			Consent consent = consentFactory.createConsent(req.getPayload());
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConsentCode(null, consent, ose);
			ose.checkAndThrow();

			daoFactory.getConsentDao().saveOrUpdate(consent);
			return ResponseEvent.response(ConsentDetail.from(consent));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<ConsentDetail> updateConsent(RequestEvent<ConsentDetail> req) {
		try {
			User user = AuthUtil.getCurrentUser();
			if (!user.isInstituteAdmin() && !user.isAdmin()) {
				return ResponseEvent.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
			}
			
			ConsentDetail input = req.getPayload();
			Consent existing = getConsent(input.getId());
			Consent consent = consentFactory.createConsent(input);
			
			OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);
			ensureUniqueConsentCode(existing, consent, ose);
			ose.checkAndThrow();
			
			existing.update(consent);
			daoFactory.getConsentDao().saveOrUpdate(existing);
			return ResponseEvent.response(ConsentDetail.from(existing));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
	private void ensureUniqueConsentCode(Consent existingConsent, Consent newConsent, OpenSpecimenException ose) {
		if (!isUniqueCode(existingConsent, newConsent)) {
			ose.addError(ConsentErrorCode.DUP_CODE, newConsent.getCode());
		}
	}
	
	private boolean isUniqueCode(Consent existingConsent, Consent newConsent) {
		if (existingConsent != null && existingConsent.getCode().equals(newConsent.getCode())) {
			return true;
		}
		
		Consent consent = daoFactory.getConsentDao().getConsentByCode(newConsent.getCode());
		return consent == null;
	}
	
	private Consent getConsent(Long id) {
		Consent consent = daoFactory.getConsentDao().getById(id);
		
		if (consent == null) {
			throw OpenSpecimenException.userError(ConsentErrorCode.NOT_FOUND, id);
		}
		
		return consent;
	}

}
