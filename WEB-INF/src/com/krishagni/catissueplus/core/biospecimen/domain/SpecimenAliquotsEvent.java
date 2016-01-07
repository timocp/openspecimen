package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Map;


public class SpecimenAliquotsEvent extends SpecimenEvent {
	
	public SpecimenAliquotsEvent(Specimen specimen) {
		super(specimen);
	}

	@Override
	public String getFormName() {
		return "SpecimenAliquotsEvent";
	}
	
	@Override
	protected Map<String, Object> getEventAttrs() {
		return null;
	}

	@Override
	protected void setEventAttrs(Map<String, Object> attrValues) {
		
	}
	
	public static SpecimenAliquotsEvent createForShipmentItem(Specimen specimen) {
		SpecimenAliquotsEvent event = new SpecimenAliquotsEvent(specimen.getParentSpecimen());
		event.setId(specimen.getId());
		return event;
	}
}
