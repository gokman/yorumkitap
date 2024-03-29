package com.bookworm.ws.book;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.bookworm.model.BookLike;
import com.google.gson.Gson;

public class GetBookInfoWS extends
		AsyncTask<String, Void, Book> {

	@Override
	protected Book doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(Book result) {
	}

	public static Book GET(String url) {
		InputStream inputStream = null;
		Gson gson = new Gson();
		String result = ApplicationConstants.EMPTY_STRING;
		try {
			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet get = new HttpGet(url);
			
			get.setHeader("Accept", "application/json");
			get.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = ApplicationConstants.signed_in_email + ":" + ApplicationConstants.signed_in_password;  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            get.addHeader("Authorization", "Basic " + base64EncodedCredentials);
			
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(get);

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

		return gson.fromJson(result, Book.class);
	}
}
