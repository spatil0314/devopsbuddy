package com.devopsbuddy.test.integration;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserRepositoryIntegrationTest extends AbstractIntegrationTest {

	@Rule public TestName testName = new TestName();


	@Before
	public void init() {
		Assert.assertNotNull(planRepository);
		Assert.assertNotNull(roleRepository);
		Assert.assertNotNull(userRepository);
	}

	@Test
	public void testCreateNewPlan() throws Exception {
		final Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);
		final Optional<Plan> retrievedPlan = planRepository.findById(PlansEnum.BASIC.getId());
		Assert.assertTrue(retrievedPlan.isPresent());
	}

	@Test
	public void testCreateNewRole() throws Exception {

		final Role userRole  = createRole(RolesEnum.BASIC);
		roleRepository.save(userRole);

		final Optional<Role> retrievedRole = roleRepository.findById(RolesEnum.BASIC.getId());
		Assert.assertTrue(retrievedRole.isPresent());
	}

	@Test
	public void createNewUser() throws Exception {

		final String username = testName.getMethodName();
		final String email = testName.getMethodName() + "@devopsbuddy.com";

		User basicUser = createUser(username, email);

		final Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
		basicUser = userRepository.save(basicUser);
		Assert.assertTrue(newlyCreatedUser.isPresent());
		Assert.assertTrue(newlyCreatedUser.get().getId() != 0);
		Assert.assertNotNull(newlyCreatedUser.get().getPlan());
		Assert.assertNotNull(newlyCreatedUser.get().getPlan().getId());
		final Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.get().getUserRoles();
		for (final UserRole ur : newlyCreatedUserUserRoles) {
			Assert.assertNotNull(ur.getRole());
			Assert.assertNotNull(ur.getRole().getId());
		}

	}

	@Test
	public void testDeleteUser() throws Exception {

		final String username = testName.getMethodName();
		final String email = testName.getMethodName() + "@devopsbuddy.com";

		final User basicUser = createUser(username, email);
		userRepository.deleteById(basicUser.getId());
	}

	@Test
	public void testGetUserByEmail() throws Exception {
		final User user = createUser(testName);

		final User newlyFoundUser = userRepository.findByEmail(user.getEmail());
		Assert.assertNotNull(newlyFoundUser);
		Assert.assertNotNull(newlyFoundUser.getId());
	}

	@Test
	public void testUpdateUserPassword() throws Exception {
		final User user = createUser(testName);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());

		final String newPassword = UUID.randomUUID().toString();

		userRepository.updateUserPassword(user.getId(), newPassword);

		final Optional<User> user2 = userRepository.findById(user.getId());
		Assert.assertEquals(newPassword, user2.get().getPassword());

	}

}