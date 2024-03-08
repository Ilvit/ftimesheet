package com.timesheet.services;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.entities.Employee;
import com.timesheet.enums.Positions;
import com.timesheet.repositories.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
	@Autowired
	private EmployeeRepository employeeRepository;
		
	public Employee getEmployee(String employeeID) {
		return employeeRepository.findById(employeeID).get();
	}
	public String getEmployeeID(String employeeName) {		
		return employeeRepository.findByName(employeeName).getId();
	}
	
	public boolean deleteEmployee(String employeeID) {
		employeeRepository.deleteById(employeeID);
		return true;
	}
	public boolean saveEmployee(Employee employee) {
		employeeRepository.save(employee);
		return true;
	}
	public boolean addNewEmployee(String name, String postName, String nickName, String mail, Positions position) {
		Employee employee=new Employee();
		employee.setId(UUID.randomUUID().toString().substring(0, 10).toUpperCase());
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
		employee.setId(UUID.randomUUID().toString());
		employee.setName(employeeRequestDTO.getName());
		employee.setPostName(employeeRequestDTO.getPostName());
		employee.setNickName(employeeRequestDTO.getNickName());
		employee.setGender(employeeRequestDTO.getGender());
		employee.setMail(employeeRequestDTO.getMail());
		employee.setPhoneNumber(employeeRequestDTO.getPhoneNumber());
		
		employeeRepository.save(employee);
		return true;
	}
	public boolean setSupervisorToEmployee(String supervisorName, String agentName) {
		Employee supervisor=employeeRepository.findByName(supervisorName);
		Employee agent=employeeRepository.findByName(agentName);
		agent.setSupervisorID(supervisor.getId());
		employeeRepository.save(agent);
		return true;		
	}
}
