package com.bookworm.common;

import android.os.AsyncTask;

import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraUser;

public class InsertUserTask extends AsyncTask<NetmeraUser, Void, Void> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected Void doInBackground(final NetmeraUser... users) {
		
		NetmeraUser user = null;
		if(users != null && users.length == 1)
			user = users[0];
			
		try {
		
			if(user!=null){
				user.register();
			}

		} catch (NetmeraException e) {
			try {
				throw e;
			} catch (NetmeraException e1) {
				e1.printStackTrace();
			}
		}

		return null;
	}

	// can use UI thread here
	protected void onPostExecute(final Void unused) {

	}

}
