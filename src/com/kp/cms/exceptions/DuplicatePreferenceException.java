package com.kp.cms.exceptions;

@SuppressWarnings("serial")
public class DuplicatePreferenceException extends BusinessException {

	public DuplicatePreferenceException() {

	}

	public DuplicatePreferenceException(String msg) {
		super(msg);
	}

}
