package com.bookworm.ws.like;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.bookworm.common.Utils;
import com.bookworm.model.BookLike;

public class GetBookLikeInfoHttpAsyncTask extends
		AsyncTask<String, Void, List<BookLike>> {

	@Override
	protected List<BookLike> doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(List<BookLike> result) {
	}

	public static List<BookLike> GET(String url) {
		InputStream inputStream = null;
		List<BookLike> resultList= new ArrayList<BookLike>();
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
