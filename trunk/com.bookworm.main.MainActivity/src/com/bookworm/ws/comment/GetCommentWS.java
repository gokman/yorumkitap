package com.bookworm.ws.comment;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.bookworm.model.Comment;
import com.google.gson.Gson;

public class GetCommentWS extends
		AsyncTask<String, Void, Comment> {

	@Override
	protected Comment doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(Comment result) {
	}

	public static Comment GET(String url) {
		InputStream inputStream = null;
		Gson gson = new Gson();
		String result = ApplicationConstants.EMPTY_STRING;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();
			
			// convert inputstream to string
			if (inputStream != null)
				 result = Utils.convertInputStreamToString(inputStream);
			else
				 result = "Did not work!";

		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		return gson.fromJson(result, Comment.class);
	}
}
