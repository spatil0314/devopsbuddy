package com.devopsbuddy.backend.persistence.domain.backend;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.devopsbuddy.enums.RolesEnum;

@Entity
public class Role implements Serializable {

	/** The Serial Version UID for Serializable classes. */
	private static final long serialVersionUID = 1L;

	@Id
	private int id;

	private String name;

	@OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<UserRole> userRoles = new HashSet<>();

	public Role() {

	}

	/**
	 * Full constructor. 
	 * @param rolesEnum
	 */
	public Role(final RolesEnum rolesEnum) {
		this.id = rolesEnum.getId();
		this.name = rolesEnum.getRoleName();
	}

	public int getId() {
		return id;
	}

	public void setId(final int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Set<UserRole> getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(final Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Role role = (Role) o;

		return id == role.id;

	}

	@Override
	public int hashCode() {
		return id;
	}
}