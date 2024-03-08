package com.timesheet.constants;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PeriodVars {
	private int start;
	private int end;
	private int year;
	private int month;
	private int periodLength;
	
	public PeriodVars(LocalDate date) {
		year=date.getYear();
		end=date.getDayOfMonth();
		month=date.getMonthValue();
		if(end>15) {
			start=16;
		}else start=1;
		periodLength=0;
		for(int i=start;i<=end;i++)periodLength++;
	}
	public PeriodVars(String period) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			TimesheetPeriods tp=new TimesheetPeriods();
			period=tp.getCurrentPeriod();
		}
		LocalDate date=LocalDate.parse(period,TimesheetPeriods.dtf);
		year=date.getYear();
		end=date.getDayOfMonth();
		month=date.getMonthValue();
		if(end>15) {
			start=16;
		}else start=1;
		periodLength=0;
		for(int i=start;i<=end;i++)periodLength++;
	}

}
