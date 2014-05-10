package com.bookworm.ws.comment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.custom.object.CustomComment;

public class ListCommentsWS extends
		AsyncTask<String, Void, List<CustomComment>> {

	@Override
	protected List<CustomComment> doInBackground(String... url) {

		 return GET(url[0]);
	}

	@Override
	protected void onPostExecute(List<CustomComment> result) {
	}

	public static List<CustomComment> GET(String url) {
		InputStream inputStream = null;
		List<CustomComment> resultList= new ArrayList<CustomComment>();
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();
			
			// make GET request to the given URL
            HttpGet get=new HttpGet(url);
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
			String result = "";
			// convert inputstream to string
			if (inputStream != null)
				 result = Utils.convertInputStreamToString(inputStream);
			else
				 result = "Did not work!";

			JSONArray resultArray = new JSONArray(result);
			for (int k = 0 ; k < resultArray.length(); k++){
				JSONObject obj = resultArray.getJSONObject(k);
				CustomComment customComment = new CustomComment();
				customComment.setCommentedBookAdderId(obj.getLong("commentedBookAdderId"));
				customComment.setCommentedBookId(obj.getLong("commentedBookId"));
				customComment.setCommenterId(obj.getLong("commenterId"));
				customComment.setCommentId(obj.getLong("commentId"));
				customComment.setCommentText(obj.getString("commentText"));
				customComment.setCreationDate(ApplicationConstants.dateFormat.format(obj.getLong("creationDate")));
				customComment.setCommenterName(obj.getString("commenterName"));
				
				resultList.add(customComment);
			}
						
		} catch (Exception e) {
			e.getLocalizedMessage();
		}

		return resultList;
	}
}