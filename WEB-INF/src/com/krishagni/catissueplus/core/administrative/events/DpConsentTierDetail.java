package com.krishagni.catissueplus.core.administrative.events;

import java.util.List;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;

public class DpConsentTierDetail {
	private Long dpId;
	
	private Long consentStmtId;
	
	private String dpTitle;
	
	private String dpShortTitle;
	
	private String consentStmtCode;
	
	private String consentStmt;

	public Long getDpId() {
		return dpId;
	}

	public void setDpId(Long dpId) {
		this.dpId = dpId;
	}

	public Long getConsentStmtId() {
		return consentStmtId;
	}

	public void setConsentStmtId(Long consentStmtId) {
		this.consentStmtId = consentStmtId;
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

	public static List<DpConsentTierDetail> from(DistributionProtocol dp) {
		List<DpConsentTierDetail> details = dp.getConsentStmts()
			.stream()
			.map(cs -> getDpConsentTierDetail(dp, cs))
			.collect(Collectors.toList());
		
		return details;
	}

	private static DpConsentTierDetail getDpConsentTierDetail(DistributionProtocol dp, ConsentStatement cs) {
		DpConsentTierDetail detail = new DpConsentTierDetail();
		detail.setConsentStmtId(cs.getId());
		detail.setConsentStmtCode(cs.getCode());
		detail.setConsentStmt(cs.getStatement());
		detail.setDpId(dp.getId());
		detail.setDpTitle(dp.getTitle());
		detail.setDpShortTitle(dp.getShortTitle());
		
		return detail;
	}

}
