
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class UpdateStorageContainerEvent extends RequestEvent {

	private StorageContainerDetail details;

	public UpdateStorageContainerEvent(StorageContainerDetail details, Long containerId) {
		setDetails(details);
		this.details.setId(containerId);
	}

	public StorageContainerDetail getDetails() {
		return details;
	}

	public void setDetails(StorageContainerDetail details) {
		this.details = details;
	}

}
	