package com.timesheet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.Vacation;

public interface VacationRepository extends JpaRepository<Vacation, Long> {
	public List<Vacation>findByEmployeeID(String employeeID);
	public void deleteByIdAndEmployeeID(Long id, String employeeID);
} 
