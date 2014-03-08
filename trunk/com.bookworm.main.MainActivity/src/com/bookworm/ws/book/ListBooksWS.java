package com.bookworm.ws.book;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
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
import com.bookworm.model.Book;
import com.bookworm.model.Hashtag;
import com.bookworm.util.SearchCriteria;

public class ListBooksWS extends
		AsyncTask<Object, Void, List<Book>> {

	@Override
	protected List<Book> doInBackground(Object... url) {

		 return GET((String)url[0],(SearchCriteria)url[1]);
	}

	@Override
	protected void onPostExecute(List<Book> result) {
	}

	public static List<Book> GET(String url,SearchCriteria sc) {
		InputStream inputStream = null;
		String result = "";
		List<Book> resultList= new ArrayList<Book>();
		try {

            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpPost post=new HttpPost(url);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            
            //credentials
            String credentials = "gokhankcmn@gmail.com" + ":" + "12345678";  
            String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);  
            post.addHeader("Authorization", "Basic " + base64EncodedCredentials);
            
        	JSONObject inputObj = new JSONObject();
            
            if(sc.getBookIdList()!=null && sc.getBookIdList().size()>0){
            	JSONArray bookIds = new JSONArray();
            	for(Long bookId : sc.getBookIdList()){
	            	bookIds.put(bookId);
	            }
	        	inputObj.put("bookIdList", bookIds);            
            }
            inputObj.put("orderByDrc", sc.getOrderByDrc());
            inputObj.put("orderByCrit", sc.getOrderByCrit());
            inputObj.put("pageNumber", sc.getPageNumber());
            inputObj.put("pageSize", sc.getPageSize());
            
            
            
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
				Book book = new Book();
				book.setAdderId(obj.getLong("adderId"));
				book.setBookId(obj.getLong("bookId"));
				book.setCoverPhoto(obj.getString("coverPhoto"));
				book.setDescription(obj.getString("description"));
				book.setName(obj.getString("name"));
				book.setWriter(obj.getString("writer"));
				resultList.add(book);
			}

		} catch (Exception e) {
			e.getLocalizedMessage();
		}
		
		
		return resultList;
	}
}
