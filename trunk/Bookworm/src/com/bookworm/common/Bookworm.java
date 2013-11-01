package com.bookworm.common;

import android.app.Application;

public class Bookworm extends Application{
	private String myApplicationString;

	public String getMyApplicationString() {
		return myApplicationString;
	}
 
	public void setMyApplicationString(String myApplicationString) {
		this.myApplicationString = myApplicationString;
	}
	
}
