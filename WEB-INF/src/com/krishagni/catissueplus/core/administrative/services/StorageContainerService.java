
package com.krishagni.catissueplus.core.administrative.services;

import com.krishagni.catissueplus.core.administrative.events.AllocateSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqSpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionAllocatedEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionEvent;
import com.krishagni.catissueplus.core.administrative.events.SpecimenPositionUpdatedEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.ReqStorageContainersEvent;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerEvent;
import com.krishagni.catissueplus.core.administrative.events.UpdateSpecimenPositionEvent;

public interface StorageContainerService {
	public StorageContainersEvent getStorageContainers(ReqStorageContainersEvent event);
	
	public StorageContainerEvent getStorageContainer(ReqStorageContainerEvent event);
	
	//
	// TODO: These APIs should be present in SpecimenService; but are present here owing to fact 
	// that SpecimenService is using new Specimen object and SpecimenPosition is still
	// on old Specimen and StorageContainer object.
	// 
	
	public SpecimenPositionEvent getSpecimenPosition(ReqSpecimenPositionEvent req);
	
	public SpecimenPositionAllocatedEvent assignSpecimenPosition(AllocateSpecimenPositionEvent req);
	
	public SpecimenPositionUpdatedEvent updateSpecimenPosition(UpdateSpecimenPositionEvent req);
}
