package com.timesheet.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class ConnectedUser {
	private String username;
	private String jwtToken;
	private boolean online;

}
