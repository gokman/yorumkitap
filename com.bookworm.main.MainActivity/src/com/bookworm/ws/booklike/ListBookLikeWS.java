package com.bookworm.ws.booklike;

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
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.BookLike;
import com.bookworm.util.SearchCriteria;

public class ListBookLikeWS extends
		AsyncTask<Object, Void, List<BookLike>> {

	@Override
	protected List<BookLike> doInBackground(Object... url) {

		 return GET((String)url[0],(SearchCriteria)url[1],(String)url[2],(String)url[3]);
	}

	@Override
	protected void onPostExecute(List<BookLike> result) {
	}

	public static List<BookLike> GET(String url,SearchCriteria sc,String userName,String password) {
		InputStream inputStream = null;
		String result = "";
		List<BookLike> resultList= new ArrayList<BookLike>();
		try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpPost post=new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = userName + ":" + password;  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            
        	JSONObject inputObj = new JSONObject();
            
        	inputObj.put("bookId", sc.getBookId());
            
            
            
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
				BookLike bookLike = new BookLike();
				bookLike.setBookId(obj.getLong("bookId"));
				bookLike.setBookLikeId(obj.getLong("bookLikeId"));
				bookLike.setBookLikerId(obj.getLong("bookLikerId"));
				//bookLike.setBookLikeDate(new Date());
				resultList.add(bookLike);
			}

		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		
		
		return resultList;
	}
}
