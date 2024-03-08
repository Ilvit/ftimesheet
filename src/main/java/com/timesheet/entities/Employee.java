package com.timesheet.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.timesheet.enums.ContractTypes;
import com.timesheet.enums.Genders;
import com.timesheet.enums.Positions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class Employee {
	@Id
	private String id;
	private String name;
	private String postName;
	private String nickName;
	@Column(unique = true)
	private String mail;
	private String phoneNumber;
	private Genders gender;
	private ContractTypes contractType;
	private Positions position;
	private String supervisorID;
	
	
}
