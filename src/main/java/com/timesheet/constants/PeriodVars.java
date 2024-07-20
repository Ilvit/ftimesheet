package com.timesheet.constants;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import lombok.Data;

@Data
public class PeriodVars {
	private int year;
	private int periodStart;
	private int periodEnd;
	private int periodLength;
	private LocalDate periodStartDate;
	private LocalDate periodEndDate;
	private ArrayList<LocalDate> periodDates;
	
	public PeriodVars(LocalDate date) {
		initPeriodVars(date);
	}
	
	public PeriodVars(String period) {
		LocalDate date=LocalDate.parse(period, TimesheetPeriods.dtf);
		initPeriodVars(date);
	}
	private void initPeriodVars(LocalDate date) {
		if(date.getDayOfMonth()>10 && date.getDayOfMonth()<26) {
			periodStart=11;	
			periodEnd=25;	
			periodLength=15;
			periodStartDate=LocalDate.of(date.getYear(), date.getMonth(), periodStart);
		}else {
			periodStart=26;
			periodEnd=10;
			periodLength=date.getMonth().minus(1).length(true)-15;
			
			if(date.getMonth().equals(Month.JANUARY)) {
				periodStartDate=LocalDate.of(date.getYear()-1, date.getMonth().minus(1), periodStart);
			}else periodStartDate=LocalDate.of(date.getYear(), date.getMonth().minus(1), periodStart);
		}
		periodEndDate=LocalDate.of(date.getYear(), date.getMonth(), periodEnd);
		
//		fill period dates

		LocalDate locDate;
		periodDates=new ArrayList<>();
		
		if(periodStart==11) {
			for(int i=periodStart;i<=periodEnd;i++) {
				locDate=LocalDate.of(date.getYear(), date.getMonth(), i);
				periodDates.add(locDate);
			}
		}else {//periodStart == 26
			for(int i=periodStart;i<=date.getMonth().minus(1).length(true);i++) {
				if(date.getMonth().equals(Month.JANUARY)) {
					locDate=LocalDate.of(date.getYear()-1, date.getMonth().minus(1), i);
				}else locDate=LocalDate.of(date.getYear(), date.getMonth().minus(1), i);
				periodDates.add(locDate);
			}
			for(int i=1;i<=10;i++) {
				locDate=LocalDate.of(date.getYear(), date.getMonth(), i);
				periodDates.add(locDate);
			}
		}
	}

}
