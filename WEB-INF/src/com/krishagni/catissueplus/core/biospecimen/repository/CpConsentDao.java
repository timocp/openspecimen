package com.krishagni.catissueplus.core.biospecimen.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CpConsent;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface CpConsentDao  extends Dao<CpConsent> {
	List<CpConsent> getCpConsents(CpConsentListCriteria crit);
	
	CpConsent getCpConsentByCode(String code);
}
