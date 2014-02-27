package com.bookworm.model;

import java.util.Date;


public class User{

	private Long userId;
	private String userName;
	private String userEmail;
	private String password;
	private String about;
	private Date creationDate;
	private Date lastUpdateDate;
	private Integer enabled;
	private String activationToken;

	public User() {
	}

	public User(Long id) {
		this.userId = id;
	}
	
	public User(String userName,String userEmail,String password,Integer enabled){
		this.userName=userName;
		this.userEmail=userEmail;
		this.password=password;
		this.enabled=enabled;
	}
	
	public User(String userName,String userEmail,String password,
		     String about,Integer enabled){
	this.userName=userName;
	this.userEmail=userEmail;
	this.password=password;
	this.enabled=enabled;
	this.about=about;
}
	
	public User(String userName,String userEmail,String password,
			Date creationDate, Date lastUpdateDate,Integer enabled){
		this.userName=userName;
		this.userEmail=userEmail;
		this.password=password;
		this.creationDate=creationDate;
		this.lastUpdateDate=lastUpdateDate;
		this.enabled=enabled;
	}

	public User(Long id, String userName, String password, String about,
			Date creationDate, Date lastUpdateDate) {
		this.userId = id;
		this.userName = userName;
		this.password = password;
		this.about = about;
		this.creationDate = creationDate;
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserEmail() {
		return this.userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAbout() {
		return this.about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Date getLastUpdateDate() {
		return this.lastUpdateDate;
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}
	
	public Integer getEnabled() {
		return this.enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}
	
	public String getActivationToken() {
		return activationToken;
	}

	public void setActivationToken(String activationToken) {
		this.activationToken = activationToken;
	}

}