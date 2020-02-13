package com.example.ezeats.order;

import java.util.Set;

public class StateMessage {
	private String type;
	private String user;
	private Set<String> roleser;
	
	public StateMessage(String type, String user, Set<String> roleser) {
		super();
		this.type = type;
		this.user = user;
		this.roleser = roleser;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public Set<String> getRoleser() {
		return roleser;
	}

	public void setRoleser(Set<String> roleser) {
		this.roleser = roleser;
	}
	
	
	
}
