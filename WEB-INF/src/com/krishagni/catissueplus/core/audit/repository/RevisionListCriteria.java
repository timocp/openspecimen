package com.krishagni.catissueplus.core.audit.repository;

import java.util.Date;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class RevisionListCriteria extends AbstractListCriteria<RevisionListCriteria> {
	private Date from;

	private Date to;

	private String entitySql;

	private Long entityId;

	private boolean latestFirst;

	@Override
	public RevisionListCriteria self() {
		return this;
	}

	public Date from() {
		return from;
	}

	public RevisionListCriteria from(Date from) {
		this.from = from;
		return self();
	}

	public Date to() {
		return to;
	}
	public RevisionListCriteria to(Date to) {
		this.to = to;
		return self();
	}

	public String entitySql() {
		return entitySql;
	}

	public RevisionListCriteria entitySql(String entitySql) {
		this.entitySql = entitySql;
		return self();
	}

	public Long entityId() {
		return entityId;
	}

	public RevisionListCriteria entityId(Long entityId) {
		this.entityId = entityId;
		return self();
	}

	public boolean latestFirst() {
		return latestFirst;
	}

	public RevisionListCriteria latestFirst(boolean latestFirst) {
		this.latestFirst = latestFirst;
		return self();
	}
}
