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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data
@NoArgsConstructor @AllArgsConstructor
public class Employee {
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(length = 9)
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

}
