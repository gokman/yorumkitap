package com.bookworm.common;

import android.os.AsyncTask;

import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;

public class DeletetDataTask extends AsyncTask<NetmeraContent, Void, Void> {

	// can use UI thread here
	protected void onPreExecute() {
	}

	// automatically done on worker thread (separate from UI thread)
	protected Void doInBackground(final NetmeraContent... contents) {

		NetmeraContent content = null;
		if (contents != null && contents.length == 1)
			content = contents[0];

		if (content != null) {
			try {
				content.delete();
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
		}

		return null;
	}

	// can use UI thread here
	protected void onPostExecute(final Void unused) {

	}

}
