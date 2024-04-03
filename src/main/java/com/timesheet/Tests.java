package com.timesheet;

import java.time.LocalDate;

import com.timesheet.constants.IDGenerator;
import com.timesheet.constants.TimesheetPeriods;

public class Tests {

	
	
	public static void main(String[] args) {
		
/*/		LocalDate localDate=LocalDate.of(2024, 2, 2);
		LocalDate localDate=LocalDate.now();
		DateTimeFormatter dtf=DateTimeFormatter.ofPattern("EEEE_dd-MMMM-yyyy", Locale.FRENCH);
		System.out.println(localDate.format(dtf));
		System.out.println(localDate.lengthOfYear());
		System.out.println(localDate.getDayOfWeek()+" "+localDate.getDayOfWeek().getValue());
		System.out.println(localDate.lengthOfMonth());
		System.out.println(Date.from(localDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant()));
		System.out.println(localDate.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear()));
		System.out.println(localDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth()));
		System.out.println("day of month: "+localDate.getDayOfMonth());
		System.out.println(localDate.getMonth());
		System.out.println(localDate.get(WeekFields.of(Locale.getDefault()).weekOfMonth()));
		LocalDate ld=LocalDate.parse("jeudi_22-février-2024", dtf);
		DateTimeFormatter dtf2=DateTimeFormatter.ofPattern("dd");
		System.out.println(ld.format(dtf2));
		System.out.println(ld);
//*/	
		LocalDate today=LocalDate.parse("mardi_19-mars-2024", TimesheetPeriods.dtf);
		LocalDate tomorrow=LocalDate.parse("mercredi_20-mars-2024", TimesheetPeriods.dtf);
		LocalDate now=LocalDate.now();
		System.out.println(today.isEqual(now));
		System.out.println(tomorrow.isAfter(today));
	}
}
