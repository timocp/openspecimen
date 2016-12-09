package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.CpConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CpConsentListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface CpConsentService {
	ResponseEvent<List<CpConsentDetail>> getCpConsents(RequestEvent<CpConsentListCriteria> req);
	
	ResponseEvent<CpConsentDetail> createCpConsent(RequestEvent<CpConsentDetail> req);
}
