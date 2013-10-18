package com.bookworm.common;

import android.os.AsyncTask;

import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;

public class CountDataTask extends AsyncTask<NetmeraService, Void, Long> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected Long doInBackground(final NetmeraService... args) {
		NetmeraService service = null;
		Long dataCount = null;
		if(args.length==1)
			service = args[0];
		 
		if(service!=null){
			try {
				dataCount=service.count();
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
		}
		return dataCount;
	}

	// can use UI thread here
	protected void onPostExecute(final String result) {

	}
}
