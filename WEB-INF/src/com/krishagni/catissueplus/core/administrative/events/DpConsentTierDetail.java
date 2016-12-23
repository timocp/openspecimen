package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class DpConsentTierDetail {
	private Long dpId;
	
	private String dpTitle;
	
	private String dpShortTitle;

	private Long consentStmtId;

	private String consentStmtCode;

	private String consentStmt;

	private String newConsentStmtCode;

	public Long getDpId() {
		return dpId;
	}

	public void setDpId(Long dpId) {
		this.dpId = dpId;
	}

	public String getDpTitle() {
		return dpTitle;
	}

	public void setDpTitle(String dpTitle) {
		this.dpTitle = dpTitle;
	}

	public String getDpShortTitle() {
		return dpShortTitle;
	}

	public void setDpShortTitle(String dpShortTitle) {
		this.dpShortTitle = dpShortTitle;
	}

	public Long getConsentStmtId() {
		return consentStmtId;
	}

	public void setConsentStmtId(Long consentStmtId) {
		this.consentStmtId = consentStmtId;
	}

	public String getConsentStmtCode() {
		return consentStmtCode;
	}

	public void setConsentStmtCode(String consentStmtCode) {
		this.consentStmtCode = consentStmtCode;
	}
	
	public String getConsentStmt() {
		return consentStmt;
	}

	public void setConsentStmt(String consentStmt) {
		this.consentStmt = consentStmt;
	}

	public String getNewConsentStmtCode() {
		return newConsentStmtCode;
	}

	public void setNewConsentStmtCode(String newConsentStmtCode) {
		this.newConsentStmtCode = newConsentStmtCode;
	}

	public static DpConsentTierDetail from(DistributionProtocol dp, ConsentStatement cs) {
		DpConsentTierDetail detail = new DpConsentTierDetail();
		detail.setConsentStmtId(cs.getId());
		detail.setConsentStmtCode(cs.getCode());
		detail.setConsentStmt(cs.getStatement());
		detail.setDpId(dp.getId());
		detail.setDpTitle(dp.getTitle());
		detail.setDpShortTitle(dp.getShortTitle());
		return detail;
	}

	public static List<DpConsentTierDetail> from(DistributionProtocol dp) {
		return dp.getConsentStmts().stream().map(cs -> from(dp, cs)).collect(Collectors.toList());
	}
}
