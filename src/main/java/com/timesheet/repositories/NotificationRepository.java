package com.timesheet.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.timesheet.entities.Employee;
import com.timesheet.entities.Notification;


public interface NotificationRepository extends JpaRepository<Notification, Long> {
	public List<Notification>findByMsgTo(String employeeID);
	public List<Notification>findByMsgToAndTsread(String employeeID, boolean read);
	public Page<Notification>findByMsgTo(String employeeID, Pageable pageable);
//	@Query("select no from Notification no where no.period=:per and no.msgTo=:eid and no.sender.id=:sid")
	public List<Notification>findByPeriodAndSenderAndMsgTo(String period, Employee sender, String receiverID);
}
