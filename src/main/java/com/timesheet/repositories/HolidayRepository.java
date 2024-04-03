package com.timesheet.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.Holiday;

public interface HolidayRepository extends JpaRepository<Holiday, Long> {

	public Holiday findByDate(LocalDate ld);
	public void deleteByDate(LocalDate ld);
}
