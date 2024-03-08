package com.timesheet.constants;

import com.timesheet.enums.DayType;

public class CodeType {

	public final static String businessDay="BUSDCH-24";
	public final static String publicHoliday="PUBHDCH-24";
	public final static String weekend="WEEDCH-24";
	
	public static DayType getType(String dayCode) {
		if(dayCode.equals(publicHoliday))return DayType.PUBLIC_HOLIDAY;
		if(dayCode.equals(weekend))return DayType.WEEKEND;
		return DayType.BUSINESS_DAY;
	}
}
