package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole, Long> {

	public AppRole findByRoleName(String roleName);
}
