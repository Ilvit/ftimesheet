package com.timesheet.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.timesheet.entities.Sheetday;
import com.timesheet.enums.DayType;

public interface SheetdayRepository extends JpaRepository<Sheetday, Long> {
	@Query("select sd from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.projectName=:proj")
	public List<Sheetday>findBetweenDates(@Param(value = "ld1") LocalDate ld1, @Param(value = "ld2")LocalDate ld2, @Param(value = "eid")String eid, @Param(value = "proj")String projectName);
	
	@Query("select sd from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.dayType=:dt and sd.projectName=:proj")
	public List<Sheetday>findBetweenDatesDayTypeAndProject(@Param(value = "ld1") LocalDate ld1, @Param(value = "ld2")LocalDate ld2, @Param(value = "eid")String eid, @Param(value="dt")DayType dayType, @Param(value = "proj")String projectName);
	
	@Query("select sd from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid")
	public List<Sheetday>findBetweenDates(@Param(value = "ld1") LocalDate ld1, @Param(value = "ld2")LocalDate ld2, @Param(value = "eid")String eid);
	
	@Query("select sd from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.dayType=:dayType ")
	public List<Sheetday>findBetweenDatesAndDayType(@Param(value = "ld1") LocalDate ld1, @Param(value = "ld2")LocalDate ld2, @Param(value = "eid")String eid, @Param(value="dayType")DayType dayType);
	
	@Query("delete from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.dayType=:dayType and sd.projectName=:proj ")
	public void deleteBetweenDatesAndProject(@Param(value = "ld1") LocalDate ld1, @Param(value = "ld2")LocalDate ld2, @Param(value = "eid")String eid, @Param(value="dayType")DayType dayType, @Param(value="proj")String projectName);
	
//	@Query("delete from Sheetday sd where sd.date>=:ld1 and sd.date<=:ld2 and sd.employeeID=:eid and sd.dayType=:dayType ")
	public void deleteByDateAndEmployeeIDAndDayTypeAndProjectName(LocalDate date, String employeeID, DayType dayType, String projectName);
	public void deleteByDateAndEmployeeID(LocalDate date, String employeeID);
	public void deleteByDateAndEmployeeIDAndDayType(LocalDate date, String employeeID, DayType dayType);
	public Sheetday findByDateAndEmployeeIDAndDayTypeAndProjectName(LocalDate date, String employeeID, DayType dayType, String projectName);
	public void deleteByDate(LocalDate ld);
	public List<Sheetday>findByDate(LocalDate ld);
	public List<Sheetday>findByEmployeeID(String employeeID);
}
