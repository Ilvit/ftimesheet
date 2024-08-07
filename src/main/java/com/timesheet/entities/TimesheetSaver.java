package com.timesheet.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class TimesheetSaver {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String employeeID;
	private String period;	
	private boolean signed;
	private boolean approved;
	private boolean rejected;
	private boolean hasNewCreated;
	private boolean paid;
	
	
}
