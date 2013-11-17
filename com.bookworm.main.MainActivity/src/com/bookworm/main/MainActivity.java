package com.bookworm.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GeneralUseObject;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraService.SortOrder;

public class MainActivity extends ActivityBase implements OnClickListener {



	ImageButton btn1, btn2, btn3, btn4;
	public LinearLayout middle;

	private ListView latestBooksListView;
    private BaseAdapter adap;
	private LazyAdapter adapter;
	private ArrayList<HashMap<String, String>> latestBooksList;
	private TextView bookListNextButton;
	List<NetmeraContent> mainBookList;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);

		NetmeraClient.init(getApplicationContext(), apiKey);
		bookListNextButton=(TextView)findViewById(R.id.mainBookListNext_Button);
		ApplicationConstants.mainBookListStatus=1;
		bookListNextButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				//tiklandiginda 
				NetmeraService service=new NetmeraService(ApplicationConstants.book);
				service.setMax(ApplicationConstants.item_count_per_page_for_main_page);
				service.setPage(ApplicationConstants.mainBookListStatus);
				service.setSortBy(ApplicationConstants.GENERAL_COLUMN_CREATE_DATE);
		        service.setSortOrder(SortOrder.descending);
		        
		        ApplicationConstants.mainBookListStatus+=1;
		        
		        try {
		        	mainBookList=new SelectDataTask(MainActivity.this).execute(service).get();
					//elimizdeki yeni listeyi actionListToView a ekliyoruz
					//ArrayList<HashMap<String, String>>  tempMainListToView=new ArrayList<HashMap<String, String>>();
					new GeneralUseObject().addListToMainBookListView(mainBookList, latestBooksList, MainActivity.this);
					//adapter i guncelliyoruz
					latestBooksListView.invalidate();
				        ((LazyAdapter) latestBooksListView.getAdapter()).notifyDataSetChanged(); 
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		
		NetmeraService servicer = new NetmeraService(ApplicationConstants.book);
		servicer.setMax(ApplicationConstants.item_count_per_page_for_main_page);
		servicer.setSortBy(ApplicationConstants.GENERAL_COLUMN_CREATE_DATE);
        servicer.setSortOrder(SortOrder.descending);
		List<NetmeraContent> bookList;
		try {
			bookList = new SelectDataTask(MainActivity.this).execute(servicer).get();

		latestBooksList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < bookList.size(); i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			NetmeraContent tempBook1 = bookList.get(i);
//			String tem= tempBook1.get("Path").toString();
//			String tem2 = tempBook1.get("path").toString();
			tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
			NetmeraContent tempBook2 = null;
			if (i != bookList.size() - 1) {
				tempBook2 = bookList.get(i + 1);
				tempBook2.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
			}
			
			String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();//.getNetmeraMedia(ApplicationConstants.book_coverPhoto);
			
			map.put(KEY_COVER_LEFT, tempBook1CoverURL);
			map.put(KEY_BOOK_TITLE_LEFT, tempBook1.get(ApplicationConstants.book_name).toString());
			map.put(KEY_DESC_LEFT, tempBook1.get(ApplicationConstants.book_desc).toString());
			map.put(KEY_BOOK_ADDER_ID_LEFT, tempBook1.get(ApplicationConstants.book_adderId).toString());
			if (tempBook2 != null) {
				String tempBook2CoverURL = new GetNetmerMediaTask().execute(tempBook2).get();//getNetmeraMedia(ApplicationConstants.book_coverPhoto);

				map.put(KEY_COVER_RIGHT, tempBook2CoverURL);
				map.put(KEY_BOOK_TITLE_RIGHT, tempBook2.get(ApplicationConstants.book_name).toString());
				map.put(KEY_DESC_RIGHT, tempBook2.get(ApplicationConstants.book_desc).toString());
				map.put(KEY_BOOK_ADDER_ID_RIGHT, tempBook2.get(ApplicationConstants.book_adderId).toString());
			}

			latestBooksList.add(map);
		}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NetmeraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		latestBooksListView = (ListView) findViewById(R.id.latest_books);

		adapter = new LazyAdapter(this, latestBooksList);
		latestBooksListView.setAdapter(adapter);

		// Click event for single list row
		latestBooksListView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object o = latestBooksListView.getItemAtPosition(position);
				System.out.println("damn it");
				
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