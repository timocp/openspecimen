package com.krishagni.catissueplus.rest.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.audit.events.GetRevisionsOp;
import com.krishagni.catissueplus.core.audit.events.RevisionInfo;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

import edu.common.dynamicextensions.nutility.IoUtil;
import edu.emory.mathcs.backport.java.util.Collections;

@Controller
@RequestMapping("/audit")
public class AuditController {

	@Autowired
	private AuditService auditSvc;

	@RequestMapping(method = RequestMethod.GET, value="/revisions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<RevisionInfo> getRevisions(
		@RequestParam(value = "entityName")
		String entityName,

		@RequestParam(value = "entityId")
		Long entityId,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100")
		int maxResults) {

		GetRevisionsOp op = new GetRevisionsOp();
		op.setEntityName(entityName);
		op.setEntityId(entityId);
		op.setMaxRevs(maxResults);

		RequestEvent<GetRevisionsOp> req = new RequestEvent<>(op);
		ResponseEvent<List<RevisionInfo>> resp = auditSvc.getRevisions(req);
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST, value="/export-revisions")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public Map<String, Boolean> exportRevisions(@RequestBody GetRevisionsOp revsOp) {
		ResponseEvent<Boolean> resp = auditSvc.exportRevisions(new RequestEvent<>(revsOp));
		resp.throwErrorIfUnsuccessful();
		return Collections.singletonMap("status", resp.getPayload());
	}

	@RequestMapping(method = RequestMethod.GET, value="/revisions-file")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public void downloadRevisionsFile(
		@RequestParam(value = "fileId")
		String fileId,

		@RequestParam(value = "filename", required = false, defaultValue = "audit_revisions.csv")
		String filename,

		HttpServletResponse httpResp) {

		ResponseEvent<File> resp = auditSvc.getRevisionsFile(new RequestEvent<>(fileId));
		
		httpResp.setContentType("text/csv;");
		httpResp.setHeader("Content-Disposition", "attachment;filename=" + filename);

		InputStream in = null;
		try {
			in = new FileInputStream(resp.getPayload());
			IoUtil.copy(in, httpResp.getOutputStream());
		} catch (IOException e) {
			throw new RuntimeException("Error sending file", e);
		} finally {
			IoUtil.close(in);
		}
	}
}