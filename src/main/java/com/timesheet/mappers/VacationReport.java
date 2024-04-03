package com.timesheet.mappers;

import lombok.Data;

@Data
public class VacationReport {

	private int totalHours;
	private int vacationHours;
	private int vacationDays;
	private int daysTaken;
	private int daysRemaining;
	
	public VacationReport(int totalHours, int daysTaken) {
		this.totalHours=totalHours;
		this.vacationHours=(5/44)*totalHours;
		this.vacationDays=(vacationHours/8);
		this.daysTaken=daysTaken;
		this.daysRemaining=vacationDays-daysTaken;
	}
}
