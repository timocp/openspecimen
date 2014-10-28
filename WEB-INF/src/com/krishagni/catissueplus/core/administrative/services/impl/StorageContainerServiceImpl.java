
package com.krishagni.catissueplus.core.administrative.services.impl;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.administrative.services.StorageContainerService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;

import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerServiceImpl implements StorageContainerService {
	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@PlusTransactional
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent req) {		
		List<StorageContainer> containers = daoFactory.getStorageContainerDao()
				.getAllStorageContainers(req.getName(), req.getMaxResults());
		
		List<StorageContainerSummary> details = new ArrayList<StorageContainerSummary>();
		for (StorageContainer container : containers) {
			details.add(StorageContainerSummary.fromDomain(container));
		}
		
		return StorageContainersEvent.ok(details);
	}

	@Override
	@PlusTransactional
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent req) {
		try {
			StorageContainer storageContainer = daoFactory.getStorageContainerDao()
					.getStorageContainer(req.getId());
			return StorageContainerEvent.ok(StorageContainerSummary.fromDomain(storageContainer));
		} catch (Exception e) {
			return StorageContainerEvent.serverError(e);
		}
	}	
}
