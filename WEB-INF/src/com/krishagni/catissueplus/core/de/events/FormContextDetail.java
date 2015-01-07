package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import krishagni.catissueplus.beans.FormContextBean;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolSummary;

public class FormContextDetail {
	private Long formCtxtId;
	
	private CollectionProtocolSummary collectionProtocol;
	
	private String level;
	
	private Long formId;
	
	private Integer sortOrder;

	private boolean multiRecord;
	
	private boolean sysForm;

	public Long getFormCtxtId() {
		return formCtxtId;
	}

	public void setFormCtxtId(Long formCtxtId) {
		this.formCtxtId = formCtxtId;
	}

	public CollectionProtocolSummary getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocolSummary collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}
	
	public Integer getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Integer sortOrder) {
		this.sortOrder = sortOrder;
	}

	public boolean isMultiRecord() {
		return multiRecord;
	}

	public void setMultiRecord(boolean multiRecord) {
		this.multiRecord = multiRecord;
	}

	public boolean isSysForm() {
		return sysForm;
	}

	public void setSysForm(boolean sysForm) {
		this.sysForm = sysForm;
	}
	
	public static FormContextDetail from(FormContextBean formCtx) {
		FormContextDetail result = new FormContextDetail();
		
		CollectionProtocolSummary cp = new CollectionProtocolSummary();
		cp.setId(formCtx.getCpId());		
		result.setCollectionProtocol(cp);
		result.setFormCtxtId(formCtx.getIdentifier());
		result.setFormId(formCtx.getContainerId());
		result.setLevel(formCtx.getEntityType());
		result.setMultiRecord(formCtx.isMultiRecord());
		result.setSortOrder(formCtx.getSortOrder());
		result.setSysForm(formCtx.isSysForm());
		return result;
	}
	
	public static List<FormContextDetail> from(Collection<FormContextBean> formCtxts) {
		List<FormContextDetail> result = new ArrayList<FormContextDetail>();
		for (FormContextBean formCtx : formCtxts) {
			result.add(FormContextDetail.from(formCtx));
		}
		
		return result;
	}
}
