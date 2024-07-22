package com.timesheet.dtos;

import java.util.ArrayList;
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
	private Employee daf;
	private boolean canAddNewTimesheet;
	private boolean timesheetExists;
	private boolean timesheetPeriodExists;
	private List<String>usersPeriods;
	private VacationReport vacationReport;
	
	
	public TimesheetState(String period, Employee employee, List<TimesheetSaver> tsaverList) {
		
		this.employee=employee;
		this.currentPeriod=TimesheetPeriods.currentPeriod;
		
		if(!tsaverList.isEmpty()) {	//if there is periods	
			usersPeriods=new ArrayList<>();
			periodStates=new ArrayList<>();
			timesheetExists=true;
			tsaverList.forEach(tsaver->{
				PeriodState ps=new PeriodState();
				if(TimesheetPeriods.isPeriodFinished(tsaver.getPeriod()))ps.setFinished(true);
				if(tsaver.isApproved())ps.setApproved(true);
				if(tsaver.isApprovedByDAF())ps.setTimesheetOk(true);
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
		TimesheetPeriods tp=new TimesheetPeriods();
		if(!usersPeriods.contains(TimesheetPeriods.currentPeriod)) {
			String previousPeriod=tp.getPrecedentPeriod(TimesheetPeriods.currentPeriod);
			if(usersPeriods.contains(previousPeriod)) {
				for(PeriodState ps:periodStates) {
					if(ps.getPeriod().equals(previousPeriod)) {
						if(ps.isApproved())canAddNewTimesheet=true;
					}
				}
			}
		}
		
	}
}
