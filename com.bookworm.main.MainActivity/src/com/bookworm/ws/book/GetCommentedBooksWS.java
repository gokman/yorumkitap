package com.bookworm.ws.book;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.bookworm.model.BookLike;
import com.google.gson.Gson;

public class GetCommentedBooksWS extends
		AsyncTask<String, Void, List<Book>> {

	@Override
	protected List<Book> doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(List<Book> resultList) {
	}

	public static List<Book> GET(String url) {
		InputStream inputStream = null;
		Gson gson = new Gson();
		String result = ApplicationConstants.EMPTY_STRING;
		List<Book> resultList = new ArrayList<Book>();
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

			JSONArray resultArray = new JSONArray(result);
			for (int k = 0 ; k < resultArray.length(); k++){
				JSONObject obj = resultArray.getJSONObject(k);
				Book book = new Book();
				book.setAdderId(obj.getLong("adderId"));
				book.setBookId(obj.getLong("bookId"));
				book.setCoverPhoto(obj.getString("coverPhoto"));
				book.setDescription(obj.getString("description"));
				book.setName(obj.getString("name"));
				book.setWriter(obj.getString("writer"));
				resultList.add(book);
			}			
			
		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		return resultList;
	}
}
