package com.krishagni.catissueplus.core.common;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.common.dynamicextensions.napi.FormDataManager;

public class AppInitializer implements InitializingBean {
	private static final Logger logger = Logger.getLogger(AppInitializer.class);
	
	@Autowired
	private FormDataManager formDataMgr;
	
	public FormDataManager getFormDataMgr() {
		return formDataMgr;
	}

	public void setFormDataMgr(FormDataManager formDataMgr) {
		this.formDataMgr = formDataMgr;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Initializing");
	}
}