package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentStatement;
import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTier;

@JsonFilter("withoutId")
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ConsentTierDetail {
	private Long id;
	
	private Long cpId;
	
	private Long statementId;

	private String statementCode;

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

	public Long getStatementId() {
		return statementId;
	}

	public void setStatementId(Long statementId) {
		this.statementId = statementId;
	}

	public String getStatementCode() {
		return statementCode;
	}

	public void setStatementCode(String statementCode) {
		this.statementCode = statementCode;
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
		detail.setStatementId(ct.getStatement().getId());
		detail.setStatementCode(ct.getStatement().getCode());
		detail.setStatement(ct.getStatement().getStatement());
		return detail;
	}
	
	public static List<ConsentTierDetail> from(Collection<ConsentTier> cts) {
		return cts.stream().map(ConsentTierDetail::from).collect(Collectors.toList());
	}
}
