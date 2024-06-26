package com.timesheet.dtos;

import java.util.List;

import com.timesheet.entities.Employee;
import com.timesheet.entities.Vacation;
import com.timesheet.models.VacationReport;

import lombok.Data;

@Data
public class VacationDTO {
	private List<Vacation>vacations;
	private VacationReport vacationReport;
	private Employee employee;
	
	public VacationDTO(List<Vacation>vacations, VacationReport vacationReport, Employee employee) {
		this.employee=employee;
		this.vacations=vacations;
		this.vacationReport=vacationReport;
	}
}
