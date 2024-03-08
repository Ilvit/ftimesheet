package com.timesheet.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.timesheet.enums.DayType;

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
	private boolean weekend;
	private boolean signed;
	private boolean approuved;
	
}
