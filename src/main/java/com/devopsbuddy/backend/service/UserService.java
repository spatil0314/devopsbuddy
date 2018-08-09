package com.devopsbuddy.backend.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devopsbuddy.backend.persistence.domain.backend.Plan;
import com.devopsbuddy.backend.persistence.domain.backend.Role;
import com.devopsbuddy.backend.persistence.domain.backend.User;
import com.devopsbuddy.backend.persistence.domain.backend.UserRole;
import com.devopsbuddy.backend.persistence.repositories.PlanRepository;
import com.devopsbuddy.backend.persistence.repositories.RoleRepository;
import com.devopsbuddy.backend.persistence.repositories.UserRepository;
import com.devopsbuddy.enums.PlansEnum;

@Service
@Transactional(readOnly = true)
public class UserService {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PlanRepository planRepository;

	@Autowired
	private UserRepository userRepository;

	@Transactional
	public User createUser(User user, final PlansEnum plansEnum, final Set<UserRole> userRoles) {

		Plan plan = new Plan(plansEnum);
		// It makes sure the plans exist in the database
		if (!planRepository.existsById(plansEnum.getId())) {
			plan = planRepository.save(plan);
		}

		user.setPlan(plan);

		for (final UserRole ur : userRoles) {
			final Role role = roleRepository.save(ur.getRole());
			ur.setRole(role);
		}

		user.getUserRoles().addAll(userRoles);

		user = userRepository.save(user);

		return user;

	}
}