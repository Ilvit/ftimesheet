package com.timesheet.entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.timesheet.enums.RolesNames;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class AppUser {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true)
	private String username;
	@JsonProperty(access = Access.WRITE_ONLY)
	private String password;
	@ManyToMany(fetch = FetchType.EAGER)
	private Set<Role>roles;
	@Column(unique = true)
	private String mail; 
	private String employeeID;
	private String supervisorID;
	private transient List<UserRoles>userRoles;
	private transient List<String>rolesNames;
	private transient List<String>addedRolesNames;
	
	public AppUser(String username, String password, String employeeID) {
		this.username=username;
		this.password=password;
		this.employeeID=employeeID;
	}
	
	public void loadUserRoles() {
//		remplissage des taxes pour l'affichage
		userRoles=new ArrayList<>(); rolesNames=new ArrayList<>(); addedRolesNames=new ArrayList<>();
		if(roles==null)roles=new HashSet<Role>();
		for(RolesNames rn:RolesNames.values()) {
			roles.forEach(rol->{
				rolesNames.add(rol.getName());
			});
			//On l'ajoute si elle n'est pas encore été ajoutée dans la liste
			if(!addedRolesNames.contains(rn.name())) {
				if(rolesNames.contains(rn.name())) {//paid true or false
					userRoles.add(UserRoles.builder().roleName(rn.name()).hasRole(true).build());
				}else userRoles.add(UserRoles.builder().roleName(rn.name()).hasRole(false).build());
			}			
			if(!addedRolesNames.contains(rn.name()))addedRolesNames.add(rn.name());
		}
	}
}
