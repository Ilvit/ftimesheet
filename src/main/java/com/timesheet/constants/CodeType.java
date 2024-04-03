package com.timesheet.constants;

import com.timesheet.enums.DayType;

public class CodeType {

	public final static String REGULAR_DAYS="BUSDCH-24";
	public final static String PUBLIC_HOLIDAY="PUBHDCH-24";
	public final static String VACATION_DAYS="VACDCH-24";
	
	public static DayType getType(String dayCode) {
		if(dayCode.equals(PUBLIC_HOLIDAY))return DayType.PUBLIC_HOLIDAY;
		if(dayCode.equals(VACATION_DAYS))return DayType.VACATION_DAY;
		return DayType.REGULAR_DAY;
	}
}
