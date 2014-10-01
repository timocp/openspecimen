
package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.de.events.FormDataEvent;
import com.krishagni.catissueplus.core.de.events.SaveFormDataEvent;
import com.krishagni.catissueplus.core.de.services.FormService;

import edu.common.dynamicextensions.napi.FormData;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.exception.BizLogicException;

public class FormRecordSaveServiceImpl {

	private FormService formSvc;

	public void setFormSvc(FormService formSvc) {
		this.formSvc = formSvc;
	}

	public void saveOrUpdateFormRecords(Long formId, String jsonValue, SessionDataBean sessionDataBean)
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
}
