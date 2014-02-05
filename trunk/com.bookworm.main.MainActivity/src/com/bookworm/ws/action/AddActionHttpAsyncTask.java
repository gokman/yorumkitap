package com.bookworm.ws.action;

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
import com.bookworm.model.Action;
import com.bookworm.model.Book;
import com.google.gson.Gson;

public class AddActionHttpAsyncTask extends AsyncTask<Object, Void, Action> {
		
        @Override
        protected Action doInBackground(Object... args) {
        	return POST((String)args[0],(Action)args[1]);
        }
        
        @Override
        protected void onPostExecute(Action result) {

					
       }
    	public static Action POST(String url,Action action){

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
    	            String credentials = "gokman" + ":" + "kocaman";  
    	            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
    	            
    	            //TODO Aksiyon bilgileri set edilicek
                    JSONObject jsonum=new JSONObject();

                    
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
    	        
    	        return gson.fromJson(result, Action.class);
        }
    	public void postExecuteForPost(String result){
            
    	}
    			    
    }
