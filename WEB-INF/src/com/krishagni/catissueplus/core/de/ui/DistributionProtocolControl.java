package com.krishagni.catissueplus.core.de.ui;

import java.io.Writer;
import java.util.Map;
import java.util.Properties;

import edu.common.dynamicextensions.domain.nui.AbstractLookupControl;

public class DistributionProtocolControl extends AbstractLookupControl{
	private static final long serialVersionUID = 1L;

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("type", "distributionProtocolField");
		props.put("dataType", getDataType());
	}
	
	public void serializeToXml(Writer writer, Properties props) {
		super.serializeToXml("distributionProtocolField", writer, props);
	}

	@Override
	public String getTableName() {
		return null;
	}
	
	@Override
	public Properties getPvSourceProps() {
		return null;
	}

}
