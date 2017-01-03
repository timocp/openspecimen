package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class ContainerStoreList extends BaseEntity {
	public enum Operation {
		PUT,
		PICK
	}

	public enum Status {
		PENDING,
		EXECUTED,
		STORED,
		RETRIEVED
	}

	private StorageContainer container;

	private Operation op;

	private Set<Specimen> specimens = new HashSet<>();

	private Status status;

	public StorageContainer getContainer() {
		return container;
	}

	public void setContainer(StorageContainer container) {
		this.container = container;
	}

	public Operation getOp() {
		return op;
	}

	public void setOp(Operation op) {
		this.op = op;
	}

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
