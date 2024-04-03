package com.timesheet.services;

import java.time.LocalDate;
import java.util.List;

import com.timesheet.entities.Holiday;

public interface HolidayInterface {

	public boolean saveHoliday(Holiday holiday);
	public boolean removeHoliday(LocalDate ld);
	public boolean updateHoliday(Long id, LocalDate newDate, String description);
	public boolean addHoliday(LocalDate ld, String description);
	public boolean isHoliday(LocalDate ld);
	public List<Holiday>getHolidays();
	public Holiday getHoliday(Long id);
}
