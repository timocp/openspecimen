
package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.auth.events.DomainDetail;
import com.krishagni.catissueplus.core.auth.events.ListAuthDomainCriteria;
import com.krishagni.catissueplus.core.auth.services.DomainRegistrationService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

@Controller
@RequestMapping("/auth-domains")
public class AuthDomainController {

	@Autowired
	private DomainRegistrationService domainRegService;

	@Autowired
	private HttpServletRequest httpServletRequest;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public List<DomainDetail> getAuthDomains(
			@RequestParam(value = "maxResults", required = false, defaultValue = "1000") 
			int maxResults) {
		
		ListAuthDomainCriteria crit = new ListAuthDomainCriteria().maxResults(maxResults);
		RequestEvent<ListAuthDomainCriteria> req = new RequestEvent<ListAuthDomainCriteria>(null, crit);
		ResponseEvent<List<DomainDetail>> resp = domainRegService.getDomains(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public DomainDetail registerDomain(@RequestBody DomainDetail domainDetail) {
		RequestEvent<DomainDetail> req = new RequestEvent<DomainDetail>(null, domainDetail);
		ResponseEvent<DomainDetail> resp = domainRegService.registerDomain(req);
		resp.throwErrorIfUnsuccessful();
		
		return resp.getPayload();
	}
}