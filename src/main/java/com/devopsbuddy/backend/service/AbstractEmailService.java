package com.devopsbuddy.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.devopsbuddy.web.domain.frontend.FeedbackDTO;

public abstract class AbstractEmailService implements EmailService {

	@Value("${default.to.address}")
	private String defaultToAddress;

	/**
	 * Creates a Simple Mail Message from a Feedback Pojo.
	 * 
	 * @param feedback The Feedback pojo
	 * @return
	 */
	protected SimpleMailMessage prepareSimpleMailMessageFromFeedbackPojo(final FeedbackDTO feedback) {
		final SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(defaultToAddress);
		message.setFrom(feedback.getEmail());
		message.setSubject("[DevOps Buddy]: Feedback received from " + feedback.getFirstName() + " "
				+ feedback.getLastName() + "!");
		message.setText(feedback.getFeedback());
		return message;
	}

	@Override
	public void sendFeedbackEmail(final FeedbackDTO feedback) {
		sendGenericEmailMessage(prepareSimpleMailMessageFromFeedbackPojo(feedback));
	}
}