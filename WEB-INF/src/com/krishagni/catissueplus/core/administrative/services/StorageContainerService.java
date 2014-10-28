
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;

public interface StorageContainerService {
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent event);
	
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent event);
}
