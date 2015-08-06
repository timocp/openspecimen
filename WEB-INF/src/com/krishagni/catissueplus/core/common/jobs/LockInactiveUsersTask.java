package com.krishagni.catissueplus.core.common.jobs;

import com.krishagni.catissueplus.core.administrative.domain.ScheduledJobRun;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.services.ScheduledTask;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;


public class LockInactiveUsersTask implements ScheduledTask{

	private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public void doJob(ScheduledJobRun jobRun) throws Exception {
		User user =  daoFactory.getUserDao().getById(1l);
		user.setLastName("lastName");
		daoFactory.getUserDao().saveOrUpdate(user);
	}

}
