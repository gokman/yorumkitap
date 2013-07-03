package com.bookworm.main;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraUser;

public class AddBookActivity extends ActivityBase implements OnClickListener{

	private Button addBookButton;
	private EditText bookName;
	private EditText bookDesc;
	private EditText bookWriter;
	private EditText bookTags;
	private Bitmap bitmap;
	private String imageURIString;
    private Uri uri;
    
    
	private static final Pattern TAG_PATTERN =   Pattern.compile("(?:^|\\s|[\\p{Punct}&&[^/]])(#[\\p{L}0-9-_]+)");

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.add_book);

        setExplore_button((ImageView)findViewById(R.id.explore_button));
		setHome_button((ImageView)findViewById(R.id.home_button));
		setAdd_book_button((ImageView)findViewById(R.id.add_button));
		setProfile_button((ImageView)findViewById(R.id.profile_button));
		setTimeline_button((ImageView)findViewById(R.id.timeline_button));

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        
    	addBookButton = (Button)findViewById(R.id.btnAddBook);
    	NetmeraClient.init(this, apiKey);
		
    	Intent myIntent = getIntent();
        imageURIString = myIntent.getStringExtra("newBookImageURI");
        uri = Uri.parse(imageURIString);
        ImageView tempImageView = (ImageView) findViewById(R.id.newbookImg);
//			tempImageView.setImageBitmap(bitmap);
//			imageLoader.DisplayImage(uri.toURL().toString(), tempImageView);
		tempImageView.setImageURI(uri);
		addBookButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//TODO Form validation is needed.
				
				bookName = (EditText)findViewById(R.id.book_name);
				bookDesc = (EditText)findViewById(R.id.sh_description);
				bookWriter=(EditText)findViewById(R.id.writer);
				bookTags = (EditText)findViewById(R.id.tags);
				
				try {
		        
					bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        
		    		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    		bitmap.compress(CompressFormat.PNG, 0, bos);
		    		bos.toByteArray();

		    		NetmeraMedia coverPhoto = new NetmeraMedia(bos.toByteArray());
				
			    try {
			    	String bookAdderId = NetmeraUser.getCurrentUser().getEmail();
				    NetmeraContent book = new NetmeraContent(ApplicationConstants.book);
				    book.add(ApplicationConstants.book_name, bookName.getText().toString());
				    book.add(ApplicationConstants.book_desc, bookDesc.getText().toString());
				    book.add(ApplicationConstants.book_tags, bookTags.getText().toString());
				    book.add(ApplicationConstants.book_writer, bookWriter.getText().toString());
				    book.add(ApplicationConstants.book_adderId,bookAdderId);
				    book.add(ApplicationConstants.book_coverPhoto,coverPhoto);

			    	new InsertDataTask().execute(book).get();
			    	/*
			    	 * Hashtag insertion
			    	 */
			    	String bookTagsText = bookTags.getText().toString();
			    	Matcher matcher = TAG_PATTERN.matcher(bookTagsText);
			    	while(matcher.find()) {
			    	    NetmeraContent tag = new NetmeraContent(ApplicationConstants.hashtable);
			    	    tag.add(ApplicationConstants.hashtable_book_adder_id, bookAdderId);
			    	    tag.add(ApplicationConstants.hashtable_book_title, bookName.getText().toString());
			    	    tag.add(ApplicationConstants.hashtable_tag, matcher.group(0).toString());
			    	    tag.add(ApplicationConstants.hashtable_book_path,book.getPath());
			    	    
			    	    new InsertDataTask().execute(tag).get();
			    	}
			    	/*kitap adinda gecen kelimeleri de tag olarak aliyoruz.*/
			    	matcher = TAG_PATTERN.matcher(bookName.getText().toString());
			    	while(matcher.find()){
			    	    NetmeraContent tag = new NetmeraContent(ApplicationConstants.hashtable);
			    	    tag.add(ApplicationConstants.hashtable_book_adder_id, bookAdderId);
			    	    tag.add(ApplicationConstants.hashtable_book_title, bookName.getText().toString());
			    	    tag.add(ApplicationConstants.hashtable_tag, matcher.group(0).toString());
			    		
			    	}
			    	
			    	NetmeraService service = new NetmeraService(ApplicationConstants.book);
			    	service.whereEqual(ApplicationConstants.book_adderId,NetmeraUser.getCurrentUser().getEmail());
			    	service.whereEqual(ApplicationConstants.book_name,bookName.getText().toString());
			    	List<NetmeraContent> bookList = new SelectDataTask().execute(service).get();
			    	NetmeraContent addedBook = bookList.get(0);
			    	
			    	Intent bookDetailIntent = new Intent(getApplicationContext(), BookDetailActivity.class);
			    	bookDetailIntent.putExtra(ApplicationConstants.book_name, addedBook.getString(ApplicationConstants.book_name));
			    	bookDetailIntent.putExtra(ApplicationConstants.book_adderId, addedBook.getString(ApplicationConstants.book_adderId));
			    	startActivity(bookDetailIntent);
			    	
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (NetmeraException e) {
					e.printStackTrace();
				}
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
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()){
    		case R.id.menu_about:
    			return true;
    		case R.id.menu_add_book:
    			return true;
    		default:
    			super.onContextItemSelected(item);    			
    			return true;
    		
    	}
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