package com.timesheet.models;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

@Component
public class ConnectedUserModel {
	private static ArrayList<ConnectedUser>connectedUsers=new ArrayList<>();
	
	public static ArrayList<ConnectedUser> getConnectedUsers(){
		return connectedUsers;
	}
	public static void addConnectedUser(ConnectedUser connectedUser) {
		connectedUsers.add(connectedUser);
	}
	public static boolean isConnected(ConnectedUser connectedUser) {
		if(connectedUsers.contains(connectedUser))return true;
		return false;
	}
	public static ArrayList<ConnectedUser> disconnectUser(String accessToken) {
		for(ConnectedUser cu:connectedUsers) {
			if(cu.getJwtToken().equals(accessToken)) {
				cu.setOnline(false);
				break;
			}
		}
		return connectedUsers;
	}
	public static void logout(String accessToken) {
		for(ConnectedUser cu:connectedUsers) {
			if(cu.getJwtToken().equals(accessToken)) {
				cu.setOnline(false);
				break;
			}
		}
	}
	public static boolean isDisconnected(String accessToken) {
		for(ConnectedUser cu:connectedUsers) {
			if(cu.getJwtToken().equals(accessToken)) {
				if(!cu.isOnline())return true;
				break;
			}
		}
		return false;
	}
	public static ArrayList<ConnectedUser>removeDeconnectedUsers(){
		ArrayList<ConnectedUser>alConnu=new ArrayList<>();
		connectedUsers.forEach(cu->{
			if(cu.isOnline())alConnu.add(cu);
		});
		connectedUsers=alConnu;
		return connectedUsers;
	}
}
