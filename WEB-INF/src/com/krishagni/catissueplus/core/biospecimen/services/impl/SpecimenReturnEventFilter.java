package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataFilter;
import edu.wustl.catissuecore.domain.Specimen;

public class SpecimenReturnEventFilter implements FormDataFilter {
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public FormData execute(UserContext userCtx, FormData input) {
		Number specimenId = (Number)input.getAppData().get("objectId");
		if (specimenId == null) {
			return input;
		}
		
		ControlValue cv = input.getFieldValue("returnedQuantity");
		if (cv == null) {
			throw new IllegalArgumentException("Returned quantity is mandatory");
		}
						
		try {
			Specimen specimen = daoFactory.getStorageContainerDao().getSpecimen(specimenId.longValue());
			Double returnedQty = ((Number)cv.getControl().fromString((String)cv.getValue())).doubleValue();
			if (specimen.getInitialQuantity().compareTo(returnedQty) < 0) {
				throw new IllegalArgumentException("Returned quantity can't be greater than initial quantity");
			}
			
			specimen.setAvailableQuantity(returnedQty);
		} catch (IllegalArgumentException iae) {
			throw iae;
		} catch (Exception e) {
			throw new RuntimeException("Error executing return event filter", e);
		}
		
		return input;
	}
}
