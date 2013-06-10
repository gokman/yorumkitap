package com.bookworm.common;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraContent;

public class SelectDataTask extends AsyncTask<NetmeraService, Void, List<NetmeraContent>> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected List<NetmeraContent> doInBackground(final NetmeraService... args) {
		NetmeraService service = null;
		List<NetmeraContent> searchResultList = null;
		if(args.length==1)
			service = args[0];
		 
		if(service!=null){
			try {
				searchResultList=service.search();
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
		}
		return searchResultList;
	}

	// can use UI thread here
	protected void onPostExecute(final String result) {

	}
}
