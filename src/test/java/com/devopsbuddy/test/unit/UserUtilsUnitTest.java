package com.devopsbuddy.test.unit;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.utils.UserUtils;
import com.devopsbuddy.web.controllers.ForgotMyPasswordController;
import com.devopsbuddy.web.domain.frontend.BasicAccountPayload;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;


public class UserUtilsUnitTest {

	private MockHttpServletRequest mockHttpServletRequest;
	private PodamFactory podamFactory;

	@Before
	public void init() {
		mockHttpServletRequest = new MockHttpServletRequest();
		podamFactory = new PodamFactoryImpl();
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

	@Test
	public void mapWebUserToDomainUser() {

		final BasicAccountPayload webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayload.class);
		webUser.setEmail("me@example.com");

		final User user = UserUtils.fromWebUserToDomainUser(webUser);
		Assert.assertNotNull(user);

		Assert.assertEquals(webUser.getUsername(), user.getUsername());
		Assert.assertEquals(webUser.getPassword(), user.getPassword());
		Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
		Assert.assertEquals(webUser.getLastName(), user.getLastName());
		Assert.assertEquals(webUser.getEmail(), user.getEmail());
		Assert.assertEquals(webUser.getPhoneNumber(), user.getPhoneNumber());
		Assert.assertEquals(webUser.getCountry(), user.getCountry());
		Assert.assertEquals(webUser.getDescription(), user.getDescription());

	}
}