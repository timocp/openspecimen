package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentDetail;
import com.krishagni.catissueplus.core.biospecimen.repository.ConsentListCriteria;
import com.krishagni.catissueplus.core.biospecimen.services.ConsentService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/consents")
public class ConsentsController {
	
	@Autowired
	private ConsentService consentSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<ConsentDetail> getConsentsTypes(
		@RequestParam(value="code", required = false)
		String code,
		
		@RequestParam(value="statement", required = false)
		String statement,

		@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
		int maxResults) {

		ConsentListCriteria crit = new ConsentListCriteria()
			.code(code)
			.statement(statement)
			.maxResults(maxResults);
		
		RequestEvent<ConsentListCriteria> req = new RequestEvent<ConsentListCriteria>(crit);
		ResponseEvent<List<ConsentDetail>> resp = consentSvc.getConsents(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.GET, value="{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public ConsentDetail getConsent(@PathVariable("id") Long id) {
		RequestEvent<Long> req = new RequestEvent<Long>(id);
		ResponseEvent<ConsentDetail> resp = consentSvc.getConsent(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentDetail createConsent(@RequestBody ConsentDetail detail) {
		RequestEvent<ConsentDetail> req = new RequestEvent<ConsentDetail>(detail);
		ResponseEvent<ConsentDetail> resp = consentSvc.createConsent(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}
	
	@RequestMapping(method = RequestMethod.PUT, value="{id}")
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ConsentDetail updateConsent(
		@PathVariable("id")
		Long id,
		
		@RequestBody ConsentDetail detail) {
		detail.setId(id);
		RequestEvent<ConsentDetail> req = new RequestEvent<ConsentDetail>(detail);
		ResponseEvent<ConsentDetail> resp = consentSvc.updateConsent(req);
		
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

}
