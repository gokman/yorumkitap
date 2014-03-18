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

public class GetUserPasswordWS extends
		AsyncTask<Object, Void, String> {

	@Override
	protected String doInBackground(Object... url) {

		 return GET((String)url[0],(String)url[1]);
	}

	@Override
	protected void onPostExecute(String result) {
	}

	public static String GET(String url,String email) {
		InputStream inputStream = null;
		String result = "";
		try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpPost post=new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = "gokhankcmn@gmail.com" + ":" + "12345678";  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            
        	JSONObject inputObj = new JSONObject();
            inputObj.put("email", email);
            
            StringEntity sampleEntity=new StringEntity(inputObj.toString());
            sampleEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

            post.setEntity(sampleEntity);
            HttpResponse httpResponse = httpclient.execute(post);

            inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if (inputStream != null)
				 result = Utils.convertInputStreamToString(inputStream);
			else
				 result = "Did not work!";

			JSONArray resultArray = new JSONArray(result);
			for (int k = 0 ; k < resultArray.length(); k++){
				JSONObject obj = resultArray.getJSONObject(k);
				result=obj.getString("password").toString();
			}

		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		
		
		return result;
	}
}
