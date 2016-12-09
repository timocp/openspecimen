package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.CpConsent;
import com.krishagni.catissueplus.core.biospecimen.events.CpConsentDetail;

public interface CpConsentFactory {
	CpConsent createCpConsent(CpConsentDetail summary);
}
