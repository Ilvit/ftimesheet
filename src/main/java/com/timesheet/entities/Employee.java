package com.timesheet.entities;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timesheet.enums.Genders;
import com.timesheet.enums.Positions;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity 
public class Employee {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 9, unique = true)
	private String employeeID;
	private String name;
	private String postName;
	private String nickName;
	@Column(unique = true)
	private String mail;
	private String phoneNumber;
	private Genders gender;
	private Positions position;
	private String supervisorID;
	@OneToMany(mappedBy = "sender")
	@JsonIgnore
	private List<Notification>notifications;
	private int otherWorkedHours;
	
	
	public Employee() {
		super();
	}


	public Employee(Long id, String employeeID, String name, String postName, String nickName, String mail,
			String phoneNumber, Genders gender, Positions position, String supervisorID,
			List<Notification> notifications, int otherWorkedHours) {
		super();
		this.id = id;
		this.employeeID = employeeID;
		this.name = name;
		this.postName = postName;
		this.nickName = nickName;
		this.mail = mail;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.position = position;
		this.supervisorID = supervisorID;
		this.notifications = notifications;
		this.otherWorkedHours = otherWorkedHours;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getEmployeeID() {
		return employeeID;
	}


	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPostName() {
		return postName;
	}


	public void setPostName(String postName) {
		this.postName = postName;
	}


	public String getNickName() {
		return nickName;
	}


	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public String getMail() {
		return mail;
	}


	public void setMail(String mail) {
		this.mail = mail;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public Genders getGender() {
		return gender;
	}


	public void setGender(Genders gender) {
		this.gender = gender;
	}


	public Positions getPosition() {
		return position;
	}


	public void setPosition(Positions position) {
		this.position = position;
	}


	public String getSupervisorID() {
		return supervisorID;
	}


	public void setSupervisorID(String supervisorID) {
		this.supervisorID = supervisorID;
	}


	public List<Notification> getNotifications() {
		return notifications;
	}


	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}


	public int getOtherWorkedHours() {
		return otherWorkedHours;
	}


	public void setOtherWorkedHours(int otherWorkedHours) {
		this.otherWorkedHours = otherWorkedHours;
	}	
	
}
