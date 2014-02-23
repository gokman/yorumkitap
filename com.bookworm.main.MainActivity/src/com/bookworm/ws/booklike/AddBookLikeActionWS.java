package com.bookworm.ws.booklike;

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

import com.bookworm.common.Utils;
import com.bookworm.model.ActionType;
import com.bookworm.model.BookLike;

public class AddBookLikeActionWS extends AsyncTask<Object, Void, BookLike> {
		
        @Override
        protected BookLike doInBackground(Object... args) {
        	return POST((String)args[0],(BookLike)args[1]);
        }
        
        @Override
        protected void onPostExecute(BookLike result) {

					
       }
    	public static BookLike POST(String url,BookLike bookLike){

    	       InputStream inputStream = null;
    	        String result = "";
    	        BookLike actionReturned = null;
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
                    jsonum.put("bookId", bookLike.getBookId());
                    jsonum.put("bookLikeDate", bookLike.getBookLikeDate());
                    jsonum.put("bookLikerId", bookLike.getBookLikerId());
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
    	 
    					JSONObject obj = new JSONObject(result);
    					bookLike = new BookLike();
    					bookLike.setBookId(obj.getLong("bookId"));
    					bookLike.setBookLikeDate(new Date(obj.getLong("bookLikeDate")));
    					bookLike.setBookLikeId(obj.getLong("bookLikeId"));
    					bookLike.setBookLikerId(obj.getLong("bookLikerId"));
    	            
    	        } catch (Exception e) {
    	            e.getLocalizedMessage();
    	        }
    	        return bookLike;
        }
    	public void postExecuteForPost(String result){
            
    	}
    			    
    }
