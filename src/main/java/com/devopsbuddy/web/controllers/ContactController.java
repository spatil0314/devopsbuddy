package com.devopsbuddy.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.web.domain.frontend.FeedbackDTO;

@Controller
public class ContactController {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(ContactController.class);

	/** The key which identifies the feedback payload in the Model. */
	public static final String FEEDBACK_MODEL_KEY = "feedback";

	/** The Contact Us view name. */
	private static final String CONTACT_US_VIEW_NAME = "contact/contact";

	@Autowired
	private EmailService emailService;

	@RequestMapping(value = "/contact", method = RequestMethod.GET)
	public String contactGet(final ModelMap model) {
		final FeedbackDTO dto = new FeedbackDTO();
		model.addAttribute(FEEDBACK_MODEL_KEY, dto);
		return CONTACT_US_VIEW_NAME;
	}

	@RequestMapping(value = "/contact", method = RequestMethod.POST)
	public String contactPost(final @ModelAttribute(FEEDBACK_MODEL_KEY) FeedbackDTO feedback) {
		new FeedbackDTO();
		LOG.debug("Feedback POJO content: {}", feedback);
		emailService.sendFeedbackEmail(feedback);
		return ContactController.CONTACT_US_VIEW_NAME;
	}
}
