package com.bookworm.common;

import java.util.concurrent.ExecutionException;

import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;

public class DatabaseProcess {
	//id dediðimiz aslýnda email adresi oluyor
public String getUserName(String id){
	NetmeraContent userRow=null;
	String username=null;
	NetmeraService userService=new NetmeraService(ApplicationConstants.user);
	userService.whereEqual(ApplicationConstants.user_email, id);
	try {
		userRow=new SelectDataTask().execute(userService).get().get(0);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (ExecutionException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	try {
		username=userRow.get(ApplicationConstants.user_username).toString();
	} catch (NetmeraException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	return username;
}
}
