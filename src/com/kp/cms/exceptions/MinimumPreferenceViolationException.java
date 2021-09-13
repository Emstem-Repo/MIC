package com.kp.cms.exceptions;

@SuppressWarnings("serial")
public class MinimumPreferenceViolationException extends BusinessException {

	public MinimumPreferenceViolationException() {

	}

	public MinimumPreferenceViolationException(String msg) {
		super(msg);
	}

}
