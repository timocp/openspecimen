
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import krishagni.catissueplus.dto.SpecimenDTO;
import krishagni.catissueplus.util.CatissuePlusCommonUtil;

import org.apache.commons.lang.StringUtils;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.ApplicationException;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.common.util.XMLPropertyHandler;

public class FormRecordSaveServiceImpl {

	private FormService formSvc;

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}

	private void saveOrUpdateFormRecords(Long formId, String jsonValue, SessionDataBean sessionDataBean)
			throws BizLogicException {

		FormData formData = FormData.fromJson(jsonValue, formId);

		SaveFormDataEvent req = new SaveFormDataEvent();
		req.setFormData(formData);
		req.setFormId(formId);
		req.setRecordId(formData.getRecordId());
		req.setSessionDataBean(sessionDataBean);

		FormDataEvent resp = formSvc.saveFormData(req);
		if (resp.getStatus() == EventStatus.OK) {
			return;
		}
		throw new BizLogicException(null, null, resp.getMessage());
	}

	public void saveDerivativeEvent(SpecimenDTO specimenDTO, SessionDataBean sessionDataBean) throws BizLogicException {
		try {
			String formId = XMLPropertyHandler.getValue("derivativeFormIdentifier");
			String formContextId = XMLPropertyHandler.getValue("derivativeEventFormContextId");

			if (!StringUtils.isBlank(formId) && !StringUtils.isBlank(formContextId)) {
				String derivativeEventJsonString = CatissuePlusCommonUtil.getDerivativeEventJsonString(specimenDTO,
						sessionDataBean.getUserId(), Long.parseLong(formContextId));
				saveOrUpdateFormRecords(Long.parseLong(formId), derivativeEventJsonString, sessionDataBean);
			}
		}
		catch (ApplicationException exception) {
			throw new BizLogicException(exception.getErrorKey(), exception, exception.getMsgValues(), exception.getMessage());
		}
		catch (Exception e) {
			throw new BizLogicException(null, null, e.getMessage());
		}

	}

	public void saveAliquotEvent(SpecimenDTO specimenDTO, int aliquotCount, SessionDataBean sessionDataBean)
			throws BizLogicException {
		try {
			String formId = XMLPropertyHandler.getValue("aliquotFormIdentifier");
			String formContextId = XMLPropertyHandler.getValue("aliquotEventFormContextId");

			if (!StringUtils.isBlank(formId) && !StringUtils.isBlank(formContextId)) {
				String aliquotEventJsonString = CatissuePlusCommonUtil.getAliquotEventJsonString(specimenDTO,
						sessionDataBean.getUserId(), aliquotCount, Long.parseLong(formContextId));
				saveOrUpdateFormRecords(Long.parseLong(formId), aliquotEventJsonString, sessionDataBean);
			}
		}
		catch (ApplicationException exception) {
			throw new BizLogicException(exception.getErrorKey(), exception, exception.getMsgValues(), exception.getMessage());
		}
		catch (Exception e) {
			throw new BizLogicException(null, null, e.getMessage());
		}

	}

}
