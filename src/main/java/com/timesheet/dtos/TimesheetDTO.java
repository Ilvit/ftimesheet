package com.timesheet.dtos;

import java.util.List;

import com.timesheet.entities.Employee;
import com.timesheet.mappers.Timesheet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data @AllArgsConstructor @Builder
public class TimesheetDTO {
	private Employee employee;
	private Timesheet timesheet;
	private List<String>timesheetsPeriods;
	private String timesheetPeriod;
	private boolean businessDaysPresent;
	private boolean holidaysPresent;
	private boolean weekendDaysPresent;
	
	public TimesheetDTO() {}
	
	public TimesheetDTO(Timesheet timesheet){
		this.timesheet=timesheet;
	}

}
