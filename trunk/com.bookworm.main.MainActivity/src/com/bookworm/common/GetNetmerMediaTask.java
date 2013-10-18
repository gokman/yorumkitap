package com.bookworm.common;

import android.os.AsyncTask;

import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;

public class GetNetmerMediaTask extends AsyncTask<NetmeraContent, Void, String> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected String doInBackground(final NetmeraContent... args) {
		NetmeraContent content = null;
		NetmeraMedia netmeraMedia = null;
		String mediaURL = null;
		if(args.length==1)
			content= args[0];
		 
		if(content!=null){
			try {
				netmeraMedia=content.getNetmeraMedia(content.get(ApplicationConstants.generic_property).toString());
				mediaURL =netmeraMedia.getUrl(NetmeraMedia.PhotoSize.SMALL);
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
		}
		return mediaURL;
	}

	// can use UI thread here
	@Override
	protected void onPostExecute(final String result) {

	}
}
