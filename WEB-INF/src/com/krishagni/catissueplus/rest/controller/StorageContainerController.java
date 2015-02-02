package com.krishagni.catissueplus.rest.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;

@Controller
@RequestMapping("/storage-containers")
public class StorageContainerController {
	
	@Autowired
	private StorageContainerService storageContainerSvc;

	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public List<StorageContainerSummary> getStorageContainers(
			@RequestParam(value = "name", required = false, defaultValue = "") 
			String name,
			
			@RequestParam(value = "maxResults", required = false, defaultValue = "100") 
			int maxResults,
			
			@RequestParam(value = "specimenId", required = false) 
			Long specimenId,
			
			@RequestParam(value = "onlyFreeContainers", required = false, defaultValue = "false") 
			Boolean onlyFreeContainers) {
		
		ReqStorageContainersEvent req = new ReqStorageContainersEvent();
		req.setMaxResults(maxResults);
		req.setName(name);
		req.setSpecimenId(specimenId);
		req.setOnlyFreeContainers(onlyFreeContainers);
		
		StorageContainersEvent resp = storageContainerSvc.getStorageContainers(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainers();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public StorageContainerDetail getStorageContainerById(
			@PathVariable 
			Long id,
			
			@RequestParam(value = "includeOccupiedPositions", required = false, defaultValue = "false") 
			Boolean includeOccupiedPositions) {
		
		ReqStorageContainerEvent req = new ReqStorageContainerEvent();
		req.setId(id);
		req.setIncludeOccupiedPositions(includeOccupiedPositions);
		
		StorageContainerEvent resp = storageContainerSvc.getStorageContainer(req);
		if (!resp.isSuccess()) {
			resp.raiseException();
		}
		
		return resp.getContainer();
	}
}