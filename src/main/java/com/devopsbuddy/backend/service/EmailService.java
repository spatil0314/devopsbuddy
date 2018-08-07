package com.devopsbuddy.backend.service;

import org.springframework.mail.SimpleMailMessage;

import com.devopsbuddy.web.domain.frontend.FeedbackDTO;

public interface EmailService {

	/**
	 * Sends an email with the content in the Feedback Pojo.
	 * 
	 * @param feedbackPojo The feedback Pojo
	 */
	void sendFeedbackEmail(FeedbackDTO feedbackDTO);

	/**
	 * Sends an email with the content of the Simple Mail Message object.
	 * 
	 * @param message The object containing the email content
	 */
	void sendGenericEmailMessage(SimpleMailMessage message);
}