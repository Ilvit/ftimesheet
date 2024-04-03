package com.timesheet.services;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.PeriodVars;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.Employee;
import com.timesheet.entities.Notification;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.enums.Positions;
import com.timesheet.mappers.NotificationRequest;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.NotificationRepository;
import com.timesheet.repositories.SheetdayRepository;
import com.timesheet.repositories.TimesheetSaverRepository;

@Service
@Transactional
public class SheetdayService {

	@Autowired
	private SheetdayRepository sheetdayRepository;
	@Autowired
	private TimesheetSaverRepository timesheetSaverRepository;
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
		
		
	public List<Sheetday>getNewTimesheetLine(String period, String employeeID, String dayCode, String project){
		
		List<Sheetday>sheetdays=new ArrayList<>();
		LocalDate ld2 = null;
		try {
			ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			System.out.println("Write the correct DateTimeFormatter !");
		}
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++) {
			Sheetday sd = null;
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			
			sd=new Sheetday(null, employeeID, ld, 0, CodeType.getType(dayCode), project, false, false, false, false, false);
			if(ld.getDayOfWeek().equals(DayOfWeek.SATURDAY)||ld.getDayOfWeek().equals(DayOfWeek.SUNDAY))sd.setWeekend(true);
			if(holidayService.isHoliday(ld))sd.setHoliday(true);
			sheetdays.add(sd);
		}
		return sheetdays;
	}
	
	public void saveTimesheetLine(List<Sheetday> sheetdayLines) {
		sheetdayLines.forEach(sd->{ 
			sheetdayRepository.save(sd); 
		});
	}
	public boolean updateTimesheetLine(List<Sheetday> sheetdays) {
		sheetdays.forEach(sd->{
			Sheetday sday=sheetdayRepository.findById(sd.getId()).get();
			sday.setEmployeeID(sd.getEmployeeID());
			sday.setHours(sd.getHours());
			sday.setProjectName(sd.getProjectName());
			sheetdayRepository.save(sday);
		});
		return true;
	}
	public void deleteProjectTimesheetLine(String period, String employeeID, String dayCode, String project) {
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		if(dayCode.isEmpty())dayCode=CodeType.REGULAR_DAYS;		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.deleteByDateAndEmployeeIDAndDayTypeAndProjectName(ld, employeeID, CodeType.getType(dayCode), project);
		}
	}
	
	public boolean deleteTimesheetLine(String period, String employeeID, String dayCode) {
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);
		if(dayCode.isEmpty())dayCode=CodeType.REGULAR_DAYS;
		
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
		timesheetSaverRepository.deleteByEmployeeIDAndPeriod(employeeID, period);		
		return true;
	}
	public boolean signTimesheet(String period, String employeeID) {
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.findBetweenDates(ld, ld2, employeeID).forEach(sd->{
				sd.setSigned(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}	
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		ussl.setSigned(true);
		ussl.setRejected(false);
		timesheetSaverRepository.save(ussl);
		
		String supervisorID=employeeRepository.findById(employeeID).get().getSupervisorID();
		Employee sender=employeeRepository.findById(employeeID).get();
		notificationRepository.save(new Notification(null, sender, supervisorID, "Timesheet signed", "My timesheet is signed. May you approve it please?", Instant.now(), period, false,""));
		
		return true;
	}
	public boolean approveTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {		
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.findBetweenDates(ld, ld2, employeeID).forEach(sd->{
				sd.setApproved(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}			
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		ussl.setSigned(true);
		ussl.setRejected(false);
		ussl.setApproved(true);
		timesheetSaverRepository.save(ussl);
		new Thread(()->{
			Employee sender=employeeRepository.findById(supervisorID).get();
			Employee employee=employeeRepository.findById(employeeID).get();
			String COPID=employeeRepository.findByPosition(Positions.COP).getId();
			notificationRepository.save(new Notification(null, sender, employeeID, notification.getMsgObject(), notification.getMsgBody(), Instant.now(), period, false, ""));
			notificationRepository.save(new Notification(null, sender, COPID, employee.getPostName()+" "+employee.getNickName()+" timesheet", "I'm sending you "+employee.getName()+" "+employee.getPostName()+" "+employee.getNickName()+"'s timesheet which i approved", Instant.now(), period, false, employeeID));
		}).start();
		
		return true;
	}
	public boolean rejectTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {
		
		LocalDate ld2 = LocalDate.parse(period, TimesheetPeriods.dtf);		
		PeriodVars pv=new PeriodVars(ld2);
		for(int i=pv.getStart();i<=pv.getEnd();i++ ) {
			LocalDate ld=LocalDate.of(pv.getYear(), pv.getMonth(), i);
			sheetdayRepository.findBetweenDates(ld, ld2, employeeID).forEach(sd->{
				sd.setApproved(false);
				sd.setRejected(true);
				sd.setSigned(false);
				sheetdayRepository.save(sd);
			});
		}			
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		ussl.setSigned(false);
		ussl.setRejected(true);
		ussl.setApproved(false);
		ussl.setApprovedByCOP(false);
		timesheetSaverRepository.save(ussl);	
		new Thread(()->{
			Employee sender=employeeRepository.findById(supervisorID).get();
			notificationRepository.save(new Notification(null, sender, employeeID, notification.getMsgObject(), notification.getMsgBody(), Instant.now(), period, false, ""));
		}).start();
		
		return true;
	}
		
}
