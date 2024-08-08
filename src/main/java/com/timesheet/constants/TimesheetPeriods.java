package com.timesheet.constants;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TimesheetPeriods {

	private int year=LocalDate.now().getYear();
	private List<String>periods=new ArrayList<>();
	private String currentPeriod;
	public static DateTimeFormatter dtf=DateTimeFormatter.ofPattern("EEEE_dd-MMMM-yyyy");
	
	public TimesheetPeriods() {
		for(Month month:Month.values()) {
			LocalDate ld=LocalDate.of(year, month, 10);
			LocalDate ld2=LocalDate.of(year, month, 25);
			periods.add(ld.format(dtf));
			periods.add(ld2.format(dtf));
		}
	}
//	public boolean
	public String getCurrentPeriod() {
		LocalDate currentDate=LocalDate.now();
		if(currentDate.getDayOfMonth()<11) {
			currentPeriod=LocalDate.of(year, currentDate.getMonth(), 10).format(dtf);
		}else if(currentDate.getDayOfMonth()>10) {
			currentPeriod=LocalDate.of(year, currentDate.getMonth(), 25).format(dtf);
		}
		return currentPeriod;
	}
	public static boolean isPeriodFinished(String period) {
		LocalDate today=LocalDate.now();
		LocalDate periodEndDate = LocalDate.parse(period, dtf);		
		if(today.isEqual(periodEndDate)||today.isAfter(periodEndDate))return true;
		return false;
	}
	
	public String getPrecedentPeriod(String period) {
		LocalDate ld = null;
		try {
			ld = LocalDate.parse(period, dtf);
		} catch (Exception e) {
			TimesheetPeriods tp=new TimesheetPeriods();
			period=tp.getCurrentPeriod();
		}
		String precedentPeriod ="";
		if(ld.getDayOfMonth()>10) {
			precedentPeriod=LocalDate.of(year, ld.getMonth(), 10).format(dtf);
		}else if(ld.getDayOfMonth()<11) {
			Month month=ld.getMonth().minus(1);	
			if(ld.getMonth().equals(Month.JANUARY)) {					
				precedentPeriod=LocalDate.of(ld.getYear()-1, month, 25).format(dtf);
			}else {
				precedentPeriod=LocalDate.of(year, month, month.minus(1).getValue()).format(dtf);
			}			
		}
		return precedentPeriod;
	}
	public static String findPeriod(String formattedDate) {
		LocalDate ld = null;
				try {
					ld=LocalDate.parse(formattedDate);
				} catch (Exception e) {
					ld=LocalDate.parse(formattedDate, dtf);
				}
		if(ld.getDayOfMonth()>10 && ld.getDayOfMonth()<26) {			
			return LocalDate.of(ld.getYear(), ld.getMonth(), 25).format(dtf);
		}
		return LocalDate.of(ld.getYear(), ld.getMonth(), 10).format(dtf);
	}
}
