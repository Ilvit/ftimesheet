package com.timesheet.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.UserSavedSheetLines;

public interface UserSavedSheetLineRepository extends JpaRepository<UserSavedSheetLines, Long> {
	public List<UserSavedSheetLines> findByEmployeeID(String employeeID);
	public UserSavedSheetLines findByEmployeeIDAndPeriod(String employeeID, String period);
	public void deleteByEmployeeIDAndPeriod(String employeeID, String period);
	//	@Query("delete us from UserSavedSheetLines us where us.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.dayType=:dayType ")

}
