package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.SpecimenPosition;

public interface SpecimenPositionDao extends Dao<SpecimenPosition> {
	
	public SpecimenPosition getPosition(Long posId);
	
	public SpecimenPosition getPositionBySpecimenId(Long specimenId);

}
