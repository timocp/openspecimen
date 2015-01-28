package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.StorageContainerSummary;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.Specimen;
import edu.wustl.catissuecore.domain.StorageContainer;

public interface StorageContainerDao extends Dao<StorageContainer> {
	public StorageContainer getStorageContainer(Long id);

	public List<StorageContainerSummary> getStorageContainers(String name, int maxResults, Long specimenId, boolean onlyFreeContainers);
	
	//
	// TODO: This should go away. It is present because StorageContainer still
	// uses old Specimen. Instead of polluting new SpecimenDao with old Specimen
	// object, i thought it is better to have this method here
	//
	public Specimen getSpecimen(Long specimenId);
}
	