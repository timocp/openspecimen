
package com.krishagni.catissueplus.core.common.errors;

import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class OpenSpecimenException extends RuntimeException {
	private static final long serialVersionUID = -1473557909717365251L;

	private ResponseEvent response;

	public OpenSpecimenException(ResponseEvent response) {
		this.response = response;
	}

	public ResponseEvent getResponse() {
		return response;
	}

	public void setResponse(ResponseEvent response) {
		this.response = response;
	}

	public String getMessage() {
		if (response.getMessage() != null) {
			return response.getMessage();
		} else if (response.getException() != null) {
			return response.getException().getMessage();
		}
		
		return "";
	}

}
