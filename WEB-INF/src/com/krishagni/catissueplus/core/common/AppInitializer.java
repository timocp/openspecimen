package com.krishagni.catissueplus.core.common;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import edu.common.dynamicextensions.napi.FormDataFilter;
import edu.common.dynamicextensions.napi.FormDataManager;

public class AppInitializer implements InitializingBean {
	private static final Logger logger = Logger.getLogger(AppInitializer.class);
	
	@Autowired
	private FormDataManager formDataMgr;
	
	private Map<String, FormDataFilter> postFormSaveFilters = new HashMap<String, FormDataFilter>();
	
	public FormDataManager getFormDataMgr() {
		return formDataMgr;
	}

	public void setFormDataMgr(FormDataManager formDataMgr) {
		this.formDataMgr = formDataMgr;
	}
	
	public void setPostFormSaveFilters(Map<String, FormDataFilter> postFormSaveFilters) {
		this.postFormSaveFilters = postFormSaveFilters;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		for (Map.Entry<String, FormDataFilter> filterEntry : postFormSaveFilters.entrySet()) {
			if (filterEntry.getKey().equals("all")) {
				formDataMgr.getFilterMgr().addPostFilter(filterEntry.getValue());
			} else {
				formDataMgr.getFilterMgr().addPostFilter(filterEntry.getKey(), filterEntry.getValue());
			}
		}
	}
}
