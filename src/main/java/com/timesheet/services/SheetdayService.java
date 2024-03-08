package com.timesheet.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.PeriodVars;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.Sheetday;
import com.timesheet.repositories.SheetdayRepository;
import com.timesheet.repositories.UserSavedSheetLineRepository;

@Service
@Transactional
public class SheetdayService {

	@Autowired
	private SheetdayRepository sheetdayRepository;
	@Autowired
	private UserSavedSheetLineRepository userSavedSheetLineRepository;
		
	public List<Sheetday>getNewTimesheetLine(String period, String employeeID, String dayCode){
		
		List<Sheetday>sheetdays=new ArrayList<>();
		LocalDate ld2 = null;
		try {
			ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (DateTimeParseException e) {
			System.out.println("Whrite the correct DateTimeFormatter !");
		}
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++) {
			Sheetday sd = null;
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sd=new Sheetday(null, employeeID, ld, 0, CodeType.getType(dayCode), false, false, false);
			if(ld.getDayOfWeek().equals(DayOfWeek.SATURDAY)||ld.getDayOfWeek().equals(DayOfWeek.SUNDAY))sd.setWeekend(true);
			sheetdays.add(sd);
		}
		return sheetdays;
	}
	
	public void saveTimesheetLine(List<Sheetday> sheetdays) {
		sheetdays.forEach(sd->{
			sheetdayRepository.save(sd);
		});
	}
	public boolean updateTimesheetLine(List<Sheetday> sheetdays) {
		sheetdays.forEach(sd->{
			Sheetday sday=sheetdayRepository.findById(sd.getId()).get();
			sday.setEmployeeID(sd.getEmployeeID());
			sday.setHours(sd.getHours());
			sheetdayRepository.save(sday);
		});
		return true;
	}
	public boolean deleteTimesheetLine(String period, String employeeID, String dayCode) {
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		if(dayCode.isEmpty())dayCode=CodeType.businessDay;
		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.deleteByDateAndEmployeeIDAndDayType(ld, employeeID, CodeType.getType(dayCode));
		}		
		return true;
	}
	public boolean deleteTimesheet(String period, String employeeID) {
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.deleteByDateAndEmployeeID(ld, employeeID);
		}
		userSavedSheetLineRepository.deleteByEmployeeIDAndPeriod(employeeID, period);		
		return true;
	}
		
	
}
