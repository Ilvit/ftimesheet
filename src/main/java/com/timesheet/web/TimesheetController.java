package com.timesheet.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.timesheet.constants.CodeType;
import com.timesheet.constants.TimesheetPeriods;
import com.timesheet.dtos.TimesheetDTO;
import com.timesheet.services.TimesheetService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class TimesheetController {
	@Autowired
	private TimesheetService timesheetService;
	private TimesheetPeriods tp=new TimesheetPeriods();
	
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/timesheet")
	public TimesheetDTO getTimeSheetLine(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		return timesheetService.getTimesheet(period, employeeID);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@GetMapping("/timesheet/newline")
	public TimesheetDTO getNewTimesheetLine(@RequestParam(name="per",defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.businessDay) String daysCode){
		
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		System.out.println("new line resquest: "+period);
		return timesheetService.getNewTimesheetLine(period, employeeID, daysCode);		
	}
	@PutMapping("/timesheet")
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	public boolean updateTimesheet(@RequestBody TimesheetDTO timesheetDTO) {
		return timesheetService.updateTimesheet(timesheetDTO);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@PostMapping("/timesheet/save")
	public TimesheetDTO saveTimesheet(@RequestParam(name="per", defaultValue = "")String period, @RequestBody TimesheetDTO timesheetDTO) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		timesheetDTO.setTimesheetPeriod(period);
		return timesheetService.saveTimesheet(timesheetDTO);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@DeleteMapping("/timesheet/deleteline")
	public boolean deleteTimesheetLine(@RequestParam(name="per", defaultValue = "") String period, @RequestParam(name = "eid") String employeeID, 
			@RequestParam(name = "dc", defaultValue = CodeType.businessDay) String daysCode) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		return timesheetService.deleteTimesheetLine(period, employeeID, daysCode);
	}
	@PreAuthorize("hasAuthority('SCOPE_USER')")
	@DeleteMapping("/timesheet/deletetimesheet")
	public boolean deleteTimesheet(@RequestParam(name="per", defaultValue = "") String period, @RequestParam(name = "eid") String employeeID) {
		try {
			LocalDate.parse(period, TimesheetPeriods.dtf);
		} catch (Exception e) {
			period=tp.getCurrentPeriod();
		}
		return timesheetService.deleteTimesheet(period, employeeID);
	}
	
}
