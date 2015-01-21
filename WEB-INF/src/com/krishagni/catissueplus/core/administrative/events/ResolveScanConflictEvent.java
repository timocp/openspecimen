
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ResolveScanConflictEvent extends RequestEvent {

	private ScanStorageContainerDetails details;

	public ScanStorageContainerDetails getDetails() {
		return details;
	}

	public void setDetails(ScanStorageContainerDetails details) {
		this.details = details;
	}

}
