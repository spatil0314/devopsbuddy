package com.devopsbuddy.test.integration;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.service.PasswordResetTokenService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordResetTokenServiceIntegrationTest extends AbstractServiceIntegrationTest {

	@Autowired
	private PasswordResetTokenService passwordResetTokenService;

	@Rule public TestName testName = new TestName();

	@Test
	public void testCreateNewTokenForUserEmail() throws Exception {

		final User user = createUser(testName);

		final PasswordResetToken passwordResetToken =
				passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		Assert.assertNotNull(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getToken());

	}

	@Test
	public void testFindByToken() throws Exception {
		final User user = createUser(testName);

		final PasswordResetToken passwordResetToken =
				passwordResetTokenService.createPasswordResetTokenForEmail(user.getEmail());
		Assert.assertNotNull(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getToken());

		final PasswordResetToken token = passwordResetTokenService.findByToken(passwordResetToken.getToken());
		Assert.assertNotNull(token);

	}

}