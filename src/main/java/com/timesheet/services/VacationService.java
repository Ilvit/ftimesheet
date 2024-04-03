package com.timesheet.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.entities.Vacation;
import com.timesheet.repositories.VacationRepository;

@Service
@Transactional
public class VacationService {

	@Autowired
	private VacationRepository vacationRepository;
	
	public Vacation takeNewVacation() {
		return new Vacation();
	}
	public Vacation getVacation(Long id ) {
		return vacationRepository.findById(id).get();
	}
	public boolean saveVacation(Vacation vacation) {
		vacationRepository.save(vacation);
		return false;
	}
	public boolean updateVacation(Vacation vacation) {
		Vacation va=vacationRepository.findById(vacation.getId()).get();
		va.setEmployeeID(vacation.getEmployeeID());
		va.setDaysTaken(vacation.getDaysTaken());
		va.setLocalDate(vacation.getLocalDate());
		vacationRepository.save(va);
		return true;
	}
}
