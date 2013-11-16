package com.bookworm.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.CommentAdapter;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraUser;
import com.netmera.mobile.NetmeraService.SortOrder;

public class AddCommentActivity extends ActivityBase implements OnClickListener {

	private Button addCommentButton;
	private EditText commentText;
	private ListView commentsListView;
	private CommentAdapter adapter;
	private ArrayList<HashMap<String, String>> comments;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.add_comment);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);
		NetmeraClient.init(this, apiKey);
		Intent myIntent = getIntent();
		final String book_name = myIntent.getStringExtra(ApplicationConstants.book_name);
		final String adderID = myIntent.getStringExtra(ApplicationConstants.book_adderId);

		commentText = (EditText) findViewById(R.id.comment);
		addCommentButton = (Button)findViewById(R.id.btnAddComment);
		addCommentButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Form validation is needed.


				try {

					NetmeraContent comment = new NetmeraContent(
							ApplicationConstants.comment);
					comment.add(ApplicationConstants.comment_edBook, book_name);
					comment.add(ApplicationConstants.comment_edBookOwner,
							adderID);
					comment.add(ApplicationConstants.comment_er, NetmeraUser
							.getCurrentUser().getEmail());
					comment.add(ApplicationConstants.comment_text, commentText.getText().toString());
					new InsertDataTask().execute(comment).get();

					// comment icin action kaydi olustur
					NetmeraContent action = new NetmeraContent(
							ApplicationConstants.action);
					action.add(ApplicationConstants.ACTION_TYPE,
							ApplicationConstants.ACTION_TYPE_COMMENT);
					action.add(ApplicationConstants.action_comment_edBook,
							book_name);
					action.add(ApplicationConstants.action_comment_edBookOwner,
							adderID);
					action.add(ApplicationConstants.action_comment_er,
							NetmeraUser.getCurrentUser().getEmail());
					action.add(ApplicationConstants.action_comment_text,
							commentText.getText().toString());
					action.add(ApplicationConstants.ACTION_OWNER, NetmeraUser
							.getCurrentUser().getEmail());
					new InsertDataTask().execute(action).get();

					commentText
							.setText(getString(R.string.commentLabel));

					NetmeraService commentService = new NetmeraService(
							ApplicationConstants.comment);
					commentService.whereEqual(
							ApplicationConstants.comment_edBook, book_name);
					commentService.whereEqual(
							ApplicationConstants.comment_edBookOwner, adderID);
					commentService.setSortOrder(SortOrder.ascending);
					commentService
							.setSortBy(ApplicationConstants.comment_create_date);


					//TODO call list methods to show.
				} catch (NetmeraException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		commentsListView = (ListView) findViewById(R.id.comments_on_book);
		try {
			listComments(book_name, adderID);
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

	}

	public void listComments(String book_name, String adderID) throws InterruptedException, ExecutionException, NetmeraException{
		//TODO list comments to show
		
		NetmeraService commentService = new NetmeraService(ApplicationConstants.comment);
		commentService.whereEqual(ApplicationConstants.comment_edBook, book_name);
		commentService.whereEqual(ApplicationConstants.comment_edBookOwner, adderID);
		commentService.setSortOrder(SortOrder.ascending);
		commentService.setSortBy(ApplicationConstants.comment_create_date);
		
		List<NetmeraContent> commentList= new SelectDataTask(AddCommentActivity.this).execute(commentService).get();

		comments = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < commentList.size(); i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			NetmeraContent tempComment1 = commentList.get(i);
//			String tem= tempBook1.get("Path").toString();
//			String tem2 = tempBook1.get("path").toString();
			tempComment1.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);

			NetmeraService servicer = new NetmeraService(ApplicationConstants.user);
			servicer.whereEqual(ApplicationConstants.user_email,tempComment1.get(ApplicationConstants.comment_er).toString());
			List<NetmeraContent> usersList = new SelectDataTask(AddCommentActivity.this).execute(servicer).get();
			NetmeraContent user = usersList.get(0);
			user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);

//			String userProfileImageURLLeft = new GetNetmerMediaTask().execute(user).get();
//						
//
//			
//			map.put(KEY_COVER_LEFT, userProfileImageURLLeft);
			map.put(KEY_DESC_LEFT, tempComment1.get(ApplicationConstants.comment_text).toString());
			map.put(KEY_BOOK_ADDER_ID_LEFT, tempComment1.get(ApplicationConstants.comment_er).toString());
			

			
			NetmeraContent tempComment2 = null;
			if (i != commentList.size() - 1) {
				tempComment2 = commentList.get(i + 1);
				tempComment2.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
			}			
			if (tempComment2 != null) {

				servicer = new NetmeraService(ApplicationConstants.user);
				servicer.whereEqual(ApplicationConstants.user_email,tempComment2.get(ApplicationConstants.comment_er).toString());
				usersList = new SelectDataTask(AddCommentActivity.this).execute(servicer).get();
				user = usersList.get(0);
				user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
//				String userProfileImageURLRight = new GetNetmerMediaTask().execute(user).get();
				

//				map.put(KEY_COVER_RIGHT, userProfileImageURLRight);
				map.put(KEY_DESC_RIGHT, tempComment2.get(ApplicationConstants.comment_text).toString());
				map.put(KEY_BOOK_ADDER_ID_RIGHT, tempComment2.get(ApplicationConstants.comment_er).toString());
			}

			comments.add(map);
		}		
		
		
		adapter = new CommentAdapter(this, comments);
		commentsListView.setAdapter(adapter);

		
		

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			return true;
		case R.id.menu_add_book:
			return true;
		default:
			super.onContextItemSelected(item);
			return true;

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
