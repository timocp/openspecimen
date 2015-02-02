package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerPatchDetails;


public interface StorageContainerFactory {

	public StorageContainer createStorageContainer (StorageContainerDetail details);
	
	public StorageContainer patchStorageContainer(StorageContainer oldStorageContainer, StorageContainerPatchDetails details);

}
