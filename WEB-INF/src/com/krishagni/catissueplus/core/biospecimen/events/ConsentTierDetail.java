package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;

@JsonFilter("withoutId")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ConsentTierDetail {
	private Long cpId;
	
	private Long id;

	private String cpTitle;
	
	private String cpShortTitle;

	private String consentStmtCode;

	private String newConsentStmtCode;
	
	private String statement;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getCpTitle() {
		return cpTitle;
	}

	public void setCpTitle(String cpTitle) {
		this.cpTitle = cpTitle;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getConsentStmtCode() {
		return consentStmtCode;
	}

	public void setConsentStmtCode(String consentStmtCode) {
		this.consentStmtCode = consentStmtCode;
	}

	public String getNewConsentStmtCode() {
		return newConsentStmtCode;
	}

	public void setNewConsentStmtCode(String newConsentStmtCode) {
		this.newConsentStmtCode = newConsentStmtCode;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}
	
	public static ConsentTierDetail from(ConsentTier ct) {
		if (ct == null) {
			return null;
		}
		
		ConsentTierDetail detail = new ConsentTierDetail();
		detail.setId(ct.getId());
		detail.setStatement(ct.getStatement());
		return detail;
	}
	
	public static List<ConsentTierDetail> from(Collection<ConsentTier> cts) {
		List<ConsentTierDetail> tiers = new ArrayList<ConsentTierDetail>();
		for (ConsentTier ct : cts) {
			tiers.add(ConsentTierDetail.from(ct));
		}
		
		return tiers;
	}
	
	public static ConsentTierDetail from(CollectionProtocol cp, ConsentStatement cs) {
		ConsentTierDetail detail = new ConsentTierDetail();
		detail.setId(cs.getId());
		detail.setConsentStmtCode(cs.getCode());
		detail.setStatement(cs.getStatement());
		detail.setCpId(cp.getId());
		detail.setCpTitle(cp.getTitle());
		detail.setCpShortTitle(cp.getShortTitle());
		return detail;
	}
	
	public static List<ConsentTierDetail> from(CollectionProtocol cp) {
		return cp.getConsentStmts().stream().map(cs -> from(cp, cs)).collect(Collectors.toList());
	}
	
	public ConsentTier toConsentTier() {
		ConsentTier ct = new ConsentTier();
		ct.setId(id);
		ct.setStatement(statement);
		return ct;
	}
}
