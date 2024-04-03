package com.timesheet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.timesheet.entities.AppUser;
import com.timesheet.services.AppUserService;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private AppUserService appUserService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser=appUserService.getUser(username);
		if(appUser==null)throw new UsernameNotFoundException(String.format("The user %s is not found !", username));
	
		UserDetails userDetails=User
				.withUsername(appUser.getUsername())
				.password(appUser.getPassword())				
				.authorities(appUser.getRoles().stream().map(role->role.getName()).toArray(String[]::new))
				.build();	
		return userDetails;
	}

}
