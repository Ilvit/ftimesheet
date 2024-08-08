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
import com.timesheet.entities.Employee;
import com.timesheet.entities.Notification;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.enums.Positions;
import com.timesheet.models.NotificationRequest;
import com.timesheet.models.TimesheetMail;
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
	@Autowired
	private TimesheetMailService mailService;		
		
	public List<Sheetday>getNewTimesheetLine(String period, String employeeID, String dayCode, String project){
		
		List<Sheetday>sheetdays=new ArrayList<>();		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			Sheetday sd = null;
			
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
		if(dayCode.isEmpty())dayCode=CodeType.REGULAR_DAYS;		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.deleteByDateAndEmployeeIDAndDayTypeAndProjectName(ld, employeeID, CodeType.getType(dayCode), project);
		}
	}
	
	public boolean deleteTimesheetLine(String period, String employeeID, String dayCode) {		
		if(dayCode.isEmpty())dayCode=CodeType.REGULAR_DAYS;		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.deleteByDateAndEmployeeIDAndDayType(ld, employeeID, CodeType.getType(dayCode));
		}		
		return true;
	}
	
	public boolean deleteTimesheet(String period, String employeeID) {		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates() ) {
			sheetdayRepository.deleteByDateAndEmployeeID(ld, employeeID);
		}
		timesheetSaverRepository.deleteByEmployeeIDAndPeriod(employeeID, period);		
		return true;
	}
	public boolean signTimesheet(String period, String employeeID) {	
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.findBetweenDates(ld, pv.getPeriodEndDate(), employeeID).forEach(sd->{
				sd.setSigned(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}	
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		ussl.setSigned(true);
		ussl.setRejected(false);
		timesheetSaverRepository.save(ussl);
		
		Employee sender=employeeRepository.findByEmployeeID(employeeID);
		Employee supervisor=employeeRepository.findByEmployeeID(sender.getSupervisorID());
		
		notificationRepository.save(new Notification(null, sender, supervisor.getEmployeeID(), "Timesheet signed", "My timesheet is signed. May you approve it please?", Instant.now(), period, false,""));
		
		new Thread(()->{			
			mailService.sendMail(supervisor.getMail(), new TimesheetMail("Timesheet signed",sender.getPosition()+" "+sender.getNickName()+" "+sender.getName()+" has signed his timesheet. Please check your timesheet notifications"));
		}).start();
		
		return true;
	}
	public void fictiveSignTimesheet(String period, String employeeID, boolean sendMail) {	
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.findBetweenDates(ld, pv.getPeriodEndDate(), employeeID).forEach(sd->{
				sd.setSigned(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}	
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		ussl.setSigned(true);
		ussl.setRejected(false);
		timesheetSaverRepository.save(ussl);
		
		Employee sender=employeeRepository.findByEmployeeID(employeeID);
		Employee supervisor=employeeRepository.findByEmployeeID(sender.getSupervisorID());
		
		notificationRepository.save(new Notification(null, sender, supervisor.getEmployeeID(), "Timesheet signed", "My timesheet is signed. May you approve it please?", Instant.now(), period, false,""));
		if(sendMail) {
			new Thread(()->{			
				mailService.sendMail(supervisor.getMail(), new TimesheetMail("Timesheet signed",sender.getPosition()+" "+sender.getNickName()+" "+sender.getName()+" has signed his timesheet. Please check your timesheet notifications"));
			}).start();
		}
	}
	public boolean approveTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.findBetweenDates(ld, pv.getPeriodEndDate(), employeeID).forEach(sd->{
				sd.setApproved(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}			
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		Employee sender=employeeRepository.findByEmployeeID(supervisorID);
		Employee employee=employeeRepository.findByEmployeeID(employeeID);
		Employee daf=employeeRepository.findByPosition(Positions.DAF);
		
		ussl.setSigned(true);
		ussl.setRejected(false);
		ussl.setApproved(true);
		
		timesheetSaverRepository.save(ussl);
		new Thread(()->{
			notificationRepository.save(new Notification(null, sender, employeeID, notification.getMsgObject(), notification.getMsgBody(), Instant.now(), period, false, ""));//from supervisor to supervised
			if(!employeeID.equals(daf.getEmployeeID()))notificationRepository.save(new Notification(null, sender, daf.getEmployeeID(), employee.getPostName()+" "+employee.getNickName()+" timesheet", "I'm sending you "+employee.getName()+" "+employee.getPostName()+" "+employee.getNickName()+"'s timesheet which i approved", Instant.now(), period, false, employeeID));	//from supervisor #DAF to the DAF	
			
			try {
				if(!sender.equals(daf))mailService.sendMail(daf.getMail(), new TimesheetMail("Timesheet approved","The "+sender.getPosition()+" "+sender.getName()+" sent you "+employee.getName()+" "+employee.getPostName()+" "+employee.getNickName()+"'s timesheet which he approved. Please check your timesheet notifications"));
			} catch (Exception e) {
				System.out.println("No network coverage !");
			}
		}).start();
				
		return true;
	}
	public void fictiveApproveTimesheet(String period, String employeeID, String supervisorID, boolean sendMail) {		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.findBetweenDates(ld, pv.getPeriodEndDate(), employeeID).forEach(sd->{
				sd.setApproved(true);
				sd.setRejected(false);
				sheetdayRepository.save(sd);
			});
		}			
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		Employee sender=employeeRepository.findByEmployeeID(supervisorID);
		Employee employee=employeeRepository.findByEmployeeID(employeeID);
		Employee daf=employeeRepository.findByPosition(Positions.DAF);
		
		ussl.setSigned(true);
		ussl.setRejected(false);
		ussl.setApproved(true);
		
		timesheetSaverRepository.save(ussl);
		new Thread(()->{
			notificationRepository.save(new Notification(null, sender, employeeID, "Approbation", "Your timesheet for "+period+" period is approved", Instant.now(), period, false, ""));//from supervisor to supervised
			if(!employeeID.equals(daf.getEmployeeID()))notificationRepository.save(new Notification(null, sender, daf.getEmployeeID(), employee.getPostName()+" "+employee.getNickName()+" timesheet", "I'm sending you "+employee.getName()+" "+employee.getPostName()+" "+employee.getNickName()+"'s timesheet which i approved", Instant.now(), period, false, employeeID));	//from supervisor #DAF to the DAF	
			
			try {
				if(!sender.equals(daf) && sendMail)mailService.sendMail(daf.getMail(), new TimesheetMail("Timesheet approved","The "+sender.getPosition()+" "+sender.getName()+" sent you "+employee.getName()+" "+employee.getPostName()+" "+employee.getNickName()+"'s timesheet which he approved. Please check your timesheet notifications"));
			} catch (Exception e) {
				System.out.println("No network coverage !");
			}
		}).start();				
	}
	public boolean rejectTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {
		
		PeriodVars pv=new PeriodVars(period);
		for(LocalDate ld:pv.getPeriodDates()) {
			sheetdayRepository.findBetweenDates(ld, pv.getPeriodEndDate(), employeeID).forEach(sd->{
				sd.setApproved(false);
				sd.setRejected(true);
				sd.setSigned(false);
				sheetdayRepository.save(sd);
			});
		}			
		TimesheetSaver tsaver=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
		tsaver.setSigned(false);
		tsaver.setRejected(true);
		tsaver.setApproved(false);
		timesheetSaverRepository.save(tsaver);	
		new Thread(()->{
			Employee sender=employeeRepository.findByEmployeeID(supervisorID);
			String employeeMail=employeeRepository.findByEmployeeID(employeeID).getMail();
			notificationRepository.save(new Notification(null, sender, employeeID, notification.getMsgObject(), notification.getMsgBody(), Instant.now(), period, false, ""));			
			mailService.sendMail(employeeMail, new TimesheetMail("Timesheet rejected","Your timesheet is not approved. Please see the amendments into your timesheet notifications"));
		}).start();
		
		return true;
	}
		
}
