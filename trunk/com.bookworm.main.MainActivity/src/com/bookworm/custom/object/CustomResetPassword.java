package com.bookworm.custom.object;

public class CustomResetPassword {
     private String email;
     private String password;
     private long token;
     
     public CustomResetPassword(String email,String password,long token){
    	 this.email=email;
    	 this.password=password;
    	 this.token=token;
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
	public long getToken() {
		return token;
	}
	public void setToken(long token) {
		this.token = token;
	}
}
