package com.timesheet.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.timesheet.dtos.DayHours;
import com.timesheet.entities.Sheetday;

import lombok.Data;

@Data
public class Timesheet {
	private String employeeID;
	private String period;
	private List<Sheetday>regularDaysLine;
	private List<Sheetday>holidaysLine;
	private List<Sheetday>vacationDaysLine;
	private List<DayHours>verticalTotalHoursList;	
	private Map<String, Integer>rdTotalHoursByLine;
	private Map<String, Integer>hdTotalHoursByLine;
	private Map<String, Integer>vdTotalHoursByLine;
	private boolean signed;
	private boolean signable;
	private boolean approved;
	private boolean approvedByDAF;
	private boolean approvableByDAF;
	private boolean approvable;
	private boolean ok;
	private boolean rejected;
	private boolean hasNewCreated;
	private int regularTotalHours;
	private int holidayTotalHours;
	private int vacationTotalHours;
	private int totalHours;
	
	
	public Timesheet() {
		this.regularDaysLine=new ArrayList<>();
		this.holidaysLine=new ArrayList<>();
		this.vacationDaysLine=new ArrayList<>();
		this.verticalTotalHoursList=new ArrayList<>();
		this.rdTotalHoursByLine=new HashMap<>();
		this.hdTotalHoursByLine=new HashMap<>();
		this.vdTotalHoursByLine=new HashMap<>();
	}
	
	public Timesheet(String period, String employeeID, List<Sheetday>rdLines,
			List<Sheetday>hdLines, List<Sheetday>vdLines) {
		this.employeeID=employeeID;
		this.period=period;
		this.regularDaysLine=rdLines;
		this.holidaysLine=hdLines;
		this.vacationDaysLine=vdLines;
		this.rdTotalHoursByLine=new HashMap<>();
		this.hdTotalHoursByLine=new HashMap<>();
		this.vdTotalHoursByLine=new HashMap<>();
	}
	
	public int getRegularTotalHours() {
		regularTotalHours = 0;
		
		  if(!this.regularDaysLine.isEmpty()) { for(Sheetday sd:
		  this.regularDaysLine) { regularTotalHours+=sd.getHours(); } }
		 
		return this.regularTotalHours;
	}
	public int getHolidaysTotalHours() {
		holidayTotalHours = 0;		
		  if(!this.holidaysLine.isEmpty()) { for(Sheetday sd: this.holidaysLine) {
		  this.holidayTotalHours+=sd.getHours(); } }
		 
		return this.holidayTotalHours;
	}
	public int getVacationTotalHours() {
		vacationTotalHours = 0;				
		  if(!this.vacationDaysLine.isEmpty()) { for(Sheetday sd: this.vacationDaysLine) {
		  this.vacationTotalHours+=sd.getHours(); } }
		return this.vacationTotalHours;
	}
		
	public int getTotalHours() {
		totalHours = 0;
		verticalTotalHoursList.forEach(vth->{
			totalHours+=vth.getTotalHours();
		});
		return totalHours;
	}
	public void loadHoursByLine(List<Sheetday>regularDaysLine, List<Sheetday>holidaysLine,
			List<Sheetday>vacationDaysLine, List<String>rdProjects, List<String>hdProjects, List<String>vdProjects) {
		this.rdTotalHoursByLine.clear();
		this.hdTotalHoursByLine.clear();
		this.vdTotalHoursByLine.clear();
		
		rdProjects.forEach(rdpName->{
			int lineHours=0;
			for(Sheetday rd:regularDaysLine) {
				if(rd.getProjectName().equals(rdpName)) {
					lineHours+=rd.getHours();
				}
			}
			rdTotalHoursByLine.put(rdpName, lineHours);
		});
		hdProjects.forEach(hdpName->{
			int lineHours=0;
			for(Sheetday hd:holidaysLine) {
				if(hd.getProjectName().equals(hdpName)) {
					lineHours+=hd.getHours();
				}
			}
			hdTotalHoursByLine.put(hdpName, lineHours);
		});
		vdProjects.forEach(vdpName->{
			int lineHours=0;
			for(Sheetday vd:vacationDaysLine) {
				if(vd.getProjectName().equals(vdpName)) {
					lineHours+=vd.getHours();
				}
			}
			vdTotalHoursByLine.put(vdpName, lineHours);
		});
		
	}
	public void loadVerticalTotalHours(List<Sheetday>rdList, List<Sheetday>hdList, List<Sheetday>vdList, List<LocalDate>periodDates) {
		
		List<Sheetday>unifiedList=new ArrayList<>();
		verticalTotalHoursList=new ArrayList<>();		
		unifiedList.addAll(hdList);
		unifiedList.addAll(rdList);
		unifiedList.addAll(vdList);
		
		periodDates.forEach(date->{
			int tHours=0;
			for(Sheetday sd:unifiedList) {
				if(sd.getDate().equals(date)) {
					tHours+=sd.getHours();
				}
			}
			verticalTotalHoursList.add(new DayHours(date,tHours));			
		});	
		
	}
}
