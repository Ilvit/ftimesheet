package com.timesheet.services;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.PeriodVars;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.dtos.TimesheetDTO;
import com.timesheet.dtos.TimesheetState;
import com.timesheet.entities.Employee;
import com.timesheet.entities.Sheetday;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.entities.USAIDProject;
import com.timesheet.entities.Vacation;
import com.timesheet.enums.DayType;
import com.timesheet.enums.Positions;
import com.timesheet.models.NotificationRequest;
import com.timesheet.models.Timesheet;
import com.timesheet.models.VacationReport;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.SheetdayRepository;
import com.timesheet.repositories.TimesheetSaverRepository;
import com.timesheet.repositories.USAIDProjectRepository;
import com.timesheet.repositories.VacationRepository;

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
	private TimesheetSaverRepository timesheetSaverRepository;
	@Autowired
	private USAIDProjectRepository usaidProjectRepository;		
	@Autowired
	private VacationRepository vacationRepository;
	
	
	public void insertTimesheetLine(String period, String employeeID, String daysCode, String project) {
		saveTimesheet(prepareNewTimesheetLine(TimesheetPeriods.findPeriod(period),employeeID,  daysCode, project));	
	}
	public void insertTimesheetLine(String period, List<String>employeesIDList, String daysCode, String project) {
		employeesIDList.forEach(employeeID->{
			saveTimesheet(prepareNewTimesheetLine(employeeID, period, daysCode, project));
		});		
	}
	
	public TimesheetDTO getNewTimesheet(String employeeID) {		
		Employee employee=employeeRepository.findByEmployeeID(employeeID);
		Employee supervisor=employeeRepository.findByEmployeeID(employee.getSupervisorID());
		TimesheetDTO timesheetDTO=new TimesheetDTO(new Timesheet());
		
		List<String>rdProjects=new ArrayList<>();
		List<String>hdProjects=new ArrayList<>();
		List<String>allProjects=new ArrayList<>();
		usaidProjectRepository.findAll().forEach(proj->{
			allProjects.add(proj.getName());
		});
		TimesheetPeriods tp=new TimesheetPeriods();
		timesheetSaverRepository.save(new TimesheetSaver(null, employeeID, getNextUsersPeriod(employeeID), false, false, false, false, false));
		TimesheetSaver ussl=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, getLastUsersPeriod(employeeID));
		TimesheetState tstate=new TimesheetState(tp.getCurrentPeriod(), employee, timesheetSaverRepository.findByEmployeeID(employeeID));
		tstate.setSupervisor(supervisor);
		tstate.setVacationReport(getAllVacationDays(employeeID));
		
		if(ussl!=null) {				
			ussl.setHasNewCreated(true);
			timesheetSaverRepository.save(ussl);
		}
		
		timesheetDTO.setEmployee(employee);
		timesheetDTO.setPeriodStates(tstate.getPeriodStates());
		timesheetDTO.setRdProjects(rdProjects);
		timesheetDTO.setHdProjects(hdProjects);
		timesheetDTO.setAllProjects(allProjects);  
		timesheetDTO.setTimesheetPeriod(tp.getCurrentPeriod());
		timesheetDTO.setTimesheetsPeriods(tstate.getUsersPeriods());
		saveTimesheet(timesheetDTO);
		return timesheetDTO;
	}
	
	public TimesheetDTO getTimesheet(String period, String employeeID) {		
		TimesheetState tstate=new TimesheetState(period, employeeRepository.findByEmployeeID(employeeID), timesheetSaverRepository.findByEmployeeID(employeeID));
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
		
		if(tstate.isTimesheetPeriodExists()) {	//if the timesheet of this period exists	
			fillTimesheet(LocalDate.parse(period, TimesheetPeriods.dtf), timesheet, timesheetDTO, employeeID, tstate.getUsersPeriods());				
			TimesheetSaver tsaved=timesheetSaverRepository.findByEmployeeIDAndPeriod(employeeID, period);
			if(TimesheetPeriods.isPeriodFinished(period)) {//the period is finished
				timesheet.setSignable(true);
				if(tsaved.isSigned()) {//if the timesheet is signed
					timesheet.setSigned(true);
					timesheet.setSignable(false);
					timesheet.setApprovable(true);
				}
				if(tsaved.isApproved()) {
					timesheet.setApproved(true);
					timesheet.setApprovable(false);
					timesheet.setApprovableByDAF(true);
				}
				if(tsaved.isRejected()) {//if it's not signed or it is rejected
					timesheet.setSignable(true);
					timesheet.setRejected(true);
					timesheet.setSigned(false);
				}					
								
			}
			timesheetDTO.setTimesheetPeriod(period);
			timesheetDTO.setTimesheet(timesheet);;
			return timesheetDTO;
			
		}
		return null;
	}
	
	public boolean initPrecedentPeriod(String period, String employeeID, String daysCode, String project) {
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
		TimesheetState tstate=new TimesheetState(period, employeeRepository.findByEmployeeID(employeeID), timesheetSaverRepository.findByEmployeeID(employeeID));
				
		fillTimesheetLists(LocalDate.parse(period, TimesheetPeriods.dtf), period, employeeID, CodeType.getType(daysCode), timesheet, project, timesheetDTO);
		timesheetDTO.setTimesheetsPeriods(tstate.getUsersPeriods());
		timesheetDTO.setTimesheet(timesheet);
		saveTimesheet(timesheetDTO);
		if(!tstate.isTimesheetExists()) {//if a timesheet doesn't exist
			timesheetSaverRepository.save(new TimesheetSaver(null, employeeID, period, false, false, false, false, false));
		}
		return true;
	}
	public TimesheetDTO getNewTimesheetLine(String period, String employeeID, String daysCode, String project) {
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
		TimesheetState tstate=new TimesheetState(period, employeeRepository.findByEmployeeID(employeeID), timesheetSaverRepository.findByEmployeeID(employeeID));
		
		if(tstate.isTimesheetExists()) {//if a timesheet exists
			fillTimesheetLists(LocalDate.parse(period, TimesheetPeriods.dtf), period, employeeID, CodeType.getType(daysCode), timesheet, project, timesheetDTO);
			timesheetDTO.setTimesheetsPeriods(tstate.getUsersPeriods());
			timesheetDTO.setTimesheet(timesheet);
			return timesheetDTO;
		}else {
			System.out.println("Any timesheet exists for this user");
		}
		return null;
	}
	public TimesheetState getTimesheetState(String employeeID) {
		Employee employee=null;
		Employee supervisor = null;
		try {
			employee=employeeRepository.findByEmployeeID(employeeID);
			supervisor = employeeRepository.findByEmployeeID(employee.getSupervisorID());
		} catch (Exception e) {
			supervisor = employeeRepository.findByEmployeeID(employeeID);
		}
		TimesheetState tstate=new TimesheetState(getNextUsersPeriod(employeeID), employee, timesheetSaverRepository.findByEmployeeID(employeeID));
		tstate.setSupervisor(supervisor);
		tstate.setVacationReport(getAllVacationDays(employeeID));
		tstate.setCop(employeeRepository.findByPosition(Positions.COP));
		return tstate;
	}
	
	public void saveTimesheet(TimesheetDTO timesheetDTO) {
		if(timesheetDTO.isRegularDaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getRegularDaysLine());
		}
		if(timesheetDTO.isHolidaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getHolidaysLine());
		}
		if(timesheetDTO.isVacationDaysPresent()) {
			sheetdayService.saveTimesheetLine(timesheetDTO.getTimesheet().getVacationDaysLine());
		}
		
		if(timesheetSaverRepository.findByEmployeeIDAndPeriod(timesheetDTO.getEmployee().getEmployeeID(), timesheetDTO.getTimesheetPeriod())==null) {
			TimesheetSaver userSavedSl=new TimesheetSaver(null, timesheetDTO.getEmployee().getEmployeeID(), timesheetDTO.getTimesheetPeriod(), false, false, false, false, false);
			timesheetSaverRepository.save(userSavedSl);			
		}
	}
	public boolean updateTimesheet(TimesheetDTO timesheetDTO) {
		
		  if(timesheetDTO.isRegularDaysPresent())sheetdayService.updateTimesheetLine(timesheetDTO.getTimesheet().getRegularDaysLine());
		  if(timesheetDTO.isHolidaysPresent())sheetdayService.updateTimesheetLine(timesheetDTO.getTimesheet().getHolidaysLine());
		 
		return true;
	}
		
	public boolean deleteTimesheet(String period, String employeeID) {
		return sheetdayService.deleteTimesheet(period, employeeID);
	}
	public boolean signTimesheet(String period, String employeeID) {
		sheetdayService.signTimesheet(period, employeeID);
		return true;
	}
	public void fictiveSignTimesheet(String period, String employeeID, boolean sendMail) {
		sheetdayService.fictiveSignTimesheet(TimesheetPeriods.findPeriod(period), employeeID, sendMail);
	}
	public void fictiveSignTimesheet(String period, List<String>employeeIDList, boolean sendMail) {
		if(!employeeIDList.isEmpty()) {
			employeeIDList.forEach(employeeID->{
				sheetdayService.fictiveSignTimesheet(period, employeeID, sendMail);
			});
		}		
	}
	public boolean approveTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {
		sheetdayService.approveTimesheet(period, employeeID, supervisorID, notification);
		return true;
	}
	public boolean fictiveApproveTimesheet(String period, String employeeID, boolean sendMail) {
		Employee employee=employeeRepository.findByEmployeeID(employeeID);
		sheetdayService.fictiveApproveTimesheet(TimesheetPeriods.findPeriod(period), employeeID, employee.getSupervisorID(), sendMail);
		return true;
	}
	public boolean fictiveApproveTimesheet(String period, List<String>employeeIDList, boolean sendMail) {
		if(!employeeIDList.isEmpty()) {
			employeeIDList.forEach(employeeID->{
				Employee employee=employeeRepository.findByEmployeeID(employeeID);
				sheetdayService.fictiveApproveTimesheet(period, employeeID, employee.getSupervisorID(), sendMail);
			});
		}	
		return true;
	}
	
	
	public boolean rejectTimesheet(String period, String employeeID, String supervisorID, NotificationRequest notification) {
		sheetdayService.rejectTimesheet(period, employeeID, supervisorID, notification);
		return true;
	}
	public List<USAIDProject>getAllProjects(){
		return usaidProjectRepository.findAll();
	}
	
	public VacationReport getAllVacationDays(String employeeID) {
		int totalHours = 0;
		int daysTaken=0;
		int otherWorkedHours=0;
		for(Sheetday sd:sheetdayRepository.findByEmployeeID(employeeID)) {
			totalHours+=sd.getHours();
		}
		for(Vacation va:vacationRepository.findByEmployeeID(employeeID)) {
			daysTaken+=va.getDaysTaken();
		}
		otherWorkedHours=employeeRepository.findByEmployeeID(employeeID).getOtherWorkedHours();
		totalHours+=otherWorkedHours;
		VacationReport vr=new VacationReport(totalHours, daysTaken);
		return vr;
	}

	private LocalDate getStartingDate(LocalDate endDate) {	
		PeriodVars pv=new PeriodVars(endDate);
		return pv.getPeriodStartDate();		
	}
	
	////n get a new timesheet line
	
	private void fillTimesheetLists(LocalDate ld, String period, String employeeID, DayType dayType, Timesheet timesheet,String project, TimesheetDTO timesheetDTO){
		List<String>allProjects=new ArrayList<>();
		List<String>rdProjects=new ArrayList<>();
		List<String>hdProjects=new ArrayList<>();
		List<String>vdProjects=new ArrayList<>();
		List<Sheetday>hold=new ArrayList<>();
		List<Sheetday>regd=new ArrayList<>();
		List<Sheetday>vacd=new ArrayList<>();
		
		//fill regular days and holidays
		regd=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.REGULAR_DAY);	
		hold=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.PUBLIC_HOLIDAY);	
		vacd=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.VACATION_DAY);	
		
		if(dayType.equals(DayType.PUBLIC_HOLIDAY)) {			
			hold.addAll(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.PUBLIC_HOLIDAY, project));				
							
		}else if(dayType.equals(DayType.REGULAR_DAY)) {
			regd.addAll(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.REGULAR_DAYS, project));
		}else if(dayType.equals(DayType.VACATION_DAY)) {
			vacd.addAll(sheetdayService.getNewTimesheetLine(period, employeeID, CodeType.VACATION_DAYS, project));
		}
		//fill holidays projects
		for(Sheetday sd:hold){
			if(!hdProjects.contains(sd.getProjectName()))hdProjects.add(sd.getProjectName());					
		}
		//fill regulardays projects
		for(Sheetday sd:regd){
			if(!rdProjects.contains(sd.getProjectName()))rdProjects.add(sd.getProjectName());
		}
		//fill vacationdays projects
		for(Sheetday sd:vacd){
			if(!vdProjects.contains(sd.getProjectName()))vdProjects.add(sd.getProjectName());
		}
		Employee employee=employeeRepository.findByEmployeeID(employeeID);			
		PeriodVars pv=new PeriodVars(ld);		
		// fill period dates and all projects names
		List<LocalDate>ldList=pv.getPeriodDates();
		
		usaidProjectRepository.findAll().forEach(proj->{
			allProjects.add(proj.getName());
		});
		
		if(!regd.isEmpty())timesheetDTO.setRegularDaysPresent(true);
		if(!hold.isEmpty())timesheetDTO.setHolidaysPresent(true);
		if(!vacd.isEmpty())timesheetDTO.setVacationDaysPresent(true);
		
		timesheet.loadHoursByLine(regd, hold, vacd, rdProjects, hdProjects, vdProjects);
		timesheet.loadVerticalTotalHours(regd, hold, vacd, ldList);
		
		timesheet.setHolidaysLine(hold);
		timesheet.setRegularDaysLine(regd);
		timesheet.setVacationDaysLine(vacd);
		
		timesheetDTO.setRdProjects(rdProjects);	
		timesheetDTO.setHdProjects(hdProjects);		
		timesheetDTO.setVdProjects(vdProjects);		
		timesheetDTO.setEmployee(employee);
		timesheetDTO.setPeriodDates(ldList);
		timesheetDTO.setAllProjects(allProjects);
		timesheetDTO.setTimesheet(timesheet);
	}
	private void fillTimesheet(LocalDate ld, Timesheet timesheet, TimesheetDTO timesheetDTO, String employeeID, List<String>userTimesheetPeriods) {
		List<String>rdProjects=new ArrayList<>();
		List<String>hdProjects=new ArrayList<>();
		List<String>vdProjects=new ArrayList<>();
		List<String>allProjects=new ArrayList<>();
	
		List<Sheetday>regd=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.REGULAR_DAY);
		List<Sheetday>hold=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.PUBLIC_HOLIDAY);
		List<Sheetday>vacd=sheetdayRepository.findBetweenDatesAndDayType(getStartingDate(ld), ld, employeeID, DayType.VACATION_DAY);
		
		//fill regulardaysProjects			
		for(Sheetday sd:regd) {
			if(!rdProjects.contains(sd.getProjectName()))rdProjects.add(sd.getProjectName());
		}		
		//fill holidaysProjects
		for(Sheetday sd:hold){
			if(!hdProjects.contains(sd.getProjectName()))hdProjects.add(sd.getProjectName());
		}
		//fill vacationdaysProjects
		for(Sheetday sd:vacd){
			if(!vdProjects.contains(sd.getProjectName()))vdProjects.add(sd.getProjectName());
		}
		//Fill period dates
		PeriodVars pv=new PeriodVars(ld);
		List<LocalDate>ldList=new ArrayList<>();
		for(LocalDate lod:pv.getPeriodDates()) {			
			ldList.add(lod);
		}		
		//fill all projects list
		usaidProjectRepository.findAll().forEach(proj->{
			allProjects.add(proj.getName());
		});
		if(!hold.isEmpty())timesheetDTO.setHolidaysPresent(true);
		if(!regd.isEmpty())timesheetDTO.setRegularDaysPresent(true);
		if(!vacd.isEmpty())timesheetDTO.setVacationDaysPresent(true);
		
		timesheet.loadVerticalTotalHours(regd, hold, vacd, ldList);
		timesheet.loadHoursByLine(regd, hold, vacd, rdProjects, hdProjects, vdProjects);
				
		timesheet.setHolidaysLine(hold);
		timesheet.setRegularDaysLine(regd);
		timesheet.setVacationDaysLine(vacd);
		Employee employee=employeeRepository.findByEmployeeID(employeeID);
		timesheetDTO.setHdProjects(hdProjects);
		timesheetDTO.setRdProjects(rdProjects);	
		timesheetDTO.setVdProjects(vdProjects);
		timesheetDTO.setEmployee(employee);
		timesheetDTO.setPeriodDates(ldList);
		timesheetDTO.setAllProjects(allProjects);
		timesheetDTO.setTimesheetsPeriods(userTimesheetPeriods);
		timesheetDTO.setTimesheet(timesheet);
	}
	private TimesheetDTO prepareNewTimesheetLine(String period, String employeeID, String daysCode, String project) {
		TimesheetDTO timesheetDTO=new TimesheetDTO();
		Timesheet timesheet=new Timesheet();
//		une vérification si la ligne existe est nécessaire à l'avenir
		fillTimesheetLists(LocalDate.parse(period, TimesheetPeriods.dtf), period, employeeID, CodeType.getType(daysCode), timesheet, project, timesheetDTO);
		timesheetDTO.setTimesheet(timesheet);
		timesheetDTO.setTimesheetPeriod(period);
		return timesheetDTO;
	}
	private String getNextUsersPeriod(String employeeID) {
		ArrayList<LocalDate>al=new ArrayList<>();
		List<TimesheetSaver>tsaverList=timesheetSaverRepository.findByEmployeeID(employeeID);
		
		if(!tsaverList.isEmpty()) {
			tsaverList.forEach(tsaver->{
				al.add(LocalDate.parse(tsaver.getPeriod(), TimesheetPeriods.dtf));				
			});
			LocalDate ld=Collections.max(al);
			if(ld.getDayOfMonth()==10) {
				return LocalDate.of(ld.getYear(), ld.getMonth(), 25).format(TimesheetPeriods.dtf);
			}else if(ld.getDayOfMonth()==25) {
				if(ld.getMonth().equals(Month.DECEMBER)) {
					return LocalDate.of(ld.getYear()+1, ld.getMonth().plus(1), 10).format(TimesheetPeriods.dtf);
				}else {
					return LocalDate.of(ld.getYear(), ld.getMonth().plus(1), 10).format(TimesheetPeriods.dtf);
				}
			}
		}
		return LocalDate.now().format(TimesheetPeriods.dtf);
	}
	private String getLastUsersPeriod(String employeeID) {
		ArrayList<LocalDate>al=new ArrayList<>();
		List<TimesheetSaver>tsaverList=timesheetSaverRepository.findByEmployeeID(employeeID);
		if(!tsaverList.isEmpty()) {
			tsaverList.forEach(tsaver->{
				al.add(LocalDate.parse(tsaver.getPeriod(), TimesheetPeriods.dtf));
			});
			return Collections.max(al).format(TimesheetPeriods.dtf);
		}
		return LocalDate.now().format(TimesheetPeriods.dtf);
	}
	
}
