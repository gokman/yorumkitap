package com.bookworm.ws.user;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Base64;
import android.widget.Toast;

import com.bookworm.common.Utils;
import com.bookworm.model.Book;
import com.google.gson.Gson;

public class LoginWS extends AsyncTask<Object, Void, String> {
		
        @Override
        protected String doInBackground(Object... args) {
        	return GET((String)args[0],(String)args[1],(String)args[2]);
        }
        
        @Override
        protected void onPostExecute(String result) {

					
       }
    	public static String GET(String url,String username,String password){

    		 InputStream inputStream = null;
    	        String result = "" ;
    	        int statusCode=0;
    	        try {
    	 
    	            // create HttpClient
    	            DefaultHttpClient httpclient = new DefaultHttpClient();
    	            
    	            // make GET request to the given URL
    	            HttpGet get=new HttpGet(url);
    	            get.setHeader("Accept", "application/json");
    	            get.setHeader("Content-Type", "application/json");
    	            
    	            //credentials
    	            String credentials = username + ":" + password;  
    	            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
    	            get.addHeader("Authorization", "Basic " + base64EncodedCredentials);
    	            
    	 
    	            // make GET request to the given URL
    	            HttpResponse httpResponse = httpclient.execute(get);
    	            
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
