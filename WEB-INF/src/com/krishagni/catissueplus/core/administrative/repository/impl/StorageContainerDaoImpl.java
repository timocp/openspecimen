
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.StorageContainer;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {
	@Override
	public StorageContainer getStorageContainer(Long id) {
		return (StorageContainer) sessionFactory.getCurrentSession().get(StorageContainer.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StorageContainer> getAllStorageContainers(String name, int maxResults) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(StorageContainer.class);
		if (name != null && !name.trim().isEmpty()) {
			query.add(Restrictions.ilike("name", name.trim(), MatchMode.ANYWHERE));
		}
		
		if (maxResults <= 0) {
			maxResults = 200;
		}
		
		query.setMaxResults(maxResults);
		return query.list();
	}
}
