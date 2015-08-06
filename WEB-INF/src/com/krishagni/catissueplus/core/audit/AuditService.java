package com.krishagni.catissueplus.core.audit;

import java.util.List;

import com.krishagni.catissueplus.core.audit.events.AuditDetail;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public interface AuditService {

	public ResponseEvent<List<AuditDetail>> getDetailedAudit(RequestEvent<RequestAudit> req);
}
