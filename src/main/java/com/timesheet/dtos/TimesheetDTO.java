package com.timesheet.dtos;

import java.time.LocalDate;
import java.util.List;

import com.timesheet.entities.Employee;
import com.timesheet.mappers.PeriodState;
import com.timesheet.mappers.Timesheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class TimesheetDTO {
	private Employee employee;
	private Timesheet timesheet;
	private List<PeriodState>periodStates;
	private List<String>timesheetsPeriods;
	private String timesheetPeriod;
	private List<LocalDate>periodDates;
	private boolean regularDaysPresent;
	private boolean holidaysPresent;
	private boolean vacationDaysPresent;
	private List<String>rdProjects;
	private List<String>hdProjects;
	private List<String>vdProjects;
	private List<String>allProjects;
	
	public TimesheetDTO(Timesheet timesheet){
		this.timesheet=timesheet;
	}

}
