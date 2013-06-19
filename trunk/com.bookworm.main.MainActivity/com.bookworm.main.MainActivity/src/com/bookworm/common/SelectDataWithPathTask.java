package com.bookworm.common;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraContent;

public class SelectDataWithPathTask extends AsyncTask<NetmeraService, Void, NetmeraContent> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected NetmeraContent doInBackground(final NetmeraService... args) {
		NetmeraService service = null;
		NetmeraContent searchResult = null;
		if(args.length==1)
			service = args[0];
		 
		if(service!=null){
			try {
				searchResult=service.get();
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
		}
		return searchResult;
	}

	// can use UI thread here
	protected void onPostExecute(final String result) {

	}
}
