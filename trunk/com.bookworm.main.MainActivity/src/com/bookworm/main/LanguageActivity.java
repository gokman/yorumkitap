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
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;

public class LanguageActivity extends ActivityBase implements OnClickListener {

	public static final String KEY_COVER_LEFT = "coverLeft";
	public static final String KEY_COVER_RIGHT = "coverRight";
	public static final String KEY_BOOK_TITLE_LEFT = "book_title_left";
	public static final String KEY_BOOK_TITLE_RIGHT = "book_title_right";
	public static final String KEY_DESC_LEFT = "descLeft";
	public static final String KEY_DESC_RIGHT = "descRight";
	public static final String KEY_BOOK_ADDER_ID_RIGHT = "book_adder_id_right";
	public static final String KEY_BOOK_ADDER_ID_LEFT = "book_adder_id_left";

	ImageButton btn1, btn2, btn3, btn4;
	public LinearLayout middle;

	private ListView latestBooksListView;

	private LazyAdapter adapter;
	private ArrayList<HashMap<String, String>> latestBooksList;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.language);
	
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