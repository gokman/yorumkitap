package com.bookworm.main;


import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOK;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_ACTION;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_ADD;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
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

import com.bookworm.common.ApplicationConstants;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Book;
import com.bookworm.ws.action.AddActionHttpAsyncTask;
import com.bookworm.ws.book.AddBookWS;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraMedia;

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
			    	//TODO adderid will be removed.
			    	Long bookAdderId = 24L;//NetmeraUser.getCurrentUser().getEmail();
				    Book book = new Book(bookName.getText().toString(),
				    						bookDesc.getText().toString(),
				    						bookAdderId,
				    						bookWriter.getText().toString(),
				    						null);
				    //TODO
//				    book.add(ApplicationConstants.book_tags, bookTags.getText().toString());
				    book = new AddBookWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_ADD,book).get();


//				    
//				    //TODO action tablo kaydı
				    Action addBookAction = new Action(ActionType.ADD_BOOK, 24); 
				    addBookAction = new AddActionHttpAsyncTask().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_ADD,addBookAction).get();

//			    	/* TODO
//			    	 * Hashtag insertion
//			    	 */
//			    	String bookTagsText = bookTags.getText().toString();
//			    	Matcher matcher = TAG_PATTERN.matcher(bookTagsText);
//			    	while(matcher.find()) {
//			    	    NetmeraContent tag = new NetmeraContent(ApplicationConstants.hashtable);
//			    	    tag.add(ApplicationConstants.hashtable_book_adder_id, bookAdderId);
//			    	    tag.add(ApplicationConstants.hashtable_book_title, bookName.getText().toString());
//			    	    tag.add(ApplicationConstants.hashtable_tag, matcher.group(0).toString());
//			    	    tag.add(ApplicationConstants.hashtable_book_path,book.getPath());
//			    	    
//			    	    new InsertDataTask().execute(tag).get();
//			    	}
//			    	/*kitap adinda gecen kelimeleri de tag olarak aliyoruz.*/
//			    	matcher = TAG_PATTERN.matcher(bookName.getText().toString());
//			    	while(matcher.find()){
//			    	    NetmeraContent tag = new NetmeraContent(ApplicationConstants.hashtable);
//			    	    tag.add(ApplicationConstants.hashtable_book_adder_id, bookAdderId);
//			    	    tag.add(ApplicationConstants.hashtable_book_title, bookName.getText().toString());
//			    	    tag.add(ApplicationConstants.hashtable_tag, matcher.group(0).toString());
//			    		
//			    	}
//			    	
			    	
			    	Intent bookDetailIntent = new Intent(getApplicationContext(), BookDetailActivity.class);
			    	bookDetailIntent.putExtra(ApplicationConstants.book_id, book.getBookId());
			    	bookDetailIntent.putExtra(ApplicationConstants.book_adderId, book.getAdderId());
			    	startActivity(bookDetailIntent);
			    	
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
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