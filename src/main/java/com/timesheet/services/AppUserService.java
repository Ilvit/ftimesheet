package com.timesheet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.dtos.UsersDTO;
import com.timesheet.entities.AppUser;
import com.timesheet.entities.Employee;
import com.timesheet.repositories.AppUserRepository;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.RoleRepository;
@Service
@Transactional
public class AppUserService implements AppUserserviceInterface {

	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser user=appUserRepository.findByUsername(username);
		user.getRoles().add(roleRepository.findByName(roleName));
		appUserRepository.save(user);
	}

	@Override
	public void addRolesToUser(String username, List<String> rolesNames) {
		AppUser appUser=appUserRepository.findByUsername(username);
		rolesNames.forEach(rn->{
			appUser.getRoles().add(roleRepository.findByName(rn));
		});
		appUserRepository.save(appUser);
	}

	@Override
	public void removeRoleToUser(String username, String roleName) {
		AppUser appUser=appUserRepository.findByUsername(username);
		appUser.getRoles().remove(roleRepository.findByName(roleName));
		appUserRepository.save(appUser);
	}

	@Override
	public void saveUser(AppUser user) {
		Employee employee=employeeRepository.findByEmployeeID(user.getEmployeeID());
		AppUser appUser=AppUser.builder()
				.username(user.getUsername())
				.password(passwordEncoder.encode(user.getPassword()))
				.mail(employee.getMail())
				.employeeID(employee.getEmployeeID())
				.supervisorID(employee.getSupervisorID())
				.build();
		appUserRepository.save(appUser);
	}

	@Override
	public void updateUser(AppUser user) {
		AppUser appUser=appUserRepository.findByEmployeeID(user.getEmployeeID());
		Employee employee=employeeRepository.findByEmployeeID(appUser.getEmployeeID());
		appUser.setUsername(user.getUsername());
		appUser.setEmployeeID(employee.getEmployeeID());
		appUser.setSupervisorID(employee.getSupervisorID());
		appUser.setMail(employee.getMail());
		if(!user.getPassword().isEmpty() && user.getPassword().length()>3)appUser.setPassword(passwordEncoder.encode(user.getPassword()));
		appUser.getRoles().clear();
		user.getUserRoles().forEach(ur->{
			if(ur.isHasRole()) {
				appUser.getRoles().add(roleRepository.findByName(ur.getRoleName()));
			}			
		});
		appUserRepository.save(appUser);
	}

	@Override
	public void deleteUser(String username) {
		appUserRepository.deleteByUsername(username);
	}

	@Override
	public AppUser getUser(String username) {
		AppUser au=appUserRepository.findByUsername(username);
		au.loadUserRoles();
		return au;
	}

	@Override
	public AppUser getUser(Long userId) {
		AppUser au=appUserRepository.findById(userId).get();
		au.loadUserRoles();
		return au;
	}

	@Override
	public UsersDTO getAllAppUsers() {		
		List<AppUser> appUsers=appUserRepository.findAll();		
		appUsers.forEach(au->{
			au.loadUserRoles();
		});
		UsersDTO udto=new UsersDTO(appUsers, employeeRepository.findAll());
		return udto;
	}
	public String getEmployeeID(String username) {
		return appUserRepository.findByUsername(username).getEmployeeID();
	}

	@Override
	public AppUser findByEmployeeID(String employeeID) {
		return appUserRepository.findByEmployeeID(employeeID);
	}

}
