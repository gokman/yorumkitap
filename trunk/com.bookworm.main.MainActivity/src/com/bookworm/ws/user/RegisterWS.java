package com.bookworm.ws.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import com.bookworm.model.User;

import android.os.AsyncTask;

public class RegisterWS extends AsyncTask<Object, Void, String> {
		
        @Override
        protected String doInBackground(Object... args) {
        	return POST((String)args[0],(User)args[1]);
        }
        
        @Override
        protected void onPostExecute(String result) {

					
        }
    	public static String POST(String url,User user){

    		 InputStream inputStream = null;
    	        String result = "" ;
    	        int statusCode=0;
    	        try {
    	 
    	            // create HttpClient
    	            DefaultHttpClient httpclient = new DefaultHttpClient();
    	            
    	            // make GET request to the given URL
    	            HttpPost post=new HttpPost(url);
    	            post.setHeader("Accept", "application/json");
    	            post.setHeader("Content-Type", "application/json; charset=UTF-8");
    	           
    	            
    	            JSONObject jsonum=new JSONObject();
                    jsonum.put("userName", user.getUserName());
                    jsonum.put("userEmail", user.getUserEmail());
                    jsonum.put("password", user.getPassword());
                   // jsonum.put("about", user.getAbout());
                   // jsonum.put("creationDate", user.getCreationDate());
    	           // jsonum.put("lastUpdateDate", user.getLastUpdateDate());
    	            jsonum.put("enabled", user.getEnabled());
    	            
    	            if(user.getLoginPlatform()!=null){
    	            	jsonum.put("loginPlatform", user.getLoginPlatform());
    	            }

    	            StringEntity sampleEntity=new StringEntity(jsonum.toString(),HTTP.UTF_8);
                    sampleEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json; charset=UTF-8"));

    	            post.setEntity(sampleEntity);
    	 
    	            // make GET request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(post);
    	            
    	            inputStream = httpResponse.getEntity().getContent();
    	            //sonucu cek
    	 
    	            statusCode=httpResponse.getStatusLine().getStatusCode();
    	            // receive response as inputStream
    	 
    	            // convert inputstream to string
    	            if(inputStream != null){
    	                result = convertInputStreamToString(inputStream);
    	                //add status code in front of result
    	                result=statusCode+":"+result;
    	            }else{
    	                result = "Error";
    	            }
    	 
    	        } catch (Exception e) {
    	            e.getLocalizedMessage();
    	        }
    	 
    	        return result;
        }
    	public void postExecuteForPost(String result){
            
    	}
    	private static String convertInputStreamToString(InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
            String line = "";
            String result = "";
            while((line = bufferedReader.readLine()) != null)
                result += line;
     
            inputStream.close();
            return result;
     
        }
    			    
    }