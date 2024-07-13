package com.timesheet.dtos;

import java.util.List;

import com.timesheet.entities.Employee;
import com.timesheet.enums.Positions;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class EmployeeDTO {
	
	private List<Employee>employees;
	private Positions[] positions;
	
	
}
