package com.heaplay.model.beans;

import java.io.Serializable;

/**
 *	DTO dell'User Table 
 *
 */

public class UserBean implements Serializable,Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private long id;
	private String username;
	private String email;
	private String password;
	private String auth;
	private boolean active;
	
	public UserBean() {
		id = -1;
		username = email = password = auth = "";
		active = false;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public boolean equals(Object otherOb) {
		if(otherOb == null || otherOb.getClass().getName()!=getClass().getName())
			return false;
		UserBean other = (UserBean) otherOb;
		return other.id == id;
	}

	@Override
	public UserBean clone() {
		UserBean bean = null;
		try {
			bean = (UserBean) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	@Override
	public String toString() {
		return "UserBean [id=" + id + ", username=" + username + ", email=" + email + ", password=" + password
				+ ", auth=" + auth + ", active=" + active + "]";
	}

}