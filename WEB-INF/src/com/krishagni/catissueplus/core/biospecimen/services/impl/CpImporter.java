package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolDetail;
import com.krishagni.catissueplus.core.biospecimen.services.CollectionProtocolService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.services.AbstractObjectImporter;
import com.krishagni.importer.events.ImportObjectDetail;

public class CpImporter extends AbstractObjectImporter<CollectionProtocolDetail, CollectionProtocolDetail> {
	private CollectionProtocolService cpSvc;

	public void setCpSvc(CollectionProtocolService cpSvc) {
		this.cpSvc = cpSvc;
	}

	@Override
	public ResponseEvent<CollectionProtocolDetail> importObject(RequestEvent<ImportObjectDetail<CollectionProtocolDetail>> req) {
		try {
			ImportObjectDetail<CollectionProtocolDetail> detail = req.getPayload();
			RequestEvent<CollectionProtocolDetail> cpReq = new RequestEvent<CollectionProtocolDetail>(detail.getObject());
			
			if (detail.isCreate()) {
				return cpSvc.createCollectionProtocol(cpReq);
			} else {
				return cpSvc.updateCollectionProtocol(cpReq);
			}
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
	
}