package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.StorageContainer;

public interface StorageContainerDao extends Dao<StorageContainer> {
	public StorageContainer getStorageContainer(Long id);

	public List<StorageContainer> getAllStorageContainers(String name, int maxResults);
}
	