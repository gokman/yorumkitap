package com.bookworm.ws.comment;

import java.io.InputStream;
import java.util.Date;

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

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.bookworm.model.Comment;
import com.google.gson.Gson;

public class AddCommentWS extends AsyncTask<Object, Void, Comment> {
		
        @Override
        protected Comment doInBackground(Object... args) {
        	return POST((String)args[0],(Comment)args[1]);
        }
        
        @Override
        protected void onPostExecute(Comment result) {

					
       }
    	public static Comment POST(String url,Comment comment){

    	       InputStream inputStream = null;
    	       Comment resultBook =null;
    	        String result = "";
    	        try {
    	            // create HttpClient
    	            HttpClient httpclient = new DefaultHttpClient();

    	            // make GET request to the given URL
    	            HttpPost post=new HttpPost(url);
    	            post.setHeader("Accept", "application/json");
    	            post.setHeader("Content-Type", "application/json");
    	            
    	            //credentials
    	            String credentials = ApplicationConstants.signed_in_email + ":" + ApplicationConstants.signed_in_password;  
    	            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
    	            
                    JSONObject jsonum=new JSONObject();
                    jsonum.put("commenterId", comment.getCommenterId());
                    jsonum.put("commentText", comment.getCommentText());
                    jsonum.put("commentedBookId", comment.getCommentedBookId());
                    jsonum.put("commentedBookAdderId", comment.getCommentedBookAdderId());
                    jsonum.put("creationDate", comment.getCreationDate());

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
    	        
    	        return gson.fromJson(result, Comment.class);
        }
    	public void postExecuteForPost(String result){
            
    	}
    			    
    }
