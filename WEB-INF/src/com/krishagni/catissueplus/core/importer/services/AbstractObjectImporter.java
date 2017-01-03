package com.krishagni.catissueplus.core.importer.services;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.importer.events.ImportObjectDetail;
import com.krishagni.importer.services.ObjectImporter;

public abstract class AbstractObjectImporter<I, O> implements ObjectImporter<I, O> {

	@Override
	public O importObject(ImportObjectDetail<I> importObjectDetail) {
		ResponseEvent<O> resp = importObject(new RequestEvent<ImportObjectDetail<I>>(importObjectDetail));
		resp.throwErrorIfUnsuccessful();
		return resp.getPayload();
	}

	protected abstract ResponseEvent<O> importObject(RequestEvent<ImportObjectDetail<I>> req);
}
