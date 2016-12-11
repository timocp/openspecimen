package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface ConsentService {
	ResponseEvent<List<ConsentDetail>> getConsents(RequestEvent<ConsentListCriteria> req);
	
	ResponseEvent<ConsentDetail> getConsent(RequestEvent<Long> req);
	
	ResponseEvent<ConsentDetail> createConsent(RequestEvent<ConsentDetail> req);
	
	ResponseEvent<ConsentDetail> updateConsent(RequestEvent<ConsentDetail> req);
}
