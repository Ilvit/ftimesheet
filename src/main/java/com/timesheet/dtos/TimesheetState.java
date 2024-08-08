package com.timesheet.dtos;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.entities.Employee;
import com.timesheet.entities.TimesheetSaver;
import com.timesheet.models.PeriodState;
import com.timesheet.models.VacationReport;

import lombok.Data;

@Data
public class TimesheetState {
	
	private List<PeriodState>periodStates;
	private String currentPeriod;
	private Employee employee;
	private Employee supervisor;
	private Employee cop;
	private boolean canAddNewTimesheet;
	private boolean timesheetExists;
	private boolean timesheetPeriodExists;
	private boolean timesheetLineExists;
	private List<String>usersPeriods;
	private VacationReport vacationReport;
	
	
	public TimesheetState(String period, Employee employee, List<TimesheetSaver> tsaverList) {
		TimesheetPeriods tp=new TimesheetPeriods();
		this.employee=employee;
		this.currentPeriod=tp.getCurrentPeriod();
				
		if(!tsaverList.isEmpty()) {	//if there is periods	
			tsaverList.forEach(tsaver->{
				if(tsaver.getPeriod().equals(period))timesheetLineExists=true;
			});
			usersPeriods=new ArrayList<>();
			periodStates=new ArrayList<>();
			timesheetExists=true;
			tsaverList.forEach(tsaver->{
				PeriodState ps=new PeriodState();
				if(TimesheetPeriods.isPeriodFinished(tsaver.getPeriod()))ps.setFinished(true);
				if(tsaver.isApproved()) {
					ps.setApproved(true);
					ps.setTimesheetOk(true);
				}
				ps.setPeriod(tsaver.getPeriod());
				periodStates.add(ps);
				usersPeriods.add(tsaver.getPeriod());
				if(usersPeriods.contains(period))timesheetPeriodExists=true;
			});		
			//can add new timesheet                             if contains the finished current period
			handleCanAddNewTimesheet(usersPeriods, periodStates);
		}else {
			timesheetExists=false;
		}
		if(!timesheetExists)canAddNewTimesheet=true;		
		
	}
	
	private void handleCanAddNewTimesheet(List<String> usersPeriods, List<PeriodState>periodStates) {
		if(!usersPeriods.isEmpty()) {
			String usersLastPeriod=getUsersLastPeriod(usersPeriods);
			for(PeriodState ps:periodStates) {
				if(ps.getPeriod().equals(usersLastPeriod)) {
					if(ps.isApproved())canAddNewTimesheet=true;
				}
			}
		}		
	}
	
	private String getUsersLastPeriod(List<String> usersPeriods) {
		ArrayList<LocalDate>al=new ArrayList<>();
		usersPeriods.forEach(uper->{
			al.add(LocalDate.parse(uper, TimesheetPeriods.dtf));
		});
		return Collections.max(al).format(TimesheetPeriods.dtf);
	}
}
