package com.timesheet.entities;

import java.time.LocalDate;

import com.timesheet.enums.DayType;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @Builder
public class Sheetday {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String employeeID;
	private LocalDate date;
	private int hours;
	private DayType dayType ;
	private String projectName;
	private boolean weekend;
	private boolean holiday;
	private boolean signed;
	private boolean rejected;
	private boolean approved;
	
}
