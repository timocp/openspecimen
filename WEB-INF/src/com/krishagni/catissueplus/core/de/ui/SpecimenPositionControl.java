package com.krishagni.catissueplus.core.de.ui;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeCDataElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.Writer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.administrative.events.AllocateSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionAllocatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionDetail;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.common.OpenSpecimenAppCtxProvider;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.ControlValueCrud;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.ResultExtractor;

public class SpecimenPositionControl extends SubFormControl implements ControlValueCrud {
	private static final long serialVersionUID = -1259014333606704473L;
	
	private Container positionForm;
	
	private Control storageContainerCtrl;
	
	private Control posXCtrl;
	
	private Control posYCtrl;
	
	public SpecimenPositionControl() {
		initSpecimenPositionForm();
	}
	
	@Override
	public String getParentKey() {
		return getDbColumnName();
	}
	
	public String getForeignKey() {
		return "IDENTIFIER";
	}
		
	@Override
	public Container getSubContainer() {
		return positionForm;
	}

	@Override
	public boolean isOneToOne() {
		return true;
	}
	
	@Override
	public boolean isInverse() {
		return true;
	}


	@SuppressWarnings("unchecked")
	@Override
	public Long fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}
		
		return Long.parseLong(value);
	}

	@Override
	public void getProps(Map<String, Object> props) {
		//props.putAll(super.getProps());
		props.put("type", "specimenPosition");
	}
	
	@Override
	public void serializeToXml(Writer writer, Properties props) {
		writeElementStart(writer, "specimenPosition");
		writeElement(writer, "name",        getName());
		writeElement(writer, "udn",         getUserDefinedName());
		writeElement(writer, "caption",     getCaption());
		writeCDataElement(writer, "customLabel", getCustomLabel());
		writeElement(writer, "phi",         isPhi());
		writeElement(writer, "mandatory",   isMandatory());
		writeElement(writer, "toolTip",     getToolTip());
		writeElement(writer, "showLabel",   showLabel());
		writeElement(writer, "showInGrid",  showInGrid());
		writeElementEnd(writer, "specimenPosition");							
	}

	@Override
	public ControlValue getValue(JdbcDao jdbcDao, FormData form) {
		final FormData position = new FormData(positionForm); 
		final ControlValue cv = new ControlValue(this, position);
		
		Long recordId = form.getRecordId();		
		if (recordId == null) {
			return cv;
		}
		
		String sql = String.format(GET_SQL, getContainer().getDbTableName(), getDbColumnName());
		return jdbcDao.getResultSet(sql, Collections.singletonList(recordId), new ResultExtractor<ControlValue>() {

			@Override
			public ControlValue extract(ResultSet rs) 
			throws SQLException {
				if (!rs.next()) {
					return cv;
				}

				position.setRecordId(rs.getLong("id"));
				position.addFieldValue(new ControlValue(storageContainerCtrl, rs.getLong("container_id")));
				position.addFieldValue(new ControlValue(posXCtrl, rs.getString("pos_one_str")));
				position.addFieldValue(new ControlValue(posYCtrl, rs.getString("pos_two_str")));								
				return cv;
			}
		});
	}

	@Override
	public Object saveValue(JdbcDao jdbcDao, FormData form, ControlValue value) {
		final FormData position = (FormData)value.getValue();
		if (position == null) {
			return null;			
		}
				
		Long posId = position.getRecordId();		
		try {
			if (posId == null) {
				posId = insertPosition(position);
			} else {
				updatePosition(posId, position);
			}
			
			position.setRecordId(posId);
		} catch (Exception e) {
			throw new RuntimeException("Error saving specimen position", e);
		}
		
		return position.getRecordId();
	}
	
	private Long insertPosition(FormData position) 
	throws Exception {
		AllocateSpecimenPositionEvent req = new AllocateSpecimenPositionEvent();
		req.setPosition(getSpecimenPositionDetail(null, position));
		
		SpecimenPositionAllocatedEvent resp = getContainerService().assignSpecimenPosition(req);
		if (resp.isSuccess()) {
			return resp.getPosition().getId();
		}
		
		resp.raiseException();
		return null;
	}
	
	private void updatePosition(Long posId, FormData position) {
		UpdateSpecimenPositionEvent req = new UpdateSpecimenPositionEvent();
		req.setPosition(getSpecimenPositionDetail(posId, position));
		
		SpecimenPositionUpdatedEvent resp = getContainerService().updateSpecimenPosition(req);
		if (resp.isSuccess()) {
			return;
		}
		
		resp.raiseException();
	}
		
	private SpecimenPositionDetail getSpecimenPositionDetail(Long posId, FormData position) {
		SpecimenPositionDetail detail = new SpecimenPositionDetail();
		detail.setId(posId);

		ControlValue posXValue = position.getFieldValue(posXCtrl.getName());
		if (posXValue != null && posXValue.getValue() != null) {
			detail.setPosX(posXValue.getValue().toString());			
		}
		
		ControlValue posYValue = position.getFieldValue(posYCtrl.getName());
		if (posYValue != null && posYValue.getValue() != null) {
			detail.setPosY(posYValue.getValue().toString());
		} 

		Number specimenId = null;		
		Map<String, Object> appData = position.getAppData();
		if (appData != null) {
			specimenId = (Number)appData.get("specimenId");
		}
		
		if (specimenId == null) {
			appData = position.getRootFormData().getAppData();
			specimenId = (Number)appData.get("objectId");
		}
		
		if (specimenId == null) {
			throw new IllegalArgumentException("Specimen ID not specified");
		}		
		detail.setSpecimenId(specimenId.longValue());
		 		
 		Object containerCtrlVal = position.getFieldValue(storageContainerCtrl.getName()).getValue();
 		Long containerId = null;
 		if (containerCtrlVal != null && StringUtils.isNotEmpty(containerCtrlVal.toString())) {
 			containerId = ((Number)storageContainerCtrl.fromString(containerCtrlVal.toString())).longValue();
 		}
 		
 		if (containerId != null && !containerId.equals(-1L)) {
 			detail.setStorageContainerId(containerId.longValue());
 		}
				
		return detail;		
	}
	
	private StorageContainerService getContainerService() {
		return (StorageContainerService)OpenSpecimenAppCtxProvider.getAppCtx().getBean("storageContainerSvc");
	}
	
	private void initSpecimenPositionForm() {
		positionForm = new Container();
		positionForm.setManagedTables(true);
		positionForm.setDbTableName("SPECIMEN_POS_VIEW");
		
	
		storageContainerCtrl = new StorageContainerControl();
		storageContainerCtrl.setName("storageContainer");
		storageContainerCtrl.setCaption("Storage Container");
		storageContainerCtrl.setUserDefinedName("storageContainer");
		storageContainerCtrl.setDbColumnName("CONTAINER_ID");
		positionForm.addControl(storageContainerCtrl);
		
		posXCtrl = new StringTextField();
		posXCtrl.setName("posX");
		posXCtrl.setCaption("Position Dimension One");
		posXCtrl.setUserDefinedName("posX");
		posXCtrl.setDbColumnName("POSITION_DIMENSION_ONE_STRING");
		positionForm.addControl(posXCtrl);
		
		posYCtrl = new StringTextField();
		posYCtrl.setName("posY");
		posYCtrl.setCaption("Position Dimension Two");
		posYCtrl.setUserDefinedName("posY");
		posYCtrl.setDbColumnName("POSITION_DIMENSION_TWO_STRING");
		positionForm.addControl(posYCtrl);	
	}
	
	private static final String GET_SQL = 
			"select " +
			"  sp.identifier as id, sp.specimen_id as specimen_id, sp.container_id as container_id, " +
			"  ap.position_dimension_one as pos_one, ap.position_dimension_two as pos_two, " +
			"  ap.position_dimension_one_string as pos_one_str, ap.position_dimension_two_string as pos_two_str " +
			"from " +
			"  catissue_specimen_position sp " +
			"  inner join catissue_abstract_position ap on ap.identifier = sp.identifier " +
			"  inner join %s de on de.%s = sp.identifier " + 
			"where " +
			"  de.identifier = ?";	
}
