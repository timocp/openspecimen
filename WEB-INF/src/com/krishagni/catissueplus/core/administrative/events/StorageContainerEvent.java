package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class StorageContainerEvent extends ResponseEvent {

	private StorageContainerDetail container;

	private Long id;

	public StorageContainerDetail getContainer() {
		return container;
	}

	public void setContainer(StorageContainerDetail container) {
		this.container = container;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public static StorageContainerEvent ok(StorageContainerDetail detail) {
		StorageContainerEvent event = new StorageContainerEvent();
		event.setContainer(detail);
		event.setStatus(EventStatus.OK);
		return event;
	}

	public static StorageContainerEvent serverError(Throwable... t) {
		Throwable t1 = t != null && t.length > 0 ? t[0] : null;
		StorageContainerEvent resp = new StorageContainerEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(t1);
		resp.setMessage(t1 != null ? t1.getMessage() : null);
		return resp;
	}

	public static StorageContainerEvent notFound(Long id) {
		StorageContainerEvent resp = new StorageContainerEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setId(id);
		return resp;
	}
}
