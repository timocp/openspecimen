package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenPositionDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.domain.SpecimenPosition;

public class SpecimenPositionDaoImpl extends AbstractDao<SpecimenPosition> implements SpecimenPositionDao {

	@Override
	public void saveOrUpdate(SpecimenPosition position) {		
		super.saveOrUpdate(position);
		getSessionFactory().getCurrentSession().flush();
	}


	@Override
	public SpecimenPosition getPosition(Long posId) {
		return (SpecimenPosition)getSessionFactory()
				.getCurrentSession()
				.get(SpecimenPosition.class, posId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SpecimenPosition getPositionBySpecimenId(Long specimenId) {
		List<SpecimenPosition> positions = getSessionFactory()
				.getCurrentSession()
				.createCriteria(SpecimenPosition.class)
				.createAlias("specimen", "specimen")
				.add(Restrictions.eq("specimen.id", specimenId))
				.list();
		
		return positions == null || positions.isEmpty() ? null : positions.iterator().next();
	}
}
