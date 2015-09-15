
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.krishagni.catissueplus.core.audit.AuditService;
import com.krishagni.catissueplus.core.audit.events.AuditDetail;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/audit")
public class AuditController {
	@Autowired
	private AuditService auditSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public Object getAuditDetail(
		@RequestParam(value="entityType", required=true) String entityType,
		@RequestParam(value="entityId", required=true) Long entityId,
		@RequestParam(value = "maxRecs", required = false, defaultValue = "25") int maxRecs,
		@RequestParam(value = "startAt", required = false, defaultValue = "0") int  startAt,
		@RequestParam(value="detailed", required=false, defaultValue="true") boolean detailed){
			
		RequestAudit req = new RequestAudit();
		req.setEntityType(entityType);
		req.setEntityId(entityId);
		req.setDetailed(detailed);
		
		req.setMaxRecs(maxRecs);
		req.setStartAt(startAt);
		ResponseEvent<List<AuditDetail>> resp = auditSvc.getDetailedAudit(getRequest(req));
		return resp.getPayload();
	}
	
	private <T> RequestEvent<T> getRequest(T payload) {
		return new RequestEvent<T>(payload);
	}
}
