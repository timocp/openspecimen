package com.krishagni.catissueplus.core.importer.services;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.importer.events.FileRecordsDetail;
import com.krishagni.importer.events.ImportDetail;
import com.krishagni.importer.events.ImportJobDetail;
import com.krishagni.importer.events.ObjectSchemaCriteria;
import com.krishagni.importer.repository.ListImportJobsCriteria;


public interface ImportServiceWrapper {
	ResponseEvent<List<ImportJobDetail>> getImportJobs(RequestEvent<ListImportJobsCriteria> req);

	ResponseEvent<ImportJobDetail> getImportJob(RequestEvent<Long> req);

	ResponseEvent<String> getImportJobFile(RequestEvent<Long> req);

	ResponseEvent<String> uploadImportJobFile(RequestEvent<InputStream> in);

	ResponseEvent<ImportJobDetail> importObjects(RequestEvent<ImportDetail> req);

	ResponseEvent<String> getInputFileTemplate(RequestEvent<ObjectSchemaCriteria> req);

	ResponseEvent<List<Map<String, Object>>> processFileRecords(RequestEvent<FileRecordsDetail> req);

	ResponseEvent<ImportJobDetail> stopJob(RequestEvent<Long> req);
}