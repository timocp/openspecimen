
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.io.UnsupportedEncodingException;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;
import edu.wustl.common.beans.SessionDataBean;
import flex.messaging.util.URLDecoder;

public class FormRecordSaveServiceImpl {

	private FormService formSvc;

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}

	public String saveOrUpdateFormRecords(Long formId, String jsonValue, SessionDataBean sessionDataBean) {

		try {
			jsonValue = URLDecoder.decode(jsonValue, "UTF-8");
		}
		catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Error parsing input JSON", e);
		}

		if (jsonValue.endsWith("=")) {
			jsonValue = jsonValue.substring(0, jsonValue.length() - 1);
		}

		FormData formData = FormData.fromJson(jsonValue, formId);

		SaveFormDataEvent req = new SaveFormDataEvent();
		req.setFormData(formData);
		req.setFormId(formId);
		req.setRecordId(formData.getRecordId());
		req.setSessionDataBean(sessionDataBean);

		FormDataEvent resp = formSvc.saveFormData(req);
		if (resp.getStatus() == EventStatus.OK) {
			return "Saved successfully !!!";
		}
		return null;
	}
}
