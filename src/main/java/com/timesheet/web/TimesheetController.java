package com.timesheet.web;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.dtos.EmployeeDTO;
import com.timesheet.dtos.NotificationResponseDTO;
import com.timesheet.dtos.TimesheetDTO;
import com.timesheet.dtos.TimesheetState;
import com.timesheet.dtos.UsersDTO;
import com.timesheet.dtos.VacationDTO;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Employee;
import com.timesheet.entities.Holiday;
import com.timesheet.entities.Notification;
import com.timesheet.entities.USAIDProject;
import com.timesheet.entities.Vacation;
import com.timesheet.models.ConnectedUser;
import com.timesheet.models.ConnectedUserModel;
import com.timesheet.models.NotificationRequest;
import com.timesheet.services.AppUserService;
import com.timesheet.services.EmployeeService;
import com.timesheet.services.HolidayService;
import com.timesheet.services.NotificationService;
import com.timesheet.services.ProjectService;
import com.timesheet.services.SheetdayService;
import com.timesheet.services.TimesheetService;
import com.timesheet.services.VacationService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/timesheet")
public class TimesheetController {
	@Autowired
	private TimesheetService timesheetService;
	@Autowired
	private HolidayService holidayService;
	@Autowired
	private AppUserService appUserService;
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private SheetdayService sheetdayService;
	@Autowired
	private VacationService vacationService;
	@Autowired
	private ProjectService projectService;
	
	private TimesheetPeriods tp=new TimesheetPeriods();
	
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/timesheetstate")
	public TimesheetState getTimesheetState(@RequestParam(name = "eid") String employeeID) {
		return timesheetService.getTimesheetState(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("")
	public TimesheetDTO getTimeSheetLine(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}		
		return timesheetService.getTimesheet(period, employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADM4')")
	@GetMapping("/userstimesheet")
	public TimesheetDTO getTimeSheet(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID) {
		return timesheetService.getTimesheet(TimesheetPeriods.findPeriod(period), employeeID);
	}
	
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/newtimesheet")
	public TimesheetDTO getNewTimesheet(@RequestParam(name="eid",defaultValue = "") String employeeID){		
		return timesheetService.getNewTimesheet(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/insertline")
	public boolean insertNewTimesheetLine(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.REGULAR_DAYS) String daysCode, @RequestParam(name="proj") String project){
		
		timesheetService.insertTimesheetLine(period, employeeID, daysCode, project);
		return true;
	}
	
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/newline")
	public TimesheetDTO getNewTimesheetLine(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.REGULAR_DAYS) String daysCode, @RequestParam(name="proj") String project){
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		TimesheetDTO tsd=timesheetService.getNewTimesheetLine(period, employeeID, daysCode, project);
		return tsd;
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/initprecedent")
	public boolean initPrecedentPeriod(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.REGULAR_DAYS) String daysCode, @RequestParam(name="proj") String project){		
		timesheetService.initPrecedentPeriod(tp.getPrecedentPeriod(tp.getCurrentPeriod()), employeeID, daysCode, project);
		return true;
	}
	
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@PostMapping("/save")
	public TimesheetDTO saveTimesheet(@RequestParam(name="per", defaultValue = "")String period, @RequestBody TimesheetDTO timesheetDTO) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		timesheetDTO.setTimesheetPeriod(period);
		timesheetService.saveTimesheet(timesheetDTO);
		return timesheetService.getTimesheet(period, timesheetDTO.getEmployee().getEmployeeID());
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/sign")
	public boolean signTimesheet(@RequestParam(name="eid") String employeeID, @RequestParam(name="per") String period) {
		return timesheetService.signTimesheet(period, employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/fictsign")
	public boolean fictiveSignTimesheet(@RequestParam(name="eid") String employeeID, @RequestParam(name="per") String period, @RequestParam(name = "smail", defaultValue = "false")boolean sendMail) {
		timesheetService.fictiveSignTimesheet(period, employeeID, sendMail);
		return true;
	}
	
	@PreAuthorize("hasAuthority('SCOPE_SUPERVISOR')")
	@PostMapping("/approve")
	public boolean approveTimesheet(@RequestParam(name="eid") String employeeID, @RequestParam(name="sid") String supervisorID,
			@RequestParam(name="per") String period, @RequestBody NotificationRequest notification) {
		return timesheetService.approveTimesheet(period, employeeID, supervisorID, notification);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/fictapprove")
	public boolean fictiveApproveTimesheet(@RequestParam(name="eid") String employeeID, @RequestParam(name="per") String period, @RequestParam(name = "smail")boolean sendMail ) {
		return timesheetService.fictiveApproveTimesheet(employeeID, period, sendMail);
	}
	
	@PreAuthorize("hasAuthority('SCOPE_SUPERVISOR')")
	@PostMapping("/reject")
	public boolean rejectTimesheet(@RequestParam(name="eid") String employeeID, @RequestParam(name="sid") String supervisorID,
			@RequestParam(name="per") String period, @RequestBody NotificationRequest notification) {
		return timesheetService.rejectTimesheet(period, employeeID, supervisorID, notification);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@DeleteMapping("/deleteprojectline")
	public TimesheetDTO deleteProjectTimesheetLine(@RequestParam(name="per", defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.REGULAR_DAYS) String daysCode, @RequestParam(name="proj")String project) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		sheetdayService.deleteProjectTimesheetLine(period, employeeID, daysCode, project);
		return timesheetService.getTimesheet(period, employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@DeleteMapping("/deleteline")
	public TimesheetDTO deleteTimesheetLine(@RequestParam(name="per", defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.REGULAR_DAYS) String daysCode) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		sheetdayService.deleteTimesheetLine(period, employeeID, daysCode);
		return timesheetService.getTimesheet(period, employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@DeleteMapping("/deletetimesheet")
	public boolean deleteTimesheet(@RequestParam(name="per", defaultValue = "") String period, @RequestParam(name = "eid") String employeeID) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		return timesheetService.deleteTimesheet(period, employeeID);
	}
	
	//Holidays
	
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/holidays")
	public List<Holiday>getHolidays(){
		return holidayService.getHolidays();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/holidays/{id}")
	public Holiday getHoliday(@PathVariable Long id){
		return holidayService.getHoliday(id);
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/holidays/new")
	public Holiday getNewHoliday(){
		return new Holiday(null,LocalDate.now(),"");
	}
	
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PostMapping("/holidays/save")
	public List<Holiday>saveHoliday(@RequestBody Holiday holiday){
		holidayService.saveHoliday(holiday);
		return holidayService.getHolidays();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PostMapping("/holidays/delete")
	public List<Holiday>deleteHoliday(@RequestBody Holiday holiday){
		holidayService.removeHoliday(holiday.getDate());
		return holidayService.getHolidays();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PutMapping("/holidays/update")
	public List<Holiday>updateHoliday(@RequestBody Holiday holiday){
		holidayService.updateHoliday(holiday.getId(), holiday.getDate(), holiday.getDescription());
		return holidayService.getHolidays();
	}
	
	//Users
	
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/users/user")
	public AppUser getAppUser(@RequestParam(name="un") String username){
		return appUserService.getUser(username);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/users")
	public UsersDTO getAllUsers(){
		return appUserService.getAllAppUsers();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/users/new")
	public AppUser getNewAppUser(){
		return new AppUser();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@PostMapping("/users/save")
	public UsersDTO saveUser(@RequestBody AppUser user){
		appUserService.saveUser(user);
		return appUserService.getAllAppUsers();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@DeleteMapping("/users/delete")
	public UsersDTO deleteUser(@RequestParam(name="un") String username){
		appUserService.deleteUser(username);
		return appUserService.getAllAppUsers();
	} 
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@PutMapping("/users/update")
	public UsersDTO updateUser(@RequestBody AppUser user){
		appUserService.updateUser( user);
		return appUserService.getAllAppUsers();
	}
	
	//Employees
	
	@PreAuthorize("hasAuthority('SCOPE_ADM4')")
	@GetMapping("/employees/employee")
	public Employee getEmployee(@RequestParam(name="eid") String employeeID){
		return employeeService.getEmployee(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADM4')")
	@GetMapping("/employees")
	public EmployeeDTO getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/employees/new")
	public Employee getNewEmployee(){
		return new Employee();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PostMapping("/employees/save")
	public EmployeeDTO saveEmployee(@RequestBody Employee employee){
		employeeService.addNewEmployee(employee);
		return employeeService.getAllEmployees();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@DeleteMapping("/employees/delete")
	public EmployeeDTO deleteEmployee(@RequestParam(name="eid") String employeeID){
		employeeService.deleteEmployee(employeeID);
		return employeeService.getAllEmployees();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PutMapping("/employees/update")
	public EmployeeDTO updateEmployee(@RequestBody Employee employeeRequestDTO){
		employeeService.updateEmployee(employeeRequestDTO);
		return employeeService.getAllEmployees();
	}	
	//Message
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/notifications")
	public List<Notification>getAllNotifications(@RequestParam(name="eid")String employeeID){
		return notificationService.getByEmployeeID(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/notifications/supnotifs")
	public List<Notification>getSupervisorNotifications(@RequestParam(name="per")String period, @RequestParam(name="eid")String receiverID, @RequestParam(name="sid")String senderID){
		return notificationService.getSupNotifications(period, receiverID, senderID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/notifications/notifs")
	public NotificationResponseDTO getNotifications(@RequestParam(name="eid")String receiverID, @RequestParam(name="p", defaultValue = "0")int page ){
		return notificationService.getNotifications(receiverID, page);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@DeleteMapping("/notifications/delete")
	public List<Notification>deleteNotification(@RequestParam(name="nid")Long id, @RequestParam(name="eid")String employeeID){
		notificationService.deleteMessage(id);		
		return notificationService.getByEmployeeID(employeeID);	
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/notifications/notification")
	public Notification getNotification(@RequestParam(name="nid")Long notifID){
		return notificationService.getNotification(notifID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/notifications/notread")
	public int getNotRead(@RequestParam(name="eid")String employeeID){
		return notificationService.getNotRead(employeeID);
	}
	
//	vacations
	
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/vacations")
	public VacationDTO getAllVacations(@RequestParam(name="eid")String employeeID){
		return vacationService.getAllVacations(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/vacations/new")
	public Vacation getNewVacation(){
		return new Vacation();
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@DeleteMapping("/vacations/delete/{id}")
	public VacationDTO deleteVacation(@PathVariable Long id, @RequestParam(name="eid")String employeeID){
		vacationService.deleteVacation(id);
		return vacationService.getAllVacations(employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@GetMapping("/vacations/{id}")
	public Vacation getVacation(@PathVariable Long id){
		return vacationService.getVacation(id);
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PostMapping("/vacations/save")
	public VacationDTO addNewVacation(@RequestBody Vacation vacation){
		vacationService.saveVacation(vacation);
		return getAllVacations(vacation.getEmployeeID());
	}
	@PreAuthorize("hasAuthority('SCOPE_HR')")
	@PutMapping("/vacations/update")
	public VacationDTO updateVacation(@RequestBody Vacation vacation){
		vacationService.updateVacation(vacation);
		return getAllVacations(vacation.getEmployeeID());
	}
	
//	Projects
	
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/projects")
	public List<USAIDProject> getAllProjects(){
		return projectService.getAllProjects();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/projects/new")
	public USAIDProject getNewProject(){
		return new USAIDProject();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/projects/{id}")
	public USAIDProject getProject(@PathVariable Long id){
		return projectService.getProject(id);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@DeleteMapping("/projects/delete/{id}")
	public List<USAIDProject> deleteProject(@PathVariable Long id){
		projectService.deleteProject(id);
		return projectService.getAllProjects();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@PostMapping("/projects/save")
	public List<USAIDProject> addNewProject(@RequestBody USAIDProject project){
		projectService.addNewProject(project);
		return projectService.getAllProjects();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@PutMapping("/projects/update")
	public List<USAIDProject> updateProject(@RequestBody USAIDProject project){
		projectService.updateProject(project);
		return projectService.getAllProjects();
	}
	//Connected users
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/connectedusers")
	public List<ConnectedUser>getAllConnectedUsers(){
		return ConnectedUserModel.getConnectedUsers();
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/disconnect")
	public List<ConnectedUser> disconnectUser(@RequestParam(value = "acct")String accessToken){
		return ConnectedUserModel.disconnectUser(accessToken);
	}
	@PreAuthorize("hasAuthority('SCOPE_ADMIN')")
	@GetMapping("/removedisconnectedusers")
	public List<ConnectedUser> removeDisconnectedUsers(){
		return ConnectedUserModel.removeDeconnectedUsers();
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/logout")
	public void logOut(@RequestParam(value = "acct")String accessToken){
		ConnectedUserModel.logout(accessToken);
	}
	
}
