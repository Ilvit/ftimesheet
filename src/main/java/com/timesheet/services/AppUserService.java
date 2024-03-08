package com.timesheet.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.entities.AppUser;
import com.timesheet.repositories.AppRoleRepository;
import com.timesheet.repositories.AppUserRepository;
@Service
@Transactional
public class AppUserService implements AppUserserviceInterface {

	@Autowired
	private AppRoleRepository appRoleRepository;
	@Autowired
	private AppUserRepository appUserRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@Override
	public void addRoleToUser(String username, String roleName) {
		AppUser user=appUserRepository.findByUsername(username);
		user.getRoles().add(appRoleRepository.findByRoleName(roleName));
		appUserRepository.save(user);
	}

	@Override
	public void addRolesToUser(String username, ArrayList<String> rolesNames) {
		AppUser appUser=appUserRepository.findByUsername(username);
		rolesNames.forEach(rn->{
			appUser.getRoles().add(appRoleRepository.findByRoleName(rn));
		});
		appUserRepository.save(appUser);
	}

	@Override
	public void removeRole(String roleName, Long userId) {
		AppUser appUser=appUserRepository.findById(userId).get();
		appUser.getRoles().remove(appRoleRepository.findByRoleName(roleName));
		appUserRepository.save(appUser);
	}

	@Override
	public void addUser(AppUser user) {
		AppUser appUser=AppUser.builder()
				.username(user.getUsername())
				.password(passwordEncoder.encode(user.getPassword()))
				.mail(user.getMail())
				.employeeID(user.getEmployeeID())
				.build();
		appUserRepository.save(appUser);
	}

	@Override
	public void updateUser(Long appUserId, AppUser user) {
		AppUser appUser=appUserRepository.findById(appUserId).get();
		appUser.setUsername(user.getUsername());
		appUser.setEmployeeID(user.getEmployeeID());
		appUser.setMail(user.getMail());
		if(!user.getPassword().isEmpty() && user.getPassword().length()>3)appUser.setPassword(passwordEncoder.encode(user.getPassword()));
		appUser.getRoles().clear();
		user.getUserRoles().forEach(ur->{
			if(ur.isHasRole()) {
				appUser.getRoles().add(appRoleRepository.findByRoleName(ur.getRoleName().toString()));
			}			
		});
		appUserRepository.save(appUser);
	}

	@Override
	public void deleteAppUser(Long appUserId) {
		appUserRepository.deleteById(appUserId);
	}

	@Override
	public AppUser getUser(String username) {
		return appUserRepository.findByUsername(username);
	}

	@Override
	public AppUser getUser(Long userId) {
		AppUser au=appUserRepository.findById(userId).get();
		au.loadUserRoles();
		return au;
	}

	@Override
	public List<AppUser> getAllAppUsers() {
		List<AppUser> appUsers=appUserRepository.findAll();
		appUsers.forEach(au->{
			au.loadUserRoles();
		});
		return appUsers;
	}
	public String getEmployeeID(String username) {
		return appUserRepository.findByUsername(username).getEmployeeID();
	}

}
