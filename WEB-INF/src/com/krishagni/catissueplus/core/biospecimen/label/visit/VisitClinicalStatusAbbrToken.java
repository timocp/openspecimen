package com.krishagni.catissueplus.core.biospecimen.label.visit;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.common.PvAttributes;

public class VisitClinicalStatusAbbrToken extends AbstractVisitAbbrLabelToken {

	public VisitClinicalStatusAbbrToken() {
		this.name = "CLINICAL_STATUS_ABBR";
	}

	@Override
	public String getLabel(Visit visit, String... args) {
		return getLabel(PvAttributes.CLINICAL_STATUS, visit.getClinicalStatus());
	}
}
