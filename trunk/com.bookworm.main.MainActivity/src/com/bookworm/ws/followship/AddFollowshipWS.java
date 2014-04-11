package com.bookworm.ws.followship;

import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;

import com.bookworm.common.Utils;
import com.bookworm.model.Followship;
import com.google.gson.Gson;

public class AddFollowshipWS extends AsyncTask<Object, Void, Followship> {
		
        @Override
        protected Followship doInBackground(Object... args) {
        	return POST((String)args[0],(Followship)args[1],(String)args[2],(String)args[3]);
        }
        
        @Override
        protected void onPostExecute(Followship result) {

					
       }
    	public static Followship POST(String url,Followship followship,String username,String password){

    	        InputStream inputStream = null;
    	        Followship resultFollowship =null;
    	        String result = "";
    	        try {
    	            // create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();

    	            // make GET request to the given URL
    	            HttpPost post=new HttpPost(url);
    	            post.setHeader("Accept", "application/json");
    	            post.setHeader("Content-Type", "application/json");
    	            
    	            //credentials
    	            String credentials = username + ":" + password;  
    	            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
     	            
                    JSONObject jsonum=new JSONObject();
                    jsonum.put("followerUserId", followship.getFollowerUserId());
                    jsonum.put("followedUserId", followship.getFollowedUserId());
//                    jsonum.put("creationDate", followship.getCreationDate());
                    
    	            StringEntity sampleEntity=new StringEntity(jsonum.toString());
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
    	        Gson gson = new Gson();
    	        
    	        return gson.fromJson(result, Followship.class);
        }
    	public void postExecuteForPost(String result){
            
    	}
    			    
    }
