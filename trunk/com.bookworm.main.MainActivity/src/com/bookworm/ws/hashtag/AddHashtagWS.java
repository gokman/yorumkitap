package com.bookworm.ws.hashtag;

import java.io.InputStream;
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
import com.bookworm.model.Hashtag;

public class AddHashtagWS extends AsyncTask<Object, Void, List<Hashtag>> {
		
        @Override
        protected List<Hashtag> doInBackground(Object... args) {
        	return POST((String)args[0],(List<Hashtag>)args[1]);
        }
        
        @Override
        protected void onPostExecute(List<Hashtag> result) {

					
       }
    	public static List<Hashtag> POST(String url,List<Hashtag> hashtags){

    	       InputStream inputStream = null;
    	       Hashtag resultTag =null;
    	        String result = "";
    	        try {
    	            // create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();

    	            // make GET request to the given URL
    	            HttpPost post=new HttpPost(url);
    	            post.setHeader("Accept", "application/json");
    	            post.setHeader("Content-Type", "application/json");
    	            
    	            //credentials
    	            String credentials = "gokman" + ":" + "kocaman";  
    	            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
    	            
    	            JSONArray jsonHashTags = new JSONArray();
    	            for(Hashtag hashTag : hashtags){
	                    JSONObject jsonum=new JSONObject();
	                    jsonum.put("bookId", hashTag.getBookId());
	                    jsonum.put("hashTagId", hashTag.getHashTagId());
	                    jsonum.put("tag", hashTag.getTag());
	                    jsonHashTags.put(jsonum);
    	            }
    	            StringEntity sampleEntity= new StringEntity(jsonHashTags.toString());
                    sampleEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

    	            post.setEntity(sampleEntity);
    	            HttpResponse httpResponse = httpclient.execute(post);
    	 
    	            // receive response as inputStream
    	            inputStream = httpResponse.getEntity().getContent();
    	            // convert inputstream to string
    	            if(inputStream != null)
    	                result = Utils.convertInputStreamToString(inputStream);
    	            else
    	                result = "Did not work!";
    	 
    	        } catch (Exception e) {
    	            e.getLocalizedMessage();
    	        }
    	        return hashtags;
        }
    	public void postExecuteForPost(String result){
            
    	}
    			    
    }
