package com.bookworm.common;

import java.util.List;

import android.content.Context;
import android.os.AsyncTask;

import com.bookworm.main.ActivityBase;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;

public class SelectDataTask extends AsyncTask<NetmeraService, Void, List<NetmeraContent>> {


	private ActivityBase baseActivity;
	private Context context ;
	
	public SelectDataTask(ActivityBase baseActivity){
		this.baseActivity = baseActivity;
		context = baseActivity;
	}
	
	
	// can use UI thread here
	protected void onPreExecute() {
		super.onPreExecute();
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
	@Override
	protected void onPostExecute(List<NetmeraContent> result){
	}
}
