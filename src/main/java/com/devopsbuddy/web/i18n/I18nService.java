package com.devopsbuddy.web.i18n;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

@Service
public class I18nService {

	private static final Logger log = LoggerFactory.getLogger(I18nService.class);

	@Autowired
	private MessageSource messageSource;

	public String getMessage(final String messageId) {
		log.info("Returning the i18n text for messageId {}", messageId);
		final Locale locale = LocaleContextHolder.getLocale();
		return getMessage(messageId, locale);
	}

	public String getMessage(final String messageId, final Locale locale) {
		return messageSource.getMessage(messageId, null, locale);
	}
}
