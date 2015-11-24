package com.krishagni.catissueplus.core.de.services;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.de.events.CpCatalogSettingDetail;

public interface CatalogService {
	public ResponseEvent<CpCatalogSettingDetail> getCpSetting(RequestEvent<CollectionProtocolSummary> req);
		
	public ResponseEvent<CpCatalogSettingDetail> saveCpSetting(RequestEvent<CpCatalogSettingDetail> req);
	
	public ResponseEvent<CpCatalogSettingDetail> deleteCpSetting(RequestEvent<CollectionProtocolSummary> req);	
}
