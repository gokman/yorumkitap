package com.bookworm.ws.user;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.bookworm.model.User;
import com.bookworm.util.SearchCriteria;

public class IsUserExistWS extends AsyncTask<Object, Void, Boolean> {

	@Override
	protected Boolean doInBackground(Object... url) {

		return GET((String) url[0], (User) url[1]);
	}

	protected void onPostExecute(boolean result) {
	}

	public static boolean GET(String url, User user) {
		InputStream inputStream = null;
		boolean result = false;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpPost post = new HttpPost(url);
			post.setHeader("Accept", "application/json");
			post.setHeader("Content-Type", "application/json");

			JSONObject jsonum = new JSONObject();
			jsonum.put("userEmail", user.getUserEmail());

			StringEntity sampleEntity = new StringEntity(jsonum.toString());
			sampleEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE,
					"application/json"));

			post.setEntity(sampleEntity);
			HttpResponse httpResponse = httpclient.execute(post);

			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				result = Utils.convertInputStreamToBoolean(inputStream);
			else
				result = false;

		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		return result;
	}
}
