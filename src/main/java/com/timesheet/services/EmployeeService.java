package com.timesheet.services;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.dtos.EmployeeDTO;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Employee;
import com.timesheet.enums.Positions;
import com.timesheet.repositories.AppUserRepository;
import com.timesheet.repositories.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private AppUserRepository appUserRepository;
		
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
	public boolean addNewEmployee(String name, String postName, String nickName, String mail, Positions position) {
		Employee employee=new Employee();
		employee.setEmployeeID(UUID.randomUUID().toString().substring(0, 9).toUpperCase());
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
		employee.setEmployeeID(UUID.randomUUID().toString().substring(0, 9).toUpperCase());
		employee.setName(employeeRequestDTO.getName());
		employee.setPostName(employeeRequestDTO.getPostName());
		employee.setNickName(employeeRequestDTO.getNickName());
		employee.setGender(employeeRequestDTO.getGender());
		employee.setMail(employeeRequestDTO.getMail());
		employee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
		
		employeeRepository.save(employee);
		return true;
	}
	public boolean updateEmployee(Employee employeeRequestDTO) {
		Employee employee=employeeRepository.findById(employeeRequestDTO.getId()).get();
		AppUser au=appUserRepository.findByEmployeeID(employee.getEmployeeID());
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
		employeeRepository.save(employee);
		return true;
	}
	public boolean setSupervisorToEmployee(String supervisorName, String agentName) {
		Employee supervisor=employeeRepository.findByName(supervisorName);
		Employee agent=employeeRepository.findByName(agentName);
		agent.setSupervisorID(supervisor.getEmployeeID());
		employeeRepository.save(agent);
		return true;		
	}
}
