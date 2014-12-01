package krishagni.catissueplus.upgrade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import krishagni.catissueplus.dto.FormDetailsDTO;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVWriter;

import com.krishagni.catissueplus.core.de.ui.UserControl;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ndao.JdbcDao;
import edu.common.dynamicextensions.ndao.JdbcDaoFactory;
import edu.common.dynamicextensions.ndao.ResultExtractor;
import edu.common.dynamicextensions.util.DataValueMapUtility;


public class MigrateSppForm extends MigrateForm {
	private static final String RECEIVED_EVENT = "ReceivedEventParameters";
	
	private static final String COLLECTED_EVENT = "CollectionEventParameters";
	
	private static Logger logger = Logger.getLogger(MigrateSppForm.class);
	
	static {
		logger.setLevel(Level.INFO);
	}
	
	public MigrateSppForm(UserContext usrCtx) {
		super(usrCtx);
	}
	
	public MigrateSppForm(UserContext usrCtx, CSVWriter recordsLog) {
		super(usrCtx, recordsLog);
	}
	
	@Override
	public void migrateForm(Long containerId, List<FormInfo> formInfos) 
	throws Exception {		
		oldForm = getOldFormDefinition(containerId);
		
		if (oldForm == null) {
			return;
		}

		String caption = oldForm.getCaption();
		if (caption.equals(COLLECTED_EVENT)) {
			migrateStaticEventData(oldForm, formInfos, new CollectionEventDataMigrator());
		} else if (caption.equals(RECEIVED_EVENT)) {
			migrateStaticEventData(oldForm, formInfos, new ReceivedEventDataMigrator());
		} else {
			super.migrateForm(containerId, formInfos);
		}
	}
	
	@Override
	protected FormMigrationCtxt getNewFormDefinition(ContainerInterface oldForm) 
	throws Exception {
		FormMigrationCtxt formMigrationCtxt = super.getNewFormDefinition(oldForm);
		
		Container newForm = formMigrationCtxt.newForm;
		addCommonSppFields(newForm);
		newForm.setCaption(newForm.getCaption());
		return formMigrationCtxt;
	}
	
	@Override
	protected FormData getFormData(
			JdbcDao jdbcDao, RecordObject recObj, 
			FormMigrationCtxt formMigrationCtxt, ContainerInterface oldForm,
			Map<BaseAbstractAttributeInterface, Object> oldFormData) {
		FormData formData = super.getFormData(jdbcDao, recObj, formMigrationCtxt, oldForm, oldFormData);		
		Map<String, Object> fieldValues = getCommonSpeFields(jdbcDao, recObj.recordId);
		addCommonSppFieldValues(formData, fieldValues);
		return formData;
	}

	@Override
	protected List<RecordObject> getRecordAndObjectIds(Long oldCtxId) {										
		return JdbcDaoFactory.getJdbcDao().getResultSet(
				GET_RECORD_AND_SPECIMEN_IDS_SQL, 
				Collections.singletonList(oldCtxId),
				
				new ResultExtractor<List<RecordObject>>() {
					@Override
					public List<RecordObject> extract(ResultSet rs)
					throws SQLException {
						List<RecordObject> recAndObjectIds = new ArrayList<RecordObject>();

						while (rs.next()) {
							RecordObject recObj = new RecordObject();
							recObj.recordId = rs.getLong(1);
							recObj.objectId = rs.getLong(2);
							if (recObj.objectId == -1) {
								continue;
							}

							recAndObjectIds.add(recObj);
						}

						return recAndObjectIds;
					}
		});
	}

	@Override
	protected void attachForm(JdbcDao jdbcDao, Long newFormId, List<FormInfo> formInfos)
	throws Exception {
		Long formCtxId = null;
		String entityType = "SpecimenEvent";
		Long cpId = -1L;
		
		for (FormInfo info : formInfos) {			
			if (formCtxId == null) {
				FormDetailsDTO dto = new FormDetailsDTO();
				dto.setContainerId(newFormId);
				dto.setEntityType(entityType);
				dto.setCpId(cpId);
								
				insertForm(dto);
				formCtxId = dto.getId();
			} 
						
			info.setNewFormCtxId(formCtxId);
			migrateFormData(info);			
		}
	}
	
	private Map<String, Object> getCommonSpeFields(JdbcDao jdbcDao, Long recordId) {
		return jdbcDao.getResultSet(
				GET_SPP_COMMON_FIELDS_SQL, 
				Collections.singletonList(recordId), 
				new ResultExtractor<Map<String, Object>>() {

					@Override
					public Map<String, Object> extract(ResultSet rs) throws SQLException {
						Map<String, Object> fields = new HashMap<String, Object>();
						
						if (!rs.next()) {
							return fields;
						}
												
						fields.put(DEVIATION, rs.getString(1));
						fields.put(DATETIME, rs.getTimestamp(2));
						fields.put(USER, rs.getLong(3));
						fields.put(COMMENTS, rs.getString(4));
						return fields;
					}
				}
		);		
	}
	
	private void addCommonSppFields(Container newForm) {		
		for (Control ctrl : newForm.getControls()) {
			ctrl.setSequenceNumber(ctrl.getSequenceNumber() + 5);
		}
		
		for (Control ctrl : getCommonSppFields()) {
			newForm.addControl(ctrl);
		}		
	}
	
	private Control[] getCommonSppFields() {
		return new Control[] {
				getUserField(1),
				getDateTimeField(2),
				getReasonForDeviationField(3),
				getCommentsField(4)
		};
	}
	
	private Control getUserField(int rowNo) {
		Control user = new UserControl();
		user.setName(USER);
		user.setUserDefinedName(USER);
		user.setCaption("User");
		user.setSequenceNumber(rowNo);
		user.setxPos(0);
		user.setMandatory(true);
		return user;		
	}
	
	private Control getDateTimeField(int rowNo) {
		DatePicker dateTime = new DatePicker();
		dateTime.setName(DATETIME);
		dateTime.setUserDefinedName(DATETIME);
		dateTime.setCaption("Date and Time");
		dateTime.setSequenceNumber(rowNo);
		dateTime.setxPos(0);
		dateTime.setFormat("MM-dd-yyyy HH:mm");
		dateTime.setMandatory(true);
		return dateTime;		
	}
	
	private Control getReasonForDeviationField(int rowNo) {
		TextArea deviation = new TextArea();
		deviation.setName(DEVIATION);
		deviation.setUserDefinedName(DEVIATION);
		deviation.setCaption("Reason for Deviation");
		deviation.setSequenceNumber(rowNo);
		deviation.setxPos(0);
		deviation.setNoOfRows(2);
		return deviation;		
	}
	
	private Control getCommentsField(int rowNo) {
		TextArea comments = new TextArea();
		comments.setName(COMMENTS);
		comments.setUserDefinedName(COMMENTS);
		comments.setCaption("Comments");
		comments.setSequenceNumber(rowNo);
		comments.setxPos(0);
		comments.setNoOfRows(2);
		return comments;
	}
		
	private void addCommonSppFieldValues(FormData formData, Map<String, Object> fieldValues) {
		for (Map.Entry<String, Object> fieldValue : fieldValues.entrySet()) {
			addCommonSppFieldValue(formData, fieldValue.getKey(), fieldValue.getValue());
		}
	}
	
	private void addCommonSppFieldValue(FormData formData, String fieldName, Object value) {
		Control ctrl = formData.getContainer().getControl(fieldName);
		formData.addFieldValue(new ControlValue(ctrl, ctrl.toString(value)));
	}
	
	private void migrateStaticEventData(ContainerInterface oldForm, List<FormInfo> formInfos, StaticEventDataMigrator staticMigrator) 
	throws Exception {				
		for (FormInfo info : formInfos) {			
			long t1 = System.currentTimeMillis();
			
			List<RecordObject> recAndObjectIds = getRecordAndObjectIds(info.getOldFormCtxId());
			logger.info("Number of records to migrate for form : " + oldForm.getCaption() + "(" + oldForm.getId() + ")" + 
					" with form context id : " + info.getOldFormCtxId() + " is : " + recAndObjectIds.size());
			
			if (recAndObjectIds.size() == 0) {
				continue;
			}
			
			EntityInterface entity = (EntityInterface)oldForm.getAbstractEntity();
			String tableName = entity.getTableProperties().getName();			
			String recordIdCol = getRecordIdCol(info, entity.getId(), tableName);
			if (recordIdCol == null) {
				throw new RuntimeException("Could not determine record id column for " + tableName + 
						", old container : " + oldForm.getId());
			}
			
			logger.info("Using record ID column: " + recordIdCol + 
					" for table: " + tableName +
					" while migrating: " + oldForm.getId());
			
			for (RecordObject recObj : recAndObjectIds) {				
				try {
					Long oldRecId = getRecordId(tableName, recordIdCol, recObj.recordId); 
					if (oldRecId == null) {
						continue;
					}

					Map<AbstractAttributeInterface, Object> entityRec = EntityManager.getInstance().getRecordById(entity, oldRecId);
					Map<BaseAbstractAttributeInterface, Object> record = new HashMap<BaseAbstractAttributeInterface, Object>(entityRec);
					
					DataValueMapUtility.updateDataValueMapDataLoading(record, oldForm);					
					staticMigrator.migrate(recObj, record);
				} catch (DynamicExtensionsSystemException e) {
					if (!e.getMessage().startsWith("Exception in execution query :: Unhooked data present in database for recordEntryId")) {
						throw e;
					} else {
						log(recObj.recordId, null, e.getMessage());
					}
					logger.warn(e.getMessage());
				}
			}
			
			logger.info("Migrated records for form: " + oldForm.getCaption() + "(" + oldForm.getId() + ")" + 
					", number of records = " + recAndObjectIds.size() + 
					", time = " + (System.currentTimeMillis() - t1) / 1000 + " seconds"); 			
		}			
	}
	
	private static final String GET_RECORD_AND_SPECIMEN_IDS_SQL =
			"select " +
			"	re.identifier, spe_re.specimen_id " +
			"from " +
			"   dyextn_abstract_form_context afc " +
			"   inner join dyextn_abstract_record_entry re on re.abstract_form_context_id = afc.identifier " +
			"   inner join catissue_action_application spe_re on spe_re.action_app_record_entry_id = re.identifier " +
			"where " + 
			"   afc.identifier = ? " +
			"order by " +
			"   re.modified_date";
	
	private static final String GET_SPP_COMMON_FIELDS_SQL =
			"select " +
			"  aa.reason_deviation, aa.timestamp, aa.user_details, aa.comments " +
			"from " +
			"  catissue_abstract_application aa " +
			"  inner join catissue_action_application spe_re on spe_re.identifier = aa.identifier " +
			"where " +
			"  spe_re.action_app_record_entry_id = ?";
	
	private static final String DEVIATION = "deviationReason";
	
	private static final String USER = "user";
	
	private static final String DATETIME = "dateTime";
	
	private static final String COMMENTS = "comments";
	
	private interface StaticEventDataMigrator {
		public void migrate(RecordObject recObj, Map<BaseAbstractAttributeInterface, Object> record)
		throws Exception;
	}
	
	private abstract class AbstractStaticEventDataMigrator implements StaticEventDataMigrator {
		private static final String INSERT_SPECIMEN_EVENT_PARAM_REC_SQL = 
				"insert into " + 
				"  catissue_specimen_event_param(identifier, specimen_id, event_timestamp, user_id) " +
				"values " +
				"  (catissue_spec_event_param_seq.nextval, ?, ?, ?)";
		
		protected Long insertSpecimenEventParam(JdbcDao jdbcDao, Long specimenId, Map<String, Object> commonSpeVals) 
		throws Exception {
			List<Object> params = new ArrayList<Object>();
			params.add(specimenId);
			params.add((Date)commonSpeVals.get(DATETIME));
			params.add((Long)commonSpeVals.get(USER));
			
			Number key = jdbcDao.executeUpdateAndGetKey(INSERT_SPECIMEN_EVENT_PARAM_REC_SQL, params, "identifier");
			return key != null ? key.longValue() : null;					
		}
		
		public void migrate(RecordObject recObj, Map<BaseAbstractAttributeInterface, Object> record) 
		throws Exception {
			JdbcDao jdbcDao = JdbcDaoFactory.getJdbcDao();
			if (isDuplicate(jdbcDao, recObj.objectId)) {
				return;
			}

			Map<String, Object> commonSpeVals = getCommonSpeFields(jdbcDao, recObj.recordId);
			if (commonSpeVals.get(USER) == null) {
				return;
			}
			
			Long speId = insertSpecimenEventParam(jdbcDao, recObj.objectId, commonSpeVals);			
			Map<String, Object> eventData = new HashMap<String, Object>(commonSpeVals);
			for (Map.Entry<BaseAbstractAttributeInterface, Object> fieldVal : record.entrySet()) {
				eventData.put(fieldVal.getKey().getName(), fieldVal.getValue());
			}
			
			insertEventData(jdbcDao, recObj, speId, eventData);						
		}

		public boolean isDuplicate(JdbcDao jdbcDao, Long specimenId) {
			return jdbcDao.getResultSet(getDupCheckSql(), Collections.singletonList(specimenId), 
					new ResultExtractor<Boolean>() {
						@Override
						public Boolean extract(ResultSet rs)
						throws SQLException {				
							return rs.next() && rs.getInt(1) > 0;
						}
					});
			
		}
				
		public abstract void insertEventData(JdbcDao jdbcDao, RecordObject recObj, Long speId, Map<String, Object> eventData)
		throws Exception;
		
		public abstract String getDupCheckSql();
	}
	
	private class CollectionEventDataMigrator extends AbstractStaticEventDataMigrator {
		private static final String INSERT_COLL_EVENT_SQL = 
				"insert into " +
				"  catissue_coll_event_param(collection_procedure, container, event_timestamp, specimen_id, user_id, comments, spe_id) " +
				"values " +
				"  (?, ?, ?, ?, ?, ?, ?)";
		
		private static final String CHECK_SPECIMEN_ID_SQL = 
				"select count(specimen_id) from catissue_coll_event_param where specimen_id = ?";
     
		@Override
		public String getDupCheckSql() {
			return CHECK_SPECIMEN_ID_SQL;
		}
		
		@Override
		public void insertEventData(JdbcDao jdbcDao, RecordObject recObj, Long speId, Map<String, Object> eventData) 
		throws Exception {
			List<Object> params = new ArrayList<Object>();			
			params.add(eventData.get("collectionProcedure"));
			params.add(eventData.get("container"));
			params.add(eventData.get(DATETIME));
			params.add(recObj.objectId);
			params.add(eventData.get(USER));
			params.add(eventData.get(COMMENTS));
			params.add(speId);
			
			Number newRecordId = jdbcDao.executeUpdateAndGetKey(INSERT_COLL_EVENT_SQL, params, "identifier");
			log(recObj.recordId, newRecordId != null ? newRecordId.longValue() : -1L, "");			
		}
	}
	
	private class ReceivedEventDataMigrator extends AbstractStaticEventDataMigrator {
		private static final String INSERT_RCVD_EVENT_SQL = 
				"insert into " +
				"  catissue_received_event_param(received_quality, event_timestamp, specimen_id, user_id, comments, spe_id) " +
				"values " + 
				"  (?, ?, ?, ?, ?, ?)";

		private static final String CHECK_SPECIMEN_ID_SQL = 
				"select count(specimen_id) from catissue_received_event_param where specimen_id = ?";
		
		@Override
		public String getDupCheckSql() {
			return CHECK_SPECIMEN_ID_SQL;
		}
		
		@Override
		public void insertEventData(JdbcDao jdbcDao, RecordObject recObj, Long speId, Map<String, Object> eventData) 
		throws Exception {
			List<Object> params = new ArrayList<Object>();			
			params.add(eventData.get("receivedQuality"));
			params.add(eventData.get(DATETIME));
			params.add(recObj.objectId);
			params.add(eventData.get(USER));
			params.add(eventData.get(COMMENTS));
			params.add(speId);
			
			Number newRecordId = jdbcDao.executeUpdateAndGetKey(INSERT_RCVD_EVENT_SQL, params, "identifier");
			log(recObj.recordId, newRecordId != null ? newRecordId.longValue() : -1L, "");			
		}
	}
	
	public String getFormCaption() {
		if (oldForm.getCaption().equals(COLLECTED_EVENT)) {
			return "Collection Event";
		} else if (oldForm.getCaption().equals(RECEIVED_EVENT)) {
			return "Received Event";
		}
		
		return super.getFormCaption();
	}
}
