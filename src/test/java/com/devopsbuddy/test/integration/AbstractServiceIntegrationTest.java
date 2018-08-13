package com.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.rules.TestName;
import org.springframework.beans.factory.annotation.Autowired;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;


public abstract class AbstractServiceIntegrationTest {
	@Autowired
	protected UserService userService;

	protected User createUser(final TestName testName) {   	

		final String username = testName.getMethodName();
		final String email = testName.getMethodName() + "@devopsbuddy.com";

		final Set<UserRole> userRoles = new HashSet<>();
		final User basicUser = UserUtils.createBasicUser(username, email);
		userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

		return userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
	}
}