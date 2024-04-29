package com.timesheet.models;

import lombok.Data;

@Data
public class VacationReport {

	private int totalHours;
	private float vacationHours;
	private float vacationDays;
	private int daysTaken;
	private float daysRemaining;
	
	public VacationReport(int totalHours, int daysTaken) {
		this.totalHours=totalHours;
		this.vacationHours=(5*totalHours/44);
		this.vacationDays=(vacationHours/8);
		this.daysTaken=daysTaken;
		this.daysRemaining=vacationDays-daysTaken;
	}
}
