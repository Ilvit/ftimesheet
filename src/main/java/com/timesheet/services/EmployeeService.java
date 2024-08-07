package com.timesheet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.dtos.EmployeeDTO;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Employee;
import com.timesheet.enums.Positions;
import com.timesheet.repositories.AppUserRepository;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.SheetdayRepository;
import com.timesheet.repositories.TimesheetSaverRepository;
import com.timesheet.repositories.VacationRepository;


@Service
@Transactional
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private SheetdayRepository sheetdayRepository;
	@Autowired 
	TimesheetSaverRepository timesheetSaverRepository;
	@Autowired
	private VacationRepository vacationRepository;
	
	public Employee getEmployee(String employeeID) {
		return employeeRepository.findByEmployeeID(employeeID);
	}
	public EmployeeDTO getAllEmployees(){
		return new EmployeeDTO(employeeRepository.findAll(), Positions.values());		
	}
	public String getEmployeeID(String employeeName) {		
		return employeeRepository.findByName(employeeName).getEmployeeID();
	}
	
	public boolean deleteEmployee(String employeeID) {
		employeeRepository.deleteByEmployeeID(employeeID);
		return true;
	}
	public boolean saveEmployee(Employee employee) {
		employeeRepository.save(employee);
		return true;
	}
	
	public boolean addNewEmployee(String name, String postName, String nickName, String employeeID, String mail, Positions position) {
		Employee employee=new Employee();
		employee.setEmployeeID(employeeID);
		employee.setName(name);
		employee.setPostName(postName);
		employee.setNickName(nickName);
		employee.setPosition(position);
		employee.setMail(mail);
		
		employeeRepository.save(employee);
		
		return true;
	}
	public boolean addNewEmployee(Employee employeeRequestDTO) {
		Employee employee=new Employee();
		employee.setEmployeeID(employeeRequestDTO.getEmployeeID());
		employee.setName(employeeRequestDTO.getName());
		employee.setPostName(employeeRequestDTO.getPostName());
		employee.setNickName(employeeRequestDTO.getNickName());
		employee.setGender(employeeRequestDTO.getGender());
		employee.setPosition(employeeRequestDTO.getPosition());
		employee.setMail(employeeRequestDTO.getMail());
		employee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
		employee.setSupervisorID(employeeRequestDTO.getSupervisorID());
		
		employeeRepository.save(employee);
		return true;
	}
	public boolean updateEmployee(Employee employeeRequestDTO) {
		Employee employee=employeeRepository.findById(employeeRequestDTO.getId()).get();
		AppUser au=appUserRepository.findByEmployeeID(employee.getEmployeeID());
		
		sheetdayRepository.findByEmployeeID(employee.getEmployeeID()).forEach(sd->{
			sd.setEmployeeID(employeeRequestDTO.getEmployeeID());
			sheetdayRepository.save(sd);
		});
		timesheetSaverRepository.findByEmployeeID(employee.getEmployeeID()).forEach(ts->{
			ts.setEmployeeID(employeeRequestDTO.getEmployeeID());
			timesheetSaverRepository.save(ts);
		});
		vacationRepository.findByEmployeeID(employee.getEmployeeID()).forEach(v->{
			v.setEmployeeID(employeeRequestDTO.getEmployeeID());
			vacationRepository.save(v);
		});
		timesheetSaverRepository.findByEmployeeID(employee.getEmployeeID()).forEach(ts->{
			ts.setEmployeeID(employeeRequestDTO.getEmployeeID());
			timesheetSaverRepository.save(ts);
		});
		new Thread(()->{			
			au.setSupervisorID(employee.getSupervisorID());
			au.setMail(employeeRequestDTO.getMail());
			au.setEmployeeID(employeeRequestDTO.getEmployeeID());
			appUserRepository.save(au);
		}).start();
		employee.setEmployeeID(employeeRequestDTO.getEmployeeID());
		employee.setName(employeeRequestDTO.getName());
		employee.setPostName(employeeRequestDTO.getPostName());
		employee.setNickName(employeeRequestDTO.getNickName());
		employee.setGender(employeeRequestDTO.getGender());
		employee.setMail(employeeRequestDTO.getMail());
		employee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
		employee.setPosition(employeeRequestDTO.getPosition());
		employee.setSupervisorID(employeeRequestDTO.getSupervisorID());
		employee.setOtherWorkedHours(employeeRequestDTO.getOtherWorkedHours());
		employeeRepository.save(employee);
		return true;
	}
	
	public boolean setSupervisor(String employeeID, String supervisorEmployeeID) {		
		Employee agent=employeeRepository.findByEmployeeID(employeeID);
		Employee supervisor=employeeRepository.findByEmployeeID(supervisorEmployeeID);
		agent.setSupervisorID(supervisor.getEmployeeID());
		employeeRepository.save(agent);
		return true;		
	}
}
