package com.devopsbuddy.web.controllers;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.EmailService;
import com.devopsbuddy.backend.service.PasswordResetTokenService;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.i18n.I18nService;

@Controller
public class ForgotMyPasswordController {

	/** The application logger */
	private static final Logger LOG = LoggerFactory.getLogger(ForgotMyPasswordController.class);

	public static final String EMAIL_ADDRESS_VIEW_NAME = "forgotmypassword/emailForm";

	public static final String FORGOT_PASSWORD_URL_MAPPING = "/forgotmypassword";

	public static final String MAIL_SENT_KEY = "mailSent";

	public static final String CHANGE_PASSWORD_PATH = "/changeuserpassword";

	public static final String EMAIL_MESSAGE_TEXT_PROPERTY_NAME = "forgotmypassword.email.text";

	public static final String CHANGE_PASSWORD_VIEW_NAME = "forgotmypassword/changePassword";

	private static final String PASSWORD_RESET_ATTRIBUTE_NAME = "passwordReset";

	private static final String MESSAGE_ATTRIBUTE_NAME = "message";

	@Autowired
	private I18nService i18NService;

	@Autowired
	private EmailService emailService;

	@Value("${webmaster.email}")
	private String webMasterEmail;

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.GET)
	public String forgotPasswordGet() {
		return EMAIL_ADDRESS_VIEW_NAME;
	}

	@RequestMapping(value = FORGOT_PASSWORD_URL_MAPPING, method = RequestMethod.POST)
	public String forgotPasswordPost(final HttpServletRequest request, @RequestParam("email") final String email,
			final ModelMap model) {

		final PasswordResetToken passwordResetToken = passwordResetTokenService.createPasswordResetTokenForEmail(email);

		if (null == passwordResetToken) {
			LOG.warn("Couldn't find a password reset token for email {}", email);
		} else {

			final User user = passwordResetToken.getUser();
			final String token = passwordResetToken.getToken();

			final String resetPasswordUrl = UserUtils.createPasswordResetUrl(request, user.getId(), token);
			LOG.debug("Reset Password URL {}", resetPasswordUrl);

			final String emailText = i18NService.getMessage(EMAIL_MESSAGE_TEXT_PROPERTY_NAME, request.getLocale());

			final SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(user.getEmail());
			mailMessage.setSubject("[Devopsbuddy]: How to Reset Your Password");
			mailMessage.setText(emailText + "\r\n" + resetPasswordUrl);
			mailMessage.setFrom(webMasterEmail);

			emailService.sendGenericEmailMessage(mailMessage);
		}

		model.addAttribute(MAIL_SENT_KEY, "true");

		return EMAIL_ADDRESS_VIEW_NAME;
	}

	@RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.GET)
	public String changeUserPasswordGet(@RequestParam("id") final long id, @RequestParam("token") final String token,
			final Locale locale, final ModelMap model) {
		if (StringUtils.isEmpty(token) || id == 0) {
			LOG.error("Invalid user id {}  or token value {}", id, token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Invalid user id or token value");
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		final PasswordResetToken passwordResetToken = passwordResetTokenService.findByToken(token);

		if (null == passwordResetToken) {
			LOG.warn("A token couldn't be found with value {}", token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "Token not found");
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		final User user = passwordResetToken.getUser();
		if (user.getId() != id) {
			LOG.error("The user id {} passed as parameter does not match the user id {} associated with the token {}",
					id, user.getId(), token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.invalid", locale));
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		if (LocalDateTime.now(Clock.systemUTC()).isAfter(passwordResetToken.getExpiryDate())) {
			LOG.error("The token {} has expired", token);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, i18NService.getMessage("resetPassword.token.expired", locale));
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		model.addAttribute("principalId", user.getId());

		// OK to proceed. We auto-authenticate the user so that in the POST request we
		// can check if the user
		// is authenticated
		final Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		return CHANGE_PASSWORD_VIEW_NAME;
	}

	@RequestMapping(value = CHANGE_PASSWORD_PATH, method = RequestMethod.POST)
	public String changeUserPasswordPost(@RequestParam("principal_id") final long userId,
			@RequestParam("password") final String password, final ModelMap model) {

		final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (null == authentication) {
			LOG.error("An unauthenticated user tried to invoke the reset password POST method");
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request.");
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		final User user = (User) authentication.getPrincipal();
		if (user.getId() != userId) {
			LOG.error("Security breach! User {} is trying to make a password reset request on behalf of {}",
					user.getId(), userId);
			model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "false");
			model.addAttribute(MESSAGE_ATTRIBUTE_NAME, "You are not authorized to perform this request.");
			return CHANGE_PASSWORD_VIEW_NAME;
		}

		userService.updateUserPassword(userId, password);
		LOG.info("Password successfully updated for user {}", user.getUsername());

		model.addAttribute(PASSWORD_RESET_ATTRIBUTE_NAME, "true");

		return CHANGE_PASSWORD_VIEW_NAME;

	}

}