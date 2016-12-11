package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Consent;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface ConsentDao  extends Dao<Consent> {
	List<Consent> getConsents(ConsentListCriteria crit);
	
	Consent getConsentByCode(String code);
}
