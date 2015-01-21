package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.biospecimen.domain.BoxScanner;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.StorageContainer;


public class ContainerDaoImpl extends AbstractDao<StorageContainer> implements ContainerDao{

	@Override
	@SuppressWarnings("unchecked")
	public StorageContainer getContainer(String containerName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CONTAINER_BY_NAME);
		query.setString("name", containerName);
		List<StorageContainer> containerList= query.list();
		
		return containerList.isEmpty()?null:containerList.iterator().next();
	}
	
	private static final String FQN = StorageContainer.class.getName();
	private static final String GET_CONTAINER_BY_NAME = FQN + ".getContainerByName";
	private static final String GET_BOX_SCANNER_LIST = FQN+".getAllBoxScanners";
	@Override
	public List<BoxScanner> getBoxScannerList() {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(BoxScanner.class.getName()+".getAllBoxScanners");
		List<BoxScanner> containerList= query.list();
		
		return containerList;
	}

}
