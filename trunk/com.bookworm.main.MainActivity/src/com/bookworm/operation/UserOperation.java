package com.bookworm.operation;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_IS_USER_EXIST;

import java.util.concurrent.ExecutionException;

import com.bookworm.model.User;
import com.bookworm.ws.user.IsUserExistWS;

public class UserOperation {
	
	public boolean isUserExist(User user) throws InterruptedException, ExecutionException{
		boolean result=new IsUserExistWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_IS_USER_EXIST,
				user).get();
		
		return result;
	}
}
