package com.devopsbuddy.utils;

import javax.servlet.http.HttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;

public class UserUtils {

	/**
	 * Non instantiable.
	 */
	private UserUtils() {
		throw new AssertionError("Non instantiable");
	}

	/**
	 * Creates a user with basic attributes set.
	 * @param username 
	 * @param email 
	 * @return A User entity
	 */
	public static User createBasicUser(final String username, final String email) {

		final User user = new User();
		user.setUsername(username);
		user.setPassword("secret");
		user.setEmail(email);
		user.setFirstName("firstName");
		user.setLastName("lastName");
		user.setPhoneNumber("123456789123");
		user.setCountry("GB");
		user.setEnabled(true);
		user.setDescription("A basic user");
		user.setProfileImageUrl("https://blabla.images.com/basicuser");

		return user;
	}

	/**
	 * Builds and returns the URL to reset the user password.
	 * @param request The Http Servlet Request
	 * @param userId The user id
	 * @param token The token
	 * @return the URL to reset the user password.
	 */
	public static String createPasswordResetUrl(final HttpServletRequest request, final long userId, final String token) {
		final String passwordResetUrl =
				request.getScheme() +
				"://" +
				request.getServerName() +
				":" +
				request.getServerPort() +
				request.getContextPath() +
				ForgotMyPasswordController.CHANGE_PASSWORD_PATH +
				"?id=" +
						userId +
						"&token=" +
						token;

		return passwordResetUrl;
	}
}