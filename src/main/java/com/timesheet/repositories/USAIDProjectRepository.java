package com.timesheet.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.USAIDProject;

public interface USAIDProjectRepository extends JpaRepository<USAIDProject, Long> {

	public USAIDProject findByName(String projectName);
}
