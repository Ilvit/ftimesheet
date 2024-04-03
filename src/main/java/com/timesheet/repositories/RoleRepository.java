package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	public Role findByName(String roleName);
}
