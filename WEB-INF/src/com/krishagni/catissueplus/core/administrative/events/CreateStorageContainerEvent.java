
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class CreateStorageContainerEvent extends RequestEvent {

	private StorageContainerDetail details;
	
	public CreateStorageContainerEvent(StorageContainerDetail details) {
		setDetails(details);
	}

	public StorageContainerDetail getDetails() {
		return details;
	}

	public void setDetails(StorageContainerDetail details) {
		this.details = details;
	}

}
