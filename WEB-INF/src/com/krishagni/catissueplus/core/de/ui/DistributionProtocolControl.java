package com.krishagni.catissueplus.core.de.ui;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.ColumnDef;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.ndao.ColumnTypeHelper;

public class DistributionProtocolControl extends Control{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public Long fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		return new BigDecimal(value).longValue();
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), ColumnTypeHelper.getIntegerColType()));
	}
	
	@Override
	public DataType getDataType() {
		return DataType.INTEGER;
	}

	@Override
	public void getProps(Map<String, Object> props) {
		props.put("type", "distributionProtocolField");
		props.put("dataType", getDataType());
	}
}
