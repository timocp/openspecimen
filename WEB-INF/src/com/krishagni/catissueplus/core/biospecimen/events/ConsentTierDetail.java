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
	private Long id;
	
	private Long cpId;
	
	private Long consentId;

	private String consentStmtCode;

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

	public Long getConsentId() {
		return consentId;
	}

	public void setConsentId(Long consentId) {
		this.consentId = consentId;
	}

	public String getConsentStmtCode() {
		return consentStmtCode;
	}

	public void setConsentStmtCode(String consentStmtCode) {
		this.consentStmtCode = consentStmtCode;
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
		detail.setConsentId(ct.getConsentStmt().getId());
		detail.setConsentStmtCode(ct.getConsentStmt().getCode());
		detail.setStatement(ct.getConsentStmt().getStatement());
		return detail;
	}
	
	public static List<ConsentTierDetail> from(Collection<ConsentTier> cts) {
		List<ConsentTierDetail> tiers = new ArrayList<ConsentTierDetail>();
		for (ConsentTier ct : cts) {
			tiers.add(ConsentTierDetail.from(ct));
		}
		
		return tiers;
	}

	public ConsentTier toConsentTier(ConsentStatement stmt) {
		ConsentTier ct = new ConsentTier();
		ct.setId(id);
		ct.setConsentStmt(stmt);
		return ct;
	}
}
