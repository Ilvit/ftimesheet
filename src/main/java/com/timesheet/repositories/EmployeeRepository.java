package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.timesheet.entities.Employee;
import com.timesheet.enums.Positions;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	
	public Employee findByEmployeeID(String employeeID);
	public Employee findByMail(String mail);
	public Employee findByName(String name);
	public Employee findByPosition(Positions position);
	public void deleteByEmployeeID(String employeeID);
	
}
