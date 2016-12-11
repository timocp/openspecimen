package com.krishagni.catissueplus.core.biospecimen.domain.factory;

import com.krishagni.catissueplus.core.biospecimen.domain.Consent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;

public interface ConsentFactory {
	Consent createConsent(ConsentDetail summary);
}
