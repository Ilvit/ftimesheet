package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, String> {
	public Employee findByMail(String mail);
	public Employee findByName(String name);
	
}
