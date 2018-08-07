package com.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UsersUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RepositoriesIntegrationTest {

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;




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

		final Plan basicPlan = createPlan(PlansEnum.BASIC);
		planRepository.save(basicPlan);

		User basicUser = UsersUtils.createBasicUser();
		basicUser.setPlan(basicPlan);

		final Role basicRole = createRole(RolesEnum.BASIC);
		final Set<UserRole> userRoles = new HashSet<>();
		final UserRole userRole = new UserRole(basicUser, basicRole);
		userRoles.add(userRole);

		basicUser.getUserRoles().addAll(userRoles);

		for (final UserRole ur : userRoles) {
			roleRepository.save(ur.getRole());
		}

		basicUser = userRepository.save(basicUser);
		final Optional<User> newlyCreatedUser = userRepository.findById(basicUser.getId());
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

	//-----------------> Private methods

	private Plan createPlan(final PlansEnum plansEnum) {
		return new Plan(plansEnum);
	}

	private Role createRole(final RolesEnum rolesEnum) {
		return new Role(rolesEnum);
	}



}