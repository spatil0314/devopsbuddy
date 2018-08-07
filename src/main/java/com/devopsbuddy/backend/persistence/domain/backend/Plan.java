package com.devopsbuddy.backend.persistence.domain.backend;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.devopsbuddy.enums.PlansEnum;

@Entity
public class Plan implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private int id;
	private String name;

	public Plan() {

	}

	public Plan(final PlansEnum plansEnum) {
		this.id = plansEnum.getId();
		this.name = plansEnum.getPlanName();
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

	@Override
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		final Plan plan = (Plan) o;

		return id == plan.id;

	}

	@Override
	public int hashCode() {
		return id;
	}

}
