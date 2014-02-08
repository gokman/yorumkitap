package com.bookworm.ws.hashtag;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.bookworm.common.Utils;
import com.bookworm.model.Comment;
import com.bookworm.model.Hashtag;

public class ListHashtagsWS extends
		AsyncTask<String, Void, List<Hashtag>> {

	@Override
	protected List<Hashtag> doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(List<Hashtag> result) {
	}

	public static List<Hashtag> GET(String url) {
		InputStream inputStream = null;
		List<Hashtag> resultList= new ArrayList<Hashtag>();
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			String result = "";
			// convert inputstream to string
			if (inputStream != null)
				 Utils.convertInputStreamToString(inputStream);
			else
				 result = "Did not work!";

		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		return resultList;
	}
}
