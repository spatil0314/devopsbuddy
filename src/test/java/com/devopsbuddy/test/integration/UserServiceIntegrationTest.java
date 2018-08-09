package com.devopsbuddy.test.integration;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.service.UserService;
import com.devopsbuddy.enums.PlansEnum;
import com.devopsbuddy.enums.RolesEnum;
import com.devopsbuddy.utils.UserUtils;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceIntegrationTest {

	@Autowired
	private UserService userService;

	@Test
	public void testCreateNewUser() throws Exception {

		final Set<UserRole> userRoles = new HashSet<>();
		final User basicUser = UserUtils.createBasicUser();
		userRoles.add(new UserRole(basicUser, new Role(RolesEnum.BASIC)));

		final User user = userService.createUser(basicUser, PlansEnum.BASIC, userRoles);
		Assert.assertNotNull(user);
		Assert.assertNotNull(user.getId());

	}
}