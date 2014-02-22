package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_ACTION;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_COMMENT;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_ADD;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_COMMENTS;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.CommentAdapter;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Comment;
import com.bookworm.so.CommentSCR;
import com.bookworm.ws.action.AddActionHttpAsyncTask;
import com.bookworm.ws.comment.AddCommentWS;
import com.bookworm.ws.comment.ListCommentsWS;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraException;

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
		final Long book_id = myIntent.getLongExtra(ApplicationConstants.book_id,0);
		final Long adderID = myIntent.getLongExtra(ApplicationConstants.book_adderId,0);

		commentText = (EditText) findViewById(R.id.comment);
		addCommentButton = (Button)findViewById(R.id.btnAddComment);
		addCommentButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Form validation is needed.


				try {

					Comment comment = new Comment();
					comment.setCommentedBookId(book_id);
					comment.setCommentedBookAdderId(adderID);
					//TODO get Current User
					comment.setCommenterId(24L);
					comment.setCommentText(commentText.getText().toString());
					comment.setCreationDate(new Date());
					comment = new AddCommentWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"+WS_OPERATION_ADD,comment).get();

					// comment icin action kaydi olustur
					//TODO commenter userid will be replaced with 24 
					Action addBookAction = new Action(ActionType.ADD_COMMENT, 24L,comment.getCommentId()); 
				    addBookAction = new AddActionHttpAsyncTask().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_ADD,addBookAction).get();

					commentText.setText(getString(R.string.commentLabel));

					
					//List comments after last comment added.
					listComments(book_id, adderID);

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
			listComments(book_id, adderID);
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

	public void listComments(Long bookId, Long adderID) throws InterruptedException, ExecutionException, NetmeraException{
		//TODO list comments to show
		
		List<Comment> commentList = new ListCommentsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"
				+WS_OPERATION_LIST_COMMENTS+"/"+bookId).get();

		comments = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < commentList.size(); i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			CommentSCR commentScr = new CommentSCR(commentList.get(i));

			
			//TODO retrieve user
//			NetmeraService servicer = new NetmeraService(ApplicationConstants.user);
//			servicer.whereEqual(ApplicationConstants.user_email,tempComment1.get(ApplicationConstants.comment_er).toString());
//			List<NetmeraContent> usersList = new SelectDataTask(AddCommentActivity.this).execute(servicer).get();
//			NetmeraContent user = usersList.get(0);
//			user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);

//			String userProfileImageURLLeft = new GetNetmerMediaTask().execute(user).get();
//						
//
//			
//			map.put(KEY_COVER_LEFT, userProfileImageURLLeft);
			map.put(KEY_DESC_LEFT, commentScr.getComment().getCommentText());
			map.put(KEY_BOOK_ADDER_ID_LEFT, commentScr.getComment().getCommenterId().toString());
			

			
			CommentSCR commentSCRRight = null;
			if (i != commentList.size() - 1) {
				commentSCRRight = new CommentSCR(commentList.get(i + 1));
			}			
			if (commentSCRRight != null) {

				//TODO retrieve user
//				servicer = new NetmeraService(ApplicationConstants.user);
//				servicer.whereEqual(ApplicationConstants.user_email,tempComment2.get(ApplicationConstants.comment_er).toString());
//				usersList = new SelectDataTask(AddCommentActivity.this).execute(servicer).get();
//				user = usersList.get(0);
//				user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
////				String userProfileImageURLRight = new GetNetmerMediaTask().execute(user).get();
				

//				map.put(KEY_COVER_RIGHT, userProfileImageURLRight);
				map.put(KEY_DESC_RIGHT, commentScr.getComment().getCommentText());
				map.put(KEY_BOOK_ADDER_ID_RIGHT, commentScr.getComment().getCommenterId().toString());
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
