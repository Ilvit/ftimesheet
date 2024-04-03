package com.timesheet.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.timesheet.dtos.NotificationResponseDTO;
import com.timesheet.entities.Employee;
import com.timesheet.entities.Notification;
import com.timesheet.enums.Positions;
import com.timesheet.repositories.EmployeeRepository;
import com.timesheet.repositories.NotificationRepository;

@Service @Transactional
public class NotificationService {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private EmployeeRepository employeeRepository;
	private int pageSize=20;
	
	public List<Notification>getByEmployeeID(String employeeID){		
		return notificationRepository.findByMsgTo(employeeID);
	}
	public List<Notification>getSupNotifications(String period, String receiverID, String senderID){	
		Employee sender=employeeRepository.findById(senderID).get();
		return notificationRepository.findByPeriodAndSenderAndMsgTo(period, sender, receiverID);
	}
	public List<Notification>getByEmployeeIDAndRead(String employeeID, boolean read){		
		return notificationRepository.findByMsgToAndRead(employeeID, read);
	}
	public void deleteMessage(Long id) {
		notificationRepository.deleteById(id);
	}
	public Notification getNotification(Long nid) {
		Notification notif=notificationRepository.findById(nid).get();
		new Thread(()-> {
			notif.setRead(true);
		}).start();
		return notif;
	}
	public NotificationResponseDTO getNotifications(String employeeID, int page){
		Employee receiver=employeeRepository.findById(employeeID).get();
		NotificationResponseDTO nrDTO=new NotificationResponseDTO(notificationRepository.findByMsgTo(employeeID, PageRequest.of(page, pageSize)), receiver);
		nrDTO.setCopID(employeeRepository.findByPosition(Positions.COP).getId());
		return  nrDTO;
	}
	public int getNotRead(String employeeID) {		
		return notificationRepository.findByMsgToAndRead(employeeID, false).size();
	}
}
