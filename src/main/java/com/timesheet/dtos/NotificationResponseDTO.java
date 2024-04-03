package com.timesheet.dtos;

import java.util.List;

import org.springframework.data.domain.Page;

import com.timesheet.entities.Employee;
import com.timesheet.entities.Notification;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class NotificationResponseDTO {

	private Employee receiver;
	private List<Notification>notifications;
	private int pageSize;
	private Long totalNotifications;
	private int totalPages;
	private int currentPage;
	private int[] pagesArray;
	private int totalNotifOnPage;
	private String copID;
	
	public NotificationResponseDTO(Page<Notification>notifPage, Employee receiver) {
		this.receiver=receiver;
		this.notifications=notifPage.getContent();
		this.totalNotifications=notifPage.getTotalElements();
		this.currentPage=notifPage.getNumber();
		this.totalPages=notifPage.getTotalPages();
		this.totalNotifOnPage=notifPage.getNumberOfElements();
		this.pageSize=notifPage.getSize();
		this.pagesArray=new int[totalPages];
		
		for(int i=0;i<pagesArray.length;i++) {
			pagesArray[i]=i;
		}
	}
}
