package com.timesheet.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.entities.Holiday;
import com.timesheet.repositories.HolidayRepository;
import com.timesheet.repositories.SheetdayRepository;

@Service
@Transactional
public class HolidayService implements HolidayInterface {

	@Autowired
	private HolidayRepository holidayRepository;
	@Autowired
	private SheetdayRepository sheetdayRepository;
	
	@Override
	public boolean saveHoliday(Holiday holiday) {
		holidayRepository.save(holiday);
		new Thread(()->{
			sheetdayRepository.findByDate(holiday.getDate()).forEach(sd->{
				sd.setHoliday(true);
				sheetdayRepository.save(sd);
			});
		}).start();
		return true;
	}

	@Override
	public boolean removeHoliday(LocalDate ld) {
		holidayRepository.deleteByDate(ld);
		return true;
	}
	
	@Override
	public boolean addHoliday(LocalDate ld, String description) {
		return holidayRepository.save(new Holiday(null, ld, description)) != null;
	}

	@Override
	public boolean updateHoliday(Long id, LocalDate newDate, String description) {
		Holiday hd=holidayRepository.findById(id).get();
		hd.setDate(newDate);
		if(!description.isEmpty())hd.setDescription(description);		
		return holidayRepository.save(hd)!=null;
	}

	@Override
	public boolean isHoliday(LocalDate ld) {
		if(holidayRepository.findByDate(ld)!=null)return true;
		return false;
	}

	@Override
	public List<Holiday> getHolidays() {
		return holidayRepository.findAll();
	}

	@Override
	public Holiday getHoliday(Long id) {
		return holidayRepository.findById(id).get();
	}



}
