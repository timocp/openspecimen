package com.krishagni.catissueplus.core.de.ui;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.factory.AbstractControlFactory;

public class DistributionProtocolControlFactory extends AbstractControlFactory {
	
	public static DistributionProtocolControlFactory getInstance() {
		return new DistributionProtocolControlFactory();
	}

	@Override
	public String getType() {
		return "distributionProtocolField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		DistributionProtocolControl ctrl = new DistributionProtocolControl();
		super.setControlProps(ctrl, ele, row, xPos);
		return ctrl;
	}
	
}
