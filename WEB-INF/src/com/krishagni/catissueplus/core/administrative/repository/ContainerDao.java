package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;
import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.StorageContainer;


public interface ContainerDao extends Dao<StorageContainer>{

	StorageContainer getContainer(String containerName);
	List<BoxScanner> getBoxScannerList();

}
