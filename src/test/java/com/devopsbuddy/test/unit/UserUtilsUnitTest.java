package com.devopsbuddy.test.unit;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;


public class UserUtilsUnitTest {

	private MockHttpServletRequest mockHttpServletRequest;

	@Before
	public void init() {
		mockHttpServletRequest = new MockHttpServletRequest();
	}

	@Test
	public void testPasswordResetEmailUrlConstruction() throws Exception {

		mockHttpServletRequest.setServerPort(8080); //Default is 80

		final String token = UUID.randomUUID().toString();
		final long userId = 123456;

		final String expectedUrl = "http://localhost:8080" +
				ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id=" + userId + "&token=" + token;

		final String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);

		Assert.assertEquals(expectedUrl, actualUrl);

	}
}