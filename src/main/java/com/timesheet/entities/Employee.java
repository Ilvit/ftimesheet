package com.timesheet.entities;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.timesheet.enums.Genders;
import com.timesheet.enums.Positions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor @AllArgsConstructor
public class Employee {
	@Id @Column(length = 10)
	private String id;
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
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
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
	
	
}
