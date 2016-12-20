package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenKitDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenKitDaoImpl extends AbstractDao<SpecimenKit> implements SpecimenKitDao {

    @Override
    public Class<SpecimenKit> getType() {
        return SpecimenKit.class;
    }
}
