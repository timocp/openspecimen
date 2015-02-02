package com.krishagni.catissueplus.core.de.ui;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.AbstractControlFactory;

public class SpecimenPositionControlFactory extends AbstractControlFactory {
	public static SpecimenPositionControlFactory getInstance() {
		return new SpecimenPositionControlFactory();
	}
	
	@Override
	public String getType() {
		return "specimenPosition";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		SpecimenPositionControl ctrl = new SpecimenPositionControl();
		super.setControlProps(ctrl, ele, row, xPos);
		return ctrl;
	}
}
