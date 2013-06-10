package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;

public class MainActivity extends ActivityBase implements OnClickListener {

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
	private Uri fileUri;
	private ImageView explore_button;
	private ImageView home_button;
	private ImageView add_book_button;
	private ImageView profile_button;
	
	private ListView latestBooksListView;

	private LazyAdapter adapter;
	private ArrayList<HashMap<String, String>> latestBooksList;
	

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.home_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		explore_button = (ImageView)findViewById(R.id.explore_button);
		home_button = (ImageView)findViewById(R.id.home_button);
		add_book_button = (ImageView)findViewById(R.id.add_button);
		profile_button = (ImageView)findViewById(R.id.profile_button);
		NetmeraClient.init(getApplicationContext(), apiKey);
		
		NetmeraService servicer = new NetmeraService(ApplicationConstants.book);
		servicer.setMax(ApplicationConstants.item_count_per_page_for_main_page);
		List<NetmeraContent> bookList;
		try {
			bookList = new SelectDataTask().execute(servicer).get();

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
		
		explore_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent exploreIntent = new Intent(getApplicationContext(), ExploreActivity.class);
				startActivity(exploreIntent);
			}
		});
		home_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(homeIntent);
			}
		});
		add_book_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					PackageManager packageManager = getPackageManager();
					boolean doesHaveCamera = packageManager
							.hasSystemFeature(PackageManager.FEATURE_CAMERA);

					if (doesHaveCamera) {
						Camera mCamera = Camera.open();
						// start the image capture Intent
						Camera.Parameters cp = mCamera.getParameters();

						Size cameraResolution = cp.getPictureSize();
						mCamera.release();
						if (cameraResolution.height > 1024
								&& cameraResolution.width > 1024) {
							Toast.makeText(getApplicationContext(),
									"Camera resolution must be decreased.",
									Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// Get our fileURI
							fileUri = getOutputMediaFile();
							intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
							startActivityForResult(intent, 100);
						}
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"There was an error with the camera.",
							Toast.LENGTH_LONG).show();
				}
			}
		});
		profile_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(profileIntent);
				
			}
		});

		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	private Uri getOutputMediaFile() throws IOException {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"DayTwentyNine");
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		if (mediaFile.exists() == false) {
			mediaFile.getParentFile().mkdirs();
			mediaFile.createNewFile();
		}
		return Uri.fromFile(mediaFile);
	}

	public void onClick(View v) {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {

				Matrix matrix = new Matrix();
				// rotate the Bitmap (there a problem with exif so we'll query the mediaStore for orientation
				Cursor cursor = getApplicationContext().getContentResolver().query(this.fileUri,
				      new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
				if (cursor !=null && cursor.getCount() == 1) {
				cursor.moveToFirst();
				    int orientation =  cursor.getInt(0);
				    matrix.preRotate(orientation);
				    }
				
				Intent newbookIntent = new Intent(this, AddBookActivity.class);
				newbookIntent.putExtra("newBookImageURI", fileUri.toString());

				startActivity(newbookIntent);

			}
		}
	}
}