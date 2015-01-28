package com.krishagni.catissueplus.core.administrative.events;

import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerSummary {
	private Long id;

	private String name;

	private String barcode;

	private String activityStatus;

	private String siteName;
	
	private Long capacity;
	
	private Long occupied;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public Long getCapacity() {
		return capacity;
	}

	public void setCapacity(Long capacity) {
		this.capacity = capacity;
	}

	public Long getOccupied() {
		return occupied;
	}

	public void setOccupied(Long occupied) {
		this.occupied = occupied;
	}

	public static StorageContainerSummary fromDomain(StorageContainer storageContainer) {
		StorageContainerSummary details = new StorageContainerSummary();
		details.setId(storageContainer.getId());
		details.setActivityStatus(storageContainer.getActivityStatus());
		details.setBarcode(storageContainer.getBarcode());
		details.setName(storageContainer.getName());
		details.setSiteName(storageContainer.getSite().getName());
		return details;
	}
}
