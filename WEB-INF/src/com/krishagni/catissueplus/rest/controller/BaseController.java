package com.krishagni.catissueplus.rest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.audit.events.AuditInfo;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
public abstract class BaseController {

	@Autowired
	private AuditService auditSvc;
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}/audit-info")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public AuditInfo getAuditInfo(@PathVariable("id") Long objectId) {
		RequestAudit req = new RequestAudit(getObjectType(), objectId);
		ResponseEvent<AuditInfo> res = auditSvc.getAuditInfo(new RequestEvent<RequestAudit>(req));
		return res.getPayload();
  }

	public abstract String getObjectType();

}
