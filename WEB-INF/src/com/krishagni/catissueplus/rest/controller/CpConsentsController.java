package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.CpConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.CpConsentListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.CpConsentService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/cp-consents")
public class CpConsentsController {
	
	@Autowired
	private CpConsentService consentSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<CpConsentDetail> getCpConsentsTypes(
		@RequestParam(value="code", required = false)
		String code,
		
		@RequestParam(value="statement", required = false)
		String statement,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
		int maxResults) {

		CpConsentListCriteria crit = new CpConsentListCriteria()
			.code(code)
			.statement(statement)
			.maxResults(maxResults);
		
		RequestEvent<CpConsentListCriteria> req = new RequestEvent<CpConsentListCriteria>(crit);
		ResponseEvent<List<CpConsentDetail>> resp = consentSvc.getCpConsents(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public CpConsentDetail createCpConsent(@RequestBody CpConsentDetail detail) {
		RequestEvent<CpConsentDetail> req = new RequestEvent<CpConsentDetail>(detail);
		ResponseEvent<CpConsentDetail> resp = consentSvc.createCpConsent(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

}
