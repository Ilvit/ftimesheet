package com.timesheet.constants;

import java.time.LocalDate;

public class IDGenerator {
	
	public String getGeneratedID() {
		String generatedID="";
		LocalDate ld=LocalDate.now();
		Long value=System.currentTimeMillis()/ld.getYear()+ld.getDayOfYear();
		generatedID="ES"+value.toString().toUpperCase();		
		return generatedID;
	}
}
