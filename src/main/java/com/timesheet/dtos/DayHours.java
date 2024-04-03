package com.timesheet.dtos;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class DayHours {
	private LocalDate date;
	private int totalHours;
}
