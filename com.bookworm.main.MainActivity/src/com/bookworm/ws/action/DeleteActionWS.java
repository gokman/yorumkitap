package com.bookworm.ws.action;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.model.Action;
import com.google.gson.Gson;

public class DeleteActionWS extends
		AsyncTask<String, Void, Void> {

	@Override
	protected Void doInBackground(String... url) {

		 GET(url[0]);
		return null;
	}

	public static Action GET(String url) {
		InputStream inputStream = null;
		Gson gson = new Gson();
		String result = ApplicationConstants.EMPTY_STRING;
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			HttpDelete delete = new HttpDelete(url);
			
			delete.setHeader("Accept", "application/json");
			delete.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = ApplicationConstants.signed_in_email + ":" + ApplicationConstants.signed_in_password;  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            delete.addHeader("Authorization", "Basic " + base64EncodedCredentials);
			
			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(delete);
			
			
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

		return null;
	}
}
