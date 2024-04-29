package com.timesheet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.dtos.VacationDTO;
import com.timesheet.entities.Vacation;
import com.timesheet.models.VacationReport;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.VacationRepository;

@Service
@Transactional
public class VacationService {

	@Autowired
	private VacationRepository vacationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;	
	@Autowired
	private TimesheetService timesheetService;
	
	public VacationDTO getAllVacations(String employeeID) {
		VacationReport vr=timesheetService.getAllVacationDays(employeeID);
		VacationDTO vdto=new VacationDTO(vacationRepository.findByEmployeeID(employeeID), vr, employeeRepository.findById(employeeID).get());
		vdto.setEmployee(employeeRepository.findById(employeeID).get());
		return vdto;
	}
	public Vacation getVacation(Long id ) {
		return vacationRepository.findById(id).get();
	}
	public boolean saveVacation(Vacation vacation) {
		if(vacation.getDaysTaken()>0)vacationRepository.save(vacation);
		return false;
	}
	public boolean deleteVacation(Long id) {
		vacationRepository.deleteById(id);
		return true;
	}
	public boolean updateVacation(Vacation vacation) {
		Vacation va=vacationRepository.findById(vacation.getId()).get();
		va.setEmployeeID(vacation.getEmployeeID());
		va.setDaysTaken(vacation.getDaysTaken());
		va.setStartDate(vacation.getStartDate());
		vacationRepository.save(va);
		return true; 
	}
}
