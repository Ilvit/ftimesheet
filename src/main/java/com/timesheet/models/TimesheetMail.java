package com.timesheet.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
public class TimesheetMail {
	private String subject;
	private String message;
}
