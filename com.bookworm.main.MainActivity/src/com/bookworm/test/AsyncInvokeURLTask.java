package com.bookworm.test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import com.bookworm.model.Book;

import android.os.AsyncTask;

	public class AsyncInvokeURLTask extends AsyncTask<Void, Void, String> {
		   private static final String SOAP_ACTION = "http://bookapp.com/savebook/GetSaveBook";
	       private static final String METHOD_NAME = "GetSaveBook";
	       private static final String NAMESPACE = "http://bookapp.com/savebook/";
	       private static final String URL = "http://192.168.2.148:8080/kitapyorum/services/SaveBookRequest";
	       
	       Book book;

	    public static interface OnPostExecuteListener{
	        void onPostExecute(String result);
	    }

	    AsyncInvokeURLTask() throws Exception {


	    }

	    @Override
	    protected String doInBackground(Void... params) {

	        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME); 
	        
	        
	        PropertyInfo weightProp =new PropertyInfo();
	        weightProp.setName("name");
	        weightProp.setValue("Fatih");
	        weightProp.setType(Book.class);
	        request.addProperty(weightProp);
	          
	        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
	        envelope.dotNet = false;
	        envelope.setOutputSoapObject(request);
	        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
	  
	        try {
	            androidHttpTransport.call(SOAP_ACTION, envelope);
	            SoapObject response = (SoapObject)envelope.getResponse();
	            
	            book.setDescription(response.toString());
	  
	        } catch (Exception e) {
	            e.printStackTrace();
	        }        

	        try {

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return "";
	    }

	    @Override
	    protected void onPostExecute(String result) {
	    }

	    private static String convertStreamToString(InputStream is){
	        BufferedReader reader = new BufferedReader(
	            new InputStreamReader(is));
	        StringBuilder sb = new StringBuilder();

	        String line = null;

	        try {
	            while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                is.close();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        return sb.toString();
	    }
	} // AsyncInvokeURLTask
