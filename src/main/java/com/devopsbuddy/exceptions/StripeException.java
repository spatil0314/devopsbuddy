package com.devopsbuddy.exceptions;

public class StripeException extends RuntimeException {

	public StripeException(final Throwable e) {
		super(e);
	}
}
