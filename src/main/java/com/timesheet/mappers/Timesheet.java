package com.timesheet.mappers;

import java.util.ArrayList;
import java.util.List;

import com.timesheet.constants.PeriodVars;
import com.timesheet.dtos.DayHours;
import com.timesheet.entities.Sheetday;

import lombok.Data;

@Data
public class Timesheet {
	private String employeeID;
	private String period;
	private List<Sheetday>businessDaysLine;
	private List<Sheetday>holidaysLine;
	private List<Sheetday>weekendDaysLine;
	private List<DayHours>verticalTotalHours;	
	private boolean signed;
	private boolean approved;
	private int businessTotalHours;
	private int holidayTotalHours;
	private int weekendTotalHours;
	private int totalHours;
	
	
	public Timesheet() {
		this.businessDaysLine=new ArrayList<>();
		this.holidaysLine=new ArrayList<>();
		this.weekendDaysLine=new ArrayList<>();
		this.verticalTotalHours=new ArrayList<>();
	}
	
	public Timesheet(String period, String employeeID, List<Sheetday>businessDaysLine,
			List<Sheetday>holidaysLine, List<Sheetday>weekendDaysLine) {
		this.employeeID=employeeID;
		this.period=period;
		this.businessDaysLine=businessDaysLine;
		this.holidaysLine=holidaysLine;
		this.weekendDaysLine=weekendDaysLine;
	}
	
	public int getBusinessTotalHours() {
		businessTotalHours = 0;
		if(!this.businessDaysLine.isEmpty()) {
			for(Sheetday sd: this.businessDaysLine) {
				businessTotalHours+=sd.getHours();
			}
		}
		return this.businessTotalHours;
	}
	public int getHolidaysTotalHours() {
		holidayTotalHours = 0;
		if(!this.holidaysLine.isEmpty()) {
			for(Sheetday sd: this.holidaysLine) {
				this.holidayTotalHours+=sd.getHours();
			}
		}
		return this.holidayTotalHours;
	}
	
	public int getWeekendTotalHours() {
		weekendTotalHours = 0;
		if(!this.weekendDaysLine.isEmpty()) {
			for(Sheetday sd: this.weekendDaysLine) {
				this.weekendTotalHours+=sd.getHours();
			}
		}
		return this.weekendTotalHours;
	}
	
	public int getTotalHours() {
		totalHours = 0;
		verticalTotalHours.forEach(vth->{
			totalHours+=vth.getTotalHours();
		});
		return totalHours;
	}
	
	public void loadVerticalTotalHours() {
		verticalTotalHours=new ArrayList<>();
		PeriodVars pv=new PeriodVars(period);
		for(int i=0;i<pv.getPeriodLength();i++) {
			DayHours dh=new DayHours();
			int hours=0;
			if(!businessDaysLine.isEmpty())hours+=businessDaysLine.get(i).getHours();
			if(!holidaysLine.isEmpty())hours+=holidaysLine.get(i).getHours();
			if(!weekendDaysLine.isEmpty())hours+=weekendDaysLine.get(i).getHours();

			dh.setTotalHours(hours);
			verticalTotalHours.add(dh);
		}
	}
}
