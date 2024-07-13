package com.timesheet.dtos;

import java.util.List;

import com.timesheet.entities.AppUser;
import com.timesheet.entities.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class UsersDTO {

	private List<AppUser>users;
	private List<Employee>employees;
}
