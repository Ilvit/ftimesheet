package com.timesheet.services;

import java.util.List;

import com.timesheet.entities.AppUser;

public interface AppUserserviceInterface {
	
	public void addRoleToUser(String username, String roleName);
	public void addRolesToUser(String username, List<String> rolesNames);
	public void removeRoleToUser(String username, String roleName);
	public void saveUser(AppUser user);
	public void updateUser(String username, AppUser user);
	public void deleteUser(String username);
	public AppUser getUser(String username);
	public AppUser getUser(Long userId);
	public List<AppUser> getAllAppUsers();	
}
