package com.krishagni.rbac.service.impl;

import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.services.AbstractObjectImporter;
import com.krishagni.importer.events.ImportObjectDetail;
import com.krishagni.rbac.events.SubjectRolesList;
import com.krishagni.rbac.service.RbacService;

public class SubjectRolesImporter extends AbstractObjectImporter<SubjectRolesList, SubjectRolesList> {
	
	private RbacService rbacSvc;
	
	public void setRbacSvc(RbacService rbacSvc) {
		this.rbacSvc = rbacSvc;
	}

	@Override
	public ResponseEvent<SubjectRolesList> importObject(RequestEvent<ImportObjectDetail<SubjectRolesList>> req) {
		try {
			ImportObjectDetail<SubjectRolesList> detail = req.getPayload();
			RequestEvent<SubjectRolesList> rolesReq = new RequestEvent<SubjectRolesList>(detail.getObject());
			return rbacSvc.assignRoles(rolesReq);
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
