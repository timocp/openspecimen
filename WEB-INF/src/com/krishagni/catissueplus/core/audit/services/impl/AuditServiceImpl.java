package com.krishagni.catissueplus.core.audit.services.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.audit.AuditService;
import com.krishagni.catissueplus.core.audit.domain.CustomRevisionEntity;
import com.krishagni.catissueplus.core.audit.events.AuditDetail;
import com.krishagni.catissueplus.core.audit.events.RequestAudit;
import com.krishagni.catissueplus.core.audit.util.AuditUtil;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolEvent;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;


public class AuditServiceImpl implements AuditService{

	private DaoFactory daoFactory;
	
	private AuditUtil auditUtil;
	
	private static Map<String, String> entityAuditTableMap = getEntityAuditTableMap();
	
	private static Map<String, Class> entityClassMap = getEntityClassMap();
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	public void setAuditUtil(AuditUtil auditUtil) {
		this.auditUtil = auditUtil;
	}
	
	@Override
	@PlusTransactional
	public ResponseEvent<List<AuditDetail>> getDetailedAudit(RequestEvent<RequestAudit> req) {
		RequestAudit reqEntity = req.getPayload();

		return ResponseEvent.response(getEntityDetailedAudit(reqEntity));
	}

	private List<AuditDetail> getEntityDetailedAudit(RequestAudit req) {
		List<AuditDetail> auditDetails = new ArrayList<AuditDetail>();
		
		Long entityId = req.getEntityId();
		String entityType = req.getEntityType();

		String auditTable = entityAuditTableMap.get(entityType.toUpperCase());
		if (auditTable == null) {
			return null;
		}
		
		auditDetails.add(getCreationDetails(entityId, auditTable));
		
		List<Object> revisions = daoFactory.getAuditDao().getRevisions(auditTable, entityId, req.getStartAt(), req.getMaxRecs());
		Class klass = entityClassMap.get(entityType.toUpperCase());
		
		for (int i = 0; i < revisions.size() - 1; i++) { 
			AuditDetail revDetails = new AuditDetail();
			Long current = Long.valueOf(revisions.get(i).toString());
			addGenericDetails(revDetails, auditTable, entityId, current);
			
			Long prev = Long.valueOf(revisions.get(i+1).toString()) ;

			Object currentRevision = auditUtil.getRevisionInstance(klass, current, entityId).getSingleResult();
			Object prevRevision = auditUtil.getRevisionInstance(klass, prev, entityId).getSingleResult();
			
			List<Map<String, Object>> changeSet = getChangeSet(currentRevision, prevRevision);

			if (changeSet.size() > 0) {
				revDetails.setModifiedAttrs(changeSet);
				auditDetails.add(revDetails);
			}
		}
		return auditDetails;
	}

	private AuditDetail getCreationDetails(Long entityId, String auditTable) {
		CustomRevisionEntity creationDetails = daoFactory.getAuditDao().getEntityCreatedDetails(auditTable, entityId);
		AuditDetail creationAudit = new AuditDetail();
		creationAudit.setRevisionId(Long.valueOf(creationDetails.getIdentifier()));
		User user = daoFactory.getUserDao().getById(creationDetails.getUserId());
		creationAudit.setUser(user.getFirstName() + " " + user.getLastName());
		creationAudit.setTime(creationDetails.getTimestamp());
		creationAudit.setAction(creationDetails.getAction());
		return creationAudit;
	}
	
	private void addGenericDetails(AuditDetail revDetails, String auditTable, Long entityId, Long current) {
		CustomRevisionEntity lastModifiedDetails;
		
		if(auditTable != null){
			lastModifiedDetails = daoFactory.getAuditDao().getEntityModifiedDetails(auditTable, entityId, current);
		} else { 
			lastModifiedDetails = daoFactory.getAuditDao().getSubjectRoleLastModifiedDetails(entityId, current);
		}
		
		revDetails.setRevisionId(current);
		User user = daoFactory.getUserDao().getById(lastModifiedDetails.getUserId());
		revDetails.setUser(user.getFirstName() + " " + user.getLastName());
		revDetails.setTime(lastModifiedDetails.getTimestamp());
		revDetails.setAction(lastModifiedDetails.getAction());
	}
	
	private List<Map<String, Object>> getChangeSet(Object currentEntity, Object prevEntity) {
		try {
				return auditUtil.compareObjects(prevEntity, currentEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Map<String, String> getEntityAuditTableMap() {
		final Map<String, String> auditTableMap = new HashMap<String, String>();
		auditTableMap.put("SPECIMEN", "catissue_specimen_aud");
		auditTableMap.put("SITE", "catissue_site_aud");
		auditTableMap.put("COLLECTION_PROTOCOL_EVENT", "catissue_coll_prot_event_aud");
		auditTableMap.put("COLLECTION_PROTOCOL_REGISTRATION", "catissue_coll_prot_reg_aud");
		auditTableMap.put("COLLECTIO_NPROTOCOL", "cat_collection_protocol_aud");
		auditTableMap.put("INSTITUTE", "catissue_institution_aud");
		auditTableMap.put("PARTICIPANT", "catissue_participant_aud");
		auditTableMap.put("STORAGE_CONTAINER", "os_storage_containers_aud");
		auditTableMap.put("USER", "catissue_user_aud");
		auditTableMap.put("VISIT", "cat_specimen_coll_group_aud");
		
		return auditTableMap;
	}
	
	private static Map<String, Class> getEntityClassMap() {
		final Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("SPECIMEN", Specimen.class);
		classMap.put("SITE", Site.class);
		classMap.put("COLLECTION_PROTOCOL_EVENT", CollectionProtocolEvent.class);
		classMap.put("COLLECTION_PROTOCOL_REGISTRATION", CollectionProtocolRegistration.class);
		classMap.put("COLLECTION_PROTOCOL", CollectionProtocol.class);
		classMap.put("INSTITUTE", Institute.class);
		classMap.put("PARTICIPANT", Participant.class);
		classMap.put("STORAGE_CONTAINER", StorageContainer.class);
		classMap.put("USER", User.class);
		classMap.put("VISIT", Visit.class);
		
		return classMap;
	}
	
}
