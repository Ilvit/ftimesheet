package com.timesheet.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.dtos.TimesheetDTO;
import com.timesheet.entities.Employee;
import com.timesheet.entities.UserSavedSheetLines;
import com.timesheet.enums.DayType;
import com.timesheet.mappers.Timesheet;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.SheetdayRepository;
import com.timesheet.repositories.UserSavedSheetLineRepository;

@Service
@Transactional
public class TimesheetService {
	@Autowired
	private SheetdayRepository sheetdayRepository;
	@Autowired
	private SheetdayService sheetdayService;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private UserSavedSheetLineRepository savedSheetLineRepository;
	
	public TimesheetDTO getTimesheet(String period, String employeeID) {		
		TimesheetPeriods tp=new TimesheetPeriods();
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
		
		List<String>usersTimesheetPeriod=new ArrayList<>();
		
		savedSheetLineRepository.findByEmployeeID(employeeID).forEach(ussl->{
			usersTimesheetPeriod.add(ussl.getPeriod());
		});
		if(usersTimesheetPeriod.isEmpty()) {//There is no timesheet for this user
			timesheetDTO.setTimesheet(timesheet);
			return timesheetDTO;
		}else {//There is timesheet lines
			if(usersTimesheetPeriod.contains(TimesheetPeriods.currentPeriod)) {//Current period
				timesheetDTO.setTimesheetPeriod(TimesheetPeriods.currentPeriod);
				fillTimesheet(LocalDate.parse(TimesheetPeriods.currentPeriod, TimesheetPeriods.dtf), timesheet, timesheetDTO, employeeID, usersTimesheetPeriod);
				return timesheetDTO;
			}else if(usersTimesheetPeriod.contains(tp.getPrecedentPeriod(period))) {//precedent period
				timesheetDTO.setTimesheetPeriod(tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod));
				fillTimesheet(LocalDate.parse(tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod), TimesheetPeriods.dtf), timesheet, timesheetDTO, employeeID, usersTimesheetPeriod);
				return timesheetDTO;
			}
		}
		return null;
	}
	
	public TimesheetDTO getNewTimesheetLine(String period, String employeeID, String daysCode) {
		TimesheetPeriods tp=new TimesheetPeriods();
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
		
		List<String>usersTimesheetPeriod=new ArrayList<>();
		
		savedSheetLineRepository.findByEmployeeID(employeeID).forEach(ussl->{
			usersTimesheetPeriod.add(ussl.getPeriod());
		});
		
		if(usersTimesheetPeriod.isEmpty()) {//The user doesn't have any timesheet
			System.out.println("empty timesheet?: "+usersTimesheetPeriod.isEmpty());
			LocalDate ld = LocalDate.parse(TimesheetPeriods.currentPeriod, TimesheetPeriods.dtf);
			fillTimesheetLists(ld, period, employeeID, CodeType.getType(daysCode), timesheet, timesheetDTO);
			if(!usersTimesheetPeriod.contains(period))usersTimesheetPeriod.add(period);
			timesheetDTO.setTimesheetsPeriods(usersTimesheetPeriod);
			
			return timesheetDTO;
		}else {//A period exists
			if(usersTimesheetPeriod.contains(TimesheetPeriods.currentPeriod)) {//if it's the current period
				LocalDate ld = LocalDate.parse(TimesheetPeriods.currentPeriod, TimesheetPeriods.dtf);
				fillTimesheetLists(ld, period, employeeID, CodeType.getType(daysCode), timesheet, timesheetDTO);
				
				timesheetDTO.setTimesheetsPeriods(usersTimesheetPeriod);
								
				return timesheetDTO;
			}else if(usersTimesheetPeriod.contains(tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod))) {//if it is the precedent period
				System.out.println("Attention exécution de celle-ci aussi !");
//				if it's signed and approved
				if(savedSheetLineRepository.findByEmployeeIDAndPeriod(employeeID, tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod)).isSigned()&&savedSheetLineRepository.findByEmployeeIDAndPeriod(employeeID, tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod)).isApproved()) {
					fillTimesheetLists(LocalDate.parse(TimesheetPeriods.currentPeriod, TimesheetPeriods.dtf), period, employeeID, CodeType.getType(daysCode), timesheet, timesheetDTO);
					timesheetDTO.setTimesheetsPeriods(usersTimesheetPeriod);
				}
			}
		}
		
		return null;
	}
	
	public boolean saveTimesheet(TimesheetDTO timesheetDTO) {
		if(timesheetDTO.isBusinessDaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getBusinessDaysLine());
		}
		if(timesheetDTO.isHolidaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getHolidaysLine());
		}
		if(timesheetDTO.isWeekendDaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getWeekendDaysLine());
		}
		
		if(savedSheetLineRepository.findByEmployeeIDAndPeriod(timesheetDTO.getEmployee().getId(), timesheetDTO.getTimesheetPeriod())==null) {
			UserSavedSheetLines userSavedSl=new UserSavedSheetLines(null, timesheetDTO.getEmployee().getId(), timesheetDTO.getTimesheetPeriod(), false, false);
			savedSheetLineRepository.save(userSavedSl);
		}		
		return true;
	}
	public boolean updateTimesheet(TimesheetDTO timesheetDTO) {
		if(timesheetDTO.isBusinessDaysPresent())sheetdayService.updateTimesheetLine(timesheetDTO.getTimesheet().getBusinessDaysLine());
		if(timesheetDTO.isHolidaysPresent())sheetdayService.updateTimesheetLine(timesheetDTO.getTimesheet().getHolidaysLine());
		if(timesheetDTO.isWeekendDaysPresent())sheetdayService.updateTimesheetLine(timesheetDTO.getTimesheet().getWeekendDaysLine());
		return true;
	}
	public boolean deleteTimesheetLine(String period, String employeeID, String daysCode) {
		sheetdayService.deleteTimesheetLine(period, employeeID, daysCode);
		LocalDate ld=LocalDate.parse(period, TimesheetPeriods.dtf);
		System.out.println(sheetdayRepository.findBetweenDates(getStartingDate(ld), ld, employeeID).isEmpty());
		if(sheetdayRepository.findBetweenDates(getStartingDate(ld), ld, employeeID).isEmpty()) {
			savedSheetLineRepository.deleteByEmployeeIDAndPeriod(employeeID, period);
		}
		return true;		
	}
	public boolean deleteTimesheet(String period, String employeeID) {
		return sheetdayService.deleteTimesheet(period, employeeID);
	}
	
	private LocalDate getStartingDate(LocalDate endDate) {
		int year=endDate.getYear();
		int month=endDate.getMonthValue();
		int beginingDay;
		int endDay=endDate.getDayOfMonth();
		if(endDay>15) {
			beginingDay=16;
		}else beginingDay=1;
		LocalDate ld1=LocalDate.of(year, month, beginingDay);
		return ld1;		
	}
	private void fillTimesheetLists(LocalDate ld, String period, String employeeID, DayType dayType, Timesheet timesheet, TimesheetDTO timesheetDTO){
		if(dayType.equals(DayType.PUBLIC_HOLIDAY)) {			
			timesheet.setHolidaysLine(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.publicHoliday));
			timesheet.setBusinessDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.BUSINESS_DAY));
			timesheet.setWeekendDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.WEEKEND));
		}else if(dayType.equals(DayType.WEEKEND)) {
			timesheet.setWeekendDaysLine(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.weekend));
			timesheet.setBusinessDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.BUSINESS_DAY));
			timesheet.setHolidaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.PUBLIC_HOLIDAY));
		}else if(dayType.equals(DayType.BUSINESS_DAY)) {
			timesheet.setBusinessDaysLine(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.businessDay));
			timesheet.setHolidaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.PUBLIC_HOLIDAY));
			timesheet.setWeekendDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.WEEKEND));
		}
		Employee employee=employeeRepository.findById(employeeID).get();
		
		if(timesheet.getBusinessDaysLine().isEmpty()) {
			timesheetDTO.setBusinessDaysPresent(false);
		}else timesheetDTO.setBusinessDaysPresent(true);
		if(timesheet.getHolidaysLine().isEmpty()) {
			timesheetDTO.setHolidaysPresent(false);
		}else timesheetDTO.setHolidaysPresent(true);
		if(timesheet.getWeekendDaysLine().isEmpty()) {
			timesheetDTO.setWeekendDaysPresent(false);
		}else timesheetDTO.setWeekendDaysPresent(true);
		
		timesheet.loadVerticalTotalHours();
		timesheetDTO.setEmployee(employee);
		timesheetDTO.setTimesheet(timesheet);
	}
	private void fillTimesheet(LocalDate ld, Timesheet timesheet, TimesheetDTO timesheetDTO, String employeeID, List<String>userTimesheetPeriods) {
		timesheet.setBusinessDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.BUSINESS_DAY));
		timesheet.setHolidaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.PUBLIC_HOLIDAY));
		timesheet.setWeekendDaysLine(sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.WEEKEND));
		
		if(timesheet.getBusinessDaysLine().isEmpty()) {
			timesheetDTO.setBusinessDaysPresent(false);
		}else timesheetDTO.setBusinessDaysPresent(true);
		if(timesheet.getHolidaysLine().isEmpty()) {
			timesheetDTO.setHolidaysPresent(false);
		}else timesheetDTO.setHolidaysPresent(true);
		if(timesheet.getWeekendDaysLine().isEmpty()) {
			timesheetDTO.setWeekendDaysPresent(false);
		}else timesheetDTO.setWeekendDaysPresent(true);
		
		timesheet.loadVerticalTotalHours();
		
		Employee employee=employeeRepository.findById(employeeID).get();
		timesheetDTO.setEmployee(employee);
		timesheetDTO.setTimesheetsPeriods(userTimesheetPeriods);
		timesheetDTO.setTimesheet(timesheet);
	}
	

}
