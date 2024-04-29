package com.timesheet.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class PeriodState {
	private String period;
	private boolean finished;
	private boolean approved;
	private boolean timesheetOk;
}
