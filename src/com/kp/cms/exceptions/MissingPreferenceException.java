package com.kp.cms.exceptions;

@SuppressWarnings("serial")
public class MissingPreferenceException extends BusinessException {

	public MissingPreferenceException() {

	}

	public MissingPreferenceException(String msg) {
		super(msg);
	}
}
