package com.krishagni.catissueplus.core.biospecimen.services;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenKit;
import com.krishagni.catissueplus.core.biospecimen.events.SpecimenKitDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface SpecimenKitService {

    ResponseEvent<SpecimenKitDetail> getSpecimenKit(RequestEvent<Long> req);

    ResponseEvent<SpecimenKitDetail> createSpecimenKit(RequestEvent<SpecimenKitDetail> req);

    ResponseEvent<SpecimenKitDetail> updateSpecimenKit(RequestEvent<SpecimenKitDetail> req);

    //
    // internal APIs
    //
    SpecimenKit createSpecimenKit(SpecimenKitDetail kitDetail, List<Specimen> specimens);
}
