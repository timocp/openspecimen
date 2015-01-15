
package com.krishagni.catissueplus.core.common.errors;

public class ErroneousField {

	public ErroneousField() {

	}

	public ErroneousField(CatissueErrorCode error, String field) {
		this.error = error;
		this.fieldName = field;
	}

	private CatissueErrorCode error;

	private String fieldName;

	public String getErrorMessage() {
		return error.message();
	}

	public Integer getErrorCode() {
		return error == null ? null : error.code();
	}

	public String getFieldName() {
		return fieldName;
	}

}
