package com.krishagni.catissueplus.core.audit.services.impl;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.audit.domain.UserApiCallLog;
import com.krishagni.catissueplus.core.audit.services.AuditService;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.PlusTransactional;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.ConfigUtil;
import com.krishagni.catissueplus.core.common.util.CsvFileWriter;
import com.krishagni.catissueplus.core.common.util.CsvWriter;

public class AuditServiceImpl implements AuditService {
	
	private DaoFactory daoFactory;

	private ThreadPoolTaskExecutor taskExecutor;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public void setTaskExecutor(ThreadPoolTaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Override
	@PlusTransactional
	public void exportRevisionData(String entity, Long entityId, Date from, Date to) {
		try {
			if (AuthUtil.isAdmin()) {
				// only admin can export audit data
				return;
			}

			if (from == null) {
				// throw from cannot be null;
				return;
			}

			if (to == null) {
				// throw to cannot be null;
				return;
			}

			if (from.after(to)) {
				// throw from cannot be later than to
				return;
			}

			if (entityId == null) {
				// entity id cannot be null;
				return;
			}

			String sql = daoFactory.getAuditDao().getEntityRevisionSql(entity);
			if (StringUtils.isBlank(sql)) {
				// throw unknown entity name;
			}

			exportRevisionData(AuthUtil.getCurrentUser(), entity, entityId, from, to, sql);
		} catch (OpenSpecimenException ose) {
			throw ose;
		} catch (Exception e) {
			throw OpenSpecimenException.serverError(e);
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


	private void exportRevisionData(User user, String entity, Long entityId, Date from, Date to, String sql) {
		taskExecutor.execute(new Runnable() {
			private CsvWriter writer = null;

			@Override
			public void run() {
				try {
					File file = getOutputFile();
					writer = CsvFileWriter.createCsvFileWriter(file);
					writeHeader();

					int startAt = 0, maxRevs = 100;
					boolean endOfRevs = false;

					Map<String, Object> lastRev = null;
					while (!endOfRevs) {
						List<Map<String, Object>> revs = getRevisions(startAt, maxRevs);
						if (revs.size() < maxRevs) {
							endOfRevs = true;
						}

						for (Map<String, Object> rev : revs) {
							lastRev = processRev(lastRev, rev);
						}

						startAt += revs.size();
					}

					if (lastRev != null) {
						writeToCsv(lastRev);
					}

					writer.flush();
//					EmailUtil.getInstance().sendEmail("", new String[] {user.getEmailAddress()}, new File[] {file}, new HashMap<>());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					IOUtils.closeQuietly(writer);
				}
			}

			@PlusTransactional
			private List<Map<String, Object>> getRevisions(int startAt, int maxRevs) {
				return daoFactory.getAuditDao().getRevisions(sql, entityId, from, to, startAt, maxRevs);
			}

			private Map<String, Object> processRev(Map<String, Object> lastRev, Map<String, Object> currentRev) {
				Long revId = (Long)currentRev.get("revId");

				Long lastRevId = null;
				if (lastRev != null) {
					lastRevId = (Long) lastRev.get("revId");
				}

				if (!revId.equals(lastRevId)) {
					if (lastRev != null) {
						writeToCsv(lastRev);
					}

					lastRev = new HashMap<>();
					lastRev.putAll(currentRev);
					lastRev.remove("revType");
					lastRev.remove("entityName");
					lastRev.put("added", new HashMap<String, Integer>());
					lastRev.put("modified", new HashMap<String, Integer>());
					lastRev.put("deleted", new HashMap<String, Integer>());
				}

				Integer type = (Integer)currentRev.get("revType");
				if (type == null) {
					type = 0;
				}

				String op = null;
				switch (type) {
					case 0:
						op = "added";
						break;
					case 1:
						op = "modified";
						break;
					case 2:
						op = "deleted";
						break;
				}

				Map<String, Integer> countMap = (Map<String, Integer>)lastRev.get(op);
				String entity = (String)currentRev.get("entityName");
				Integer count = countMap.get(entity);
				if (count == null) {
					count = 0;
				}

				countMap.put(entity, ++count);
				return lastRev;
			}

			private File getOutputFile() {
				File tmp = new File(ConfigUtil.getInstance().getDataDir() + File.separator + "tmp");
				tmp.mkdirs();


				File out = new File(tmp, UUID.randomUUID().toString() + ".csv");
				out.deleteOnExit();
				return out;
			}

			private void writeHeader() {
				writer.writeNext(new String[] {
					"Revision Id",
					"Revision Date and Time",
					"User",
					"IP Address",
					"Added",
					"Modified",
					"Deleted"
				});
			}

			private void writeToCsv(Map<String, Object> revInfo) {
				writer.writeNext(new String[] {
					nullSafeString(revInfo.get("revId")),
					nullSafeString(revInfo.get("revTs")),
					nullSafeString(revInfo.get("userFirstName")) + " " + nullSafeString(revInfo.get("userLastName")),
					nullSafeString(revInfo.get("userIpAddr")),
					countString((Map<String, Integer>)revInfo.get("added")),
					countString((Map<String, Integer>)revInfo.get("modified")),
					countString((Map<String, Integer>)revInfo.get("deleted"))
				});
			}

			private String countString(Map<String, Integer> countMap) {
				StringBuilder str = new StringBuilder();
				for (Map.Entry<String, Integer> count : countMap.entrySet()) {
					str.append(count.getValue()).append(" ").append(count.getKey());
				}

				return str.toString();
			}

			private String nullSafeString(Object obj) {
				if (obj == null) {
					return StringUtils.EMPTY;
				}

				return obj.toString();
			}
		});
	}

}
