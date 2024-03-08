package com.timesheet.services;

import java.util.ArrayList;
import java.util.List;

import com.timesheet.entities.AppUser;

public interface AppUserserviceInterface {
	
	public void addRoleToUser(String username, String roleName);
	public void addRolesToUser(String username, ArrayList<String> rolesNames);
	public void removeRole(String roleName, Long userId);
	public void addUser(AppUser user);
	public void updateUser(Long appUserId, AppUser user);
	public void deleteAppUser(Long appUserId);
	public AppUser getUser(String username);
	public AppUser getUser(Long userId);
	public List<AppUser> getAllAppUsers();	
}
