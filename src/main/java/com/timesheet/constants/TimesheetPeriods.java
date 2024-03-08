package com.timesheet.constants;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TimesheetPeriods {

	private int year=LocalDate.now().getYear();
	private List<String>periods=new ArrayList<>();
	public static String currentPeriod;
	public static DateTimeFormatter dtf=DateTimeFormatter.ofPattern("EEEE_dd-MMMM-yyyy");
	
	public TimesheetPeriods() {
		for(Month month:Month.values()) {
			LocalDate ld=LocalDate.of(year, month, 15);
			LocalDate ld2=LocalDate.of(year, month, ld.lengthOfMonth());
			periods.add(ld.format(dtf));
			periods.add(ld2.format(dtf));
		}
	}
//	public boolean
	public String getCurrentPeriod() {
		LocalDate currentDate=LocalDate.now();
		if(currentDate.getDayOfMonth()<16) {
			currentPeriod=LocalDate.of(year, currentDate.getMonth(), 15).format(dtf);
		}else if(currentDate.getDayOfMonth()>15) {
			currentPeriod=LocalDate.of(year, currentDate.getMonth(), currentDate.lengthOfMonth()).format(dtf);
		}
		return currentPeriod;
	}
	public String getPrecedentPeriod(String period) {
		LocalDate ld = null;
		try {
			ld = LocalDate.parse(period, dtf);
		} catch (DateTimeParseException e) {
			TimesheetPeriods tp=new TimesheetPeriods();
			period=tp.getCurrentPeriod();
		}
		String precedentPeriod ="";
		if(ld.getDayOfMonth()>15) {
			precedentPeriod=LocalDate.of(year, ld.getMonth(), 15).format(dtf);
		}else if(ld.getDayOfMonth()<16) {
			Month month=ld.getMonth().minus(1);	
			if(ld.getMonth().equals(Month.JANUARY)) {					
				precedentPeriod=LocalDate.of(ld.getYear()-1, month, month.length(ld.minusYears(1).isLeapYear())).format(dtf);
			}else {
				precedentPeriod=LocalDate.of(year, month, month.length(ld.minusYears(1).isLeapYear())).format(dtf);
			}			
		}
		return precedentPeriod;
	}
}
