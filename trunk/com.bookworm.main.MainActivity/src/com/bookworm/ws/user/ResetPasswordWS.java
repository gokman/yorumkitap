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

import com.bookworm.custom.object.CustomResetPassword;
import com.bookworm.model.User;

import android.os.AsyncTask;

public class ResetPasswordWS extends AsyncTask<Object, Void, String> {
		
        @Override
        protected String doInBackground(Object... args) {
        	return POST((String)args[0],(CustomResetPassword)args[1]);
        }
        
        @Override
        protected void onPostExecute(String result) {

					
       }
    	public static String POST(String url,CustomResetPassword customObject){

    		 InputStream inputStream = null;
    	        String result = "" ;
    	        int statusCode=0;
    	        try {
    	 
    	            // create HttpClient
    	            DefaultHttpClient httpclient = new DefaultHttpClient();
    	            
    	            // make GET request to the given URL
    	            HttpPost post=new HttpPost(url);
    	            post.setHeader("Accept", "application/json");
    	            post.setHeader("Content-Type", "application/json");
    	            
    	            //credentials
    	            //String credentials = "gokhankcmn@gmail.com"+":"+"kocaman" ;  
    	            //String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            //post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
    	            JSONObject jsonum=new JSONObject();
                    jsonum.put("email", customObject.getEmail());
                    jsonum.put("password", customObject.getPassword());
                    jsonum.put("token", customObject.getToken());
                    

    	            StringEntity sampleEntity=new StringEntity(jsonum.toString());
                    sampleEntity.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));

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
