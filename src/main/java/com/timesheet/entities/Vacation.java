package com.timesheet.entities;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class Vacation {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String employeeID;
	private int daysToken;
	private LocalDate localDate;
	private transient int daysRemaining;
	
	
}
