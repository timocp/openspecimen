package com.krishagni.catissueplus.core.audit.services.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.audit.domain.AuditRevErrorCode;
import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.events.GetRevisionsOp;
import com.krishagni.catissueplus.core.audit.events.RevisionInfo;
import com.krishagni.catissueplus.core.audit.repository.RevisionListCriteria;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.ErrorType;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;
import com.krishagni.catissueplus.core.common.util.EmailUtil;
import com.krishagni.catissueplus.core.common.util.MessageUtil;
import com.krishagni.catissueplus.core.common.util.Utility;
import com.krishagni.rbac.common.errors.RbacErrorCode;

public class AuditServiceImpl implements AuditService {
	private DaoFactory daoFactory;

	private ThreadPoolTaskExecutor taskExecutor;

	private Map<String, Function<Long, Map<String, String>>> revFileHdrProcs = new HashMap<>();

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	@PlusTransactional
	public ResponseEvent<List<RevisionInfo>> getRevisions(RequestEvent<GetRevisionsOp> req) {
		try {
			return ResponseEvent.response(getRevisions(createRevListCriteria(req.getPayload(), true)));
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public ResponseEvent<Boolean> exportRevisions(RequestEvent<GetRevisionsOp> req) {
		try {
			GetRevisionsOp op = req.getPayload();
			exportRevisions(op, AuthUtil.getCurrentUser(), createRevListCriteria(op, false));
			return ResponseEvent.response(true);
		} catch (OpenSpecimenException ose) {
			return ResponseEvent.error(ose);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	public ResponseEvent<File> getRevisionsFile(RequestEvent<String> req) {
		if (!AuthUtil.isAdmin()) {
			throw OpenSpecimenException.userError(RbacErrorCode.ADMIN_RIGHTS_REQUIRED);
		}

		String fileId = req.getPayload();
		try {
			File f = new File(getAuditRevDir(), fileId);
			if (!f.exists()) {
				return ResponseEvent.userError(AuditRevErrorCode.FILE_NOT_FOUND, fileId);
			}


			return ResponseEvent.response(f);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}

	@Override
	@PlusTransactional
	public void insertApiCallLog(UserApiCallLog userAuditLog) {
		daoFactory.getAuditDao().saveOrUpdate(userAuditLog);
	}

	@Override
	@PlusTransactional
	public long getTimeSinceLastApiCall(Long userId, String token) {
		Date lastApiCallTime = daoFactory.getAuditDao().getLatestApiCallTime(userId, token);
		long timeSinceLastApiCallInMilli = Calendar.getInstance().getTime().getTime() - lastApiCallTime.getTime();
		return TimeUnit.MILLISECONDS.toMinutes(timeSinceLastApiCallInMilli);
	}

	@Override
	public void registerRevFileHdrProc(String entityName, Function<Long, Map<String, String>> headerProc) {
		revFileHdrProcs.put(entityName, headerProc);
	}

	private RevisionListCriteria createRevListCriteria(GetRevisionsOp op, boolean latestN) {
		OpenSpecimenException ose = new OpenSpecimenException(ErrorType.USER_ERROR);

		if (!AuthUtil.isAdmin()) {
			ose.addError(AuditRevErrorCode.ADMIN_REQ);
		}

		if (StringUtils.isBlank(op.getEntityName())) {
			ose.addError(AuditRevErrorCode.ENTITY_NAME_REQ);
		}

		if (op.getEntityId() == null) {
			ose.addError(AuditRevErrorCode.ENTITY_ID_REQ);
		}

		if (!latestN && op.getFrom() == null) {
			op.setFrom(Date.from(Instant.EPOCH));
		}

		if (!latestN && op.getTo() == null) {
			op.setTo(Date.from(Instant.now()));
		}

		if (!latestN && op.getFrom() != null && op.getTo() != null && op.getFrom().after(op.getTo())) {
			ose.addError(AuditRevErrorCode.FROM_GT_TO_DT, op.getFrom(), op.getTo());
		}

		if (latestN && op.getMaxRevs() > 100) {
			ose.addError(AuditRevErrorCode.MAX_REVS_LMT_EXCEEDED, op.getMaxRevs());
		}

		ose.checkAndThrow();

		String sql = daoFactory.getAuditDao().getEntityRevisionSql(op.getEntityName());
		if (StringUtils.isBlank(sql)) {
			throw OpenSpecimenException.userError(AuditRevErrorCode.UNKNOWN_ENTITY, op.getEntityName());
		}

		RevisionListCriteria crit = new RevisionListCriteria()
			.entitySql(sql)
			.entityId(op.getEntityId())
			.latestFirst(latestN)
			.startAt(0)
			.maxResults(op.getMaxRevs());

		if (!latestN) {
			crit.from(op.getFrom()).to(op.getTo());
		}

		return crit;
	}

	private void exportRevisions(GetRevisionsOp op, User user, RevisionListCriteria listCriteria) {
		taskExecutor.execute(new Runnable() {
			private CsvWriter writer = null;

			@Override
			public void run() {
				try {
					File file = new File(getAuditRevDir(), UUID.randomUUID().toString());
					writer = CsvFileWriter.createCsvFileWriter(file);

					Function<Long, Map<String, String>> proc = revFileHdrProcs.get(op.getEntityName());
					Map<String, String> headers = new HashMap<>();
					if (proc != null) {
						headers = proc.apply(op.getEntityId());
					}

					writeFileHeaders(headers);
					writeColumnHeaders();

					int startAt = 0, maxRevs = 100;
					boolean endOfRevs = false;

					while (!endOfRevs) {
						List<RevisionInfo> revs = getRevisions(startAt, maxRevs);
						revs.forEach(this::writeRow);

						startAt += revs.size();
						if (revs.size() < maxRevs) {
							endOfRevs = true;
						}
					}

					writer.flush();
					writer.close();
					sendEmail(op, user, headers, file);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(writer);
				}
			}

			@PlusTransactional
			private List<RevisionInfo> getRevisions(int startAt, int maxRevs) {
				return AuditServiceImpl.this.getRevisions(listCriteria.startAt(startAt).maxResults(maxRevs));
			}

			private void writeFileHeaders(Map<String, String> headers) {
				headers.entrySet().forEach(e -> {
					if (e.getKey().startsWith("$")) {
						return;
					}

					writer.writeNext(new String[] {e.getKey(), e.getValue()});
				});
			}

			private void writeColumnHeaders() {
				writer.writeNext(Arrays.stream(REV_FILE_COL_HDRS).map(AuditServiceImpl.this::getDisplayName).toArray(String[]::new));
			}

			private void writeRow(RevisionInfo revInfo) {
				writer.writeNext(new String[]{
					nsToString(revInfo.getId()),
					nsToString(Utility.getDateTimeString(revInfo.getTime())),
					nsToString(revInfo.getUser().getFirstName()) + " " + nsToString(revInfo.getUser().getLastName()),
					nsToString(revInfo.getUser().getLoginName()),
					nsToString(revInfo.getUser().getDomain()),
					nsToString(revInfo.getIpAddress()),
					revInfo.getOp(),
					revInfo.getEntityType(),
					revInfo.getEntityKey(),
					revInfo.getEntityKeyValue()
				});
			}
		});
	}

	private List<RevisionInfo> getRevisions(RevisionListCriteria listCriteria) {
		List<RevisionInfo> revisions = daoFactory.getAuditDao().getRevisions(listCriteria);
		revisions.forEach(rev -> {
			rev.setEntityType(getDisplayName(rev.getEntityType()));
			rev.setEntityKey(getEntityKeyName(rev.getEntityKey()));
			rev.setEntityKeyValue(getEntityKey(rev.getEntityId(), rev.getEntityKeyValue()));
		});

		return revisions;
	}

	private String getDisplayName(String entityName) {
		return MessageUtil.getInstance().getMessage(entityName);
	}

	private String getEntityKeyName(String keyName) {
		if (StringUtils.isBlank(keyName)) {
			keyName = "identifier";

		}

		return MessageUtil.getInstance().getMessage("audit_rev_keys_" + keyName);
	}

	private String getEntityKey(Long entityId, String keyValue) {
		return nsToString(StringUtils.isBlank(keyValue) ? entityId : keyValue);
	}

	private String nsToString(Object obj) {
		if (obj == null) {
			return StringUtils.EMPTY;
		}

		return obj.toString();
	}

	private File getAuditRevDir() {
		File auditDir = new File(ConfigUtil.getInstance().getDataDir() + File.separator + "audit");
		if (!auditDir.exists()) {
			auditDir.mkdirs();
		}

		return auditDir;
	}

	private String getRevFilename(GetRevisionsOp op) {
		return new StringBuilder()
			.append(op.getEntityName()).append("_").append(op.getEntityId()).append("_")
			.append(sdf.format(op.getFrom())).append("_").append(sdf.format(op.getTo()))
			.append(".csv")
			.toString();
	}

	private void sendEmail(GetRevisionsOp op, User user, Map<String, String> headers, File revFile) {
		String entityName = getDisplayName(op.getEntityName());
		String startDate = Utility.getDateString(op.getFrom());
		String endDate = Utility.getDateString(op.getTo());

		Map<String, Object> props = new HashMap<>();
		props.put("entityName", entityName);
		props.put("title",      headers.get("$displayName"));
		props.put("requestor",  user.formattedName());
		props.put("startDate",  startDate);
		props.put("endDate",    endDate);
		props.put("fileId",     revFile.getName());
		props.put("filename",   getRevFilename(op));
		props.put("$subject",   new String[] {entityName, headers.get("$displayName"), startDate, endDate});

		EmailUtil.getInstance().sendEmail(
			REV_EMAIL_TMPL,
			new String[] {user.getEmailAddress()},
			null,
			props);
	}

	private static final String[] REV_FILE_COL_HDRS = {
		"audit_rev_id",
		"audit_rev_date_time",
		"audit_rev_user",
		"audit_rev_login_name",
		"audit_rev_domain_name",
		"audit_rev_ip_address",
		"audit_rev_type",
		"audit_rev_entity_type",
		"audit_rev_entity_key",
		"audit_rev_entity_value"
	};

	private static final String[] OP_NAMES = {"add", "modify", "delete"};

	private static final String REV_EMAIL_TMPL = "audit_obj_revisions";

	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
}
