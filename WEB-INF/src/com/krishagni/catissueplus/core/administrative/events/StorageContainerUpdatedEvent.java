package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.errors.ErroneousField;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerUpdatedEvent extends ResponseEvent {

	private StorageContainerDetail storageContainerDetails;

	private Long storageContainerId;

	public StorageContainerDetail getStorageContainerDetails() {
		return storageContainerDetails;
	}

	public void setStorageContainerDetails(StorageContainerDetail storageContainerDetails) {
		this.storageContainerDetails = storageContainerDetails;
	}

	public Long getStorageContainerId() {
		return storageContainerId;
	}

	public void setStorageContainerId(Long storageContainerId) {
		this.storageContainerId = storageContainerId;
	}

	public static StorageContainerUpdatedEvent ok(StorageContainerDetail details) {
		StorageContainerUpdatedEvent event = new StorageContainerUpdatedEvent();
		event.setStorageContainerDetails(details);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static StorageContainerUpdatedEvent invalidRequest(String message, ErroneousField... erroneousField) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setMessage(message);
		resp.setErroneousFields(erroneousField);
		return resp;
	}

	public static StorageContainerUpdatedEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static StorageContainerUpdatedEvent notFound(Long storageContainerId) {
		StorageContainerUpdatedEvent resp = new StorageContainerUpdatedEvent();
		resp.setStorageContainerId(storageContainerId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}
}

