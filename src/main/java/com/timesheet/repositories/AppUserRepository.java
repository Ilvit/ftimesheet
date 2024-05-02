package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

	public AppUser findByUsername(String username);
	public AppUser findByEmployeeID(String employeeID);
	public void deleteByUsername(String username);
}
