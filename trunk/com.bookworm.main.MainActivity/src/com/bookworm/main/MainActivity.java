package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOK;

import static com.bookworm.common.ApplicationConstants.GENERAL_COLUMN_CREATE_DATE;
import static com.bookworm.common.ApplicationConstants.ORDER_BY_DIRECTION_DESCENDING;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST;
import static com.bookworm.common.ApplicationConstants.item_count_per_page_for_main_page;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bookworm.common.LazyAdapter;
import com.bookworm.model.Book;
import com.bookworm.util.ApplicationUtil;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.book.ListBooksWS;

public class MainActivity extends ActivityBase implements OnClickListener {



	private int pageNumber = 0;
	private ListView latestBooksListView;
	private LazyAdapter adapter;
	private ArrayList<HashMap<String, String>> latestBooksList = new ArrayList<HashMap<String,String>>();
	private TextView bookListNextButton;
	List<Book> latestBooks = Collections.emptyList();
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);

		bookListNextButton=(TextView)findViewById(R.id.mainBookListNext_Button);

		SearchCriteria sc = new SearchCriteria();
		sc.setPageSize(item_count_per_page_for_main_page);
		sc.setPageNumber(pageNumber);
		sc.setOrderByCrit(GENERAL_COLUMN_CREATE_DATE);
        sc.setOrderByDrc(ORDER_BY_DIRECTION_DESCENDING);
		try {
			latestBooks = new ListBooksWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST+"/",sc).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}							
        pageNumber++;

		ApplicationUtil.addListToMainBookListView(latestBooks, latestBooksList);

		latestBooksListView = (ListView) findViewById(R.id.latest_books);

		adapter = new LazyAdapter(this, latestBooksList);
		latestBooksListView.setAdapter(adapter);

		bookListNextButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				SearchCriteria sc = new SearchCriteria();
				sc.setPageSize(item_count_per_page_for_main_page);
				sc.setPageNumber(pageNumber);
				sc.setOrderByCrit(GENERAL_COLUMN_CREATE_DATE);
		        sc.setOrderByDrc(ORDER_BY_DIRECTION_DESCENDING);
				try {
					latestBooks = new ListBooksWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST+"/",sc).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}							
		        pageNumber++;
				ApplicationUtil.addListToMainBookListView(latestBooks, latestBooksList);
				latestBooksListView.invalidate();
			        ((LazyAdapter) latestBooksListView.getAdapter()).notifyDataSetChanged(); 
			}
		});		
		
		// Click event for single list row
		latestBooksListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object o = latestBooksListView.getItemAtPosition(position);
			}
		});
		setNavigationButtons();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main, menu);
		return true;
	}


	public void onClick(View v) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
	}
}