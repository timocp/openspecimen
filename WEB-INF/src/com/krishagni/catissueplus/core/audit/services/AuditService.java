package com.krishagni.catissueplus.core.audit.services;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.GetRevisionsOp;
import com.krishagni.catissueplus.core.audit.events.RevisionInfo;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface AuditService {
	ResponseEvent<List<RevisionInfo>> getRevisions(RequestEvent<GetRevisionsOp> req);

	ResponseEvent<Boolean> exportRevisions(RequestEvent<GetRevisionsOp> req);

	ResponseEvent<File> getRevisionsFile(RequestEvent<String> req);
	
	// Internal APIs
	
	void insertApiCallLog(UserApiCallLog userAuditLog);
	
	long getTimeSinceLastApiCall(Long userId, String token);

	void registerRevFileHdrProc(String entityName, Function<Long, Map<String, String>> headerProc);
}
