package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.LazyAdapter;
import com.bookworm.model.Book;
import com.bookworm.util.ApplicationUtil;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.book.ListBooksWS;

public class ProfileActivity extends ActivityBase implements OnClickListener {



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
		setContentView(R.layout.profile_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);


		
		
		
		
		
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