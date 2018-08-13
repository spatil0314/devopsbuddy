package com.devopsbuddy.test.integration;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.persistence.domain.backend.PasswordResetToken;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.repositories.PasswordResetTokenRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PasswordResetTokenIntegrationTest extends AbstractIntegrationTest {

	@Value("${token.expiration.length.minutes}")
	private int expirationTimeInMinutes;

	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@Rule public TestName testName = new TestName();

	@Before
	public void init() {
		Assert.assertFalse(expirationTimeInMinutes == 0);
	}

	@Test
	public void testTokenExpirationLength() throws Exception {

		final User user = createUser(testName);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());


		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
		final String token = UUID.randomUUID().toString();

		final LocalDateTime expectedTime = now.plusMinutes(expirationTimeInMinutes);

		final PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);

		final LocalDateTime actualTime = passwordResetToken.getExpiryDate();
		Assert.assertNotNull(actualTime);
		Assert.assertEquals(expectedTime, actualTime);
	}

	@Test
	public void testFindTokenByTokenValue() throws Exception {

		final User user = createUser(testName);
		final String token = UUID.randomUUID().toString();
		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		createPasswordResetToken(token, user, now);

		final PasswordResetToken retrievedPasswordResetToken = passwordResetTokenRepository.findByToken(token);
		Assert.assertNotNull(retrievedPasswordResetToken);
		Assert.assertNotNull(retrievedPasswordResetToken.getId());
		Assert.assertNotNull(retrievedPasswordResetToken.getUser());

	}

	@Test
	public void testDeleteToken() throws Exception {

		final User user = createUser(testName);
		final String token = UUID.randomUUID().toString();
		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		final PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		final long tokenId = passwordResetToken.getId();
		passwordResetTokenRepository.deleteById(tokenId);

		final Optional<PasswordResetToken> shouldNotExistToken = passwordResetTokenRepository.findById(tokenId);
		Assert.assertFalse(shouldNotExistToken.isPresent());

	}

	@Test
	public void testCascadeDeleteFromUserEntity() throws Exception {

		final User user = createUser(testName);
		final String token = UUID.randomUUID().toString();
		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		final PasswordResetToken passwordResetToken = createPasswordResetToken(token, user, now);
		passwordResetToken.getId();

		userRepository.deleteById(user.getId());

		final Set<PasswordResetToken> shouldBeEmpty = passwordResetTokenRepository.findAllByUserId(user.getId());
		Assert.assertTrue(shouldBeEmpty.isEmpty());


	}

	@Test
	public void testMultipleTokensAreReturnedWhenQueringByUserId() throws Exception {

		final User user = createUser(testName);
		final LocalDateTime now = LocalDateTime.now(Clock.systemUTC());

		final String token1 = UUID.randomUUID().toString();
		final String token2 = UUID.randomUUID().toString();
		final String token3 = UUID.randomUUID().toString();

		final Set<PasswordResetToken> tokens = new HashSet<>();
		tokens.add(createPasswordResetToken(token1, user, now));
		tokens.add(createPasswordResetToken(token2, user, now));
		tokens.add(createPasswordResetToken(token3, user, now));

		passwordResetTokenRepository.saveAll(tokens);

		final Optional<User> founduser = userRepository.findById(user.getId());

		final Set<PasswordResetToken> actualTokens = passwordResetTokenRepository.findAllByUserId(founduser.get().getId());
		Assert.assertTrue(actualTokens.size() == tokens.size());
		tokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());
		actualTokens.stream().map(prt -> prt.getToken()).collect(Collectors.toList());

	}


	//------------------> Private methods

	private PasswordResetToken createPasswordResetToken(final String token, final User user, final LocalDateTime now) {


		final PasswordResetToken passwordResetToken = new PasswordResetToken(token, user, now, expirationTimeInMinutes);
		passwordResetTokenRepository.save(passwordResetToken);
		Assert.assertNotNull(passwordResetToken.getId());
		return passwordResetToken;

	}




}