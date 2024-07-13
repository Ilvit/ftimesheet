package com.timesheet.services;

import java.util.List;

import com.timesheet.dtos.UsersDTO;
import com.timesheet.entities.AppUser;

public interface AppUserserviceInterface {
	
	public void addRoleToUser(String username, String roleName);
	public void addRolesToUser(String username, List<String> rolesNames);
	public void removeRoleToUser(String username, String roleName);
	public void saveUser(AppUser user);
	public void updateUser(AppUser user);
	public void deleteUser(String username);
	public AppUser findByEmployeeID(String employeeID);
	public AppUser getUser(String username);
	public AppUser getUser(Long userId);
	public UsersDTO getAllAppUsers();	
}
