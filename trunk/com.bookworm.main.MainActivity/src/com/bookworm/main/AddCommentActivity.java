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
import com.bookworm.custom.object.CustomComment;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Comment;
import com.bookworm.so.CommentSCR;
import com.bookworm.ws.action.AddActionWS;
import com.bookworm.ws.comment.AddCommentWS;
import com.bookworm.ws.comment.ListCommentsWS;

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
					comment.setCreationDate(ApplicationConstants.dateFormat.format(new Date()));
					comment = new AddCommentWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"+WS_OPERATION_ADD,comment).get();

					// comment icin action kaydi olustur
					//TODO commenter userid will be replaced with 24 
					Action addBookAction = new Action(ActionType.ADD_COMMENT.asCode(), 24L,comment.getCommentId()); 
				    addBookAction = new AddActionWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_ADD,
				    		addBookAction,
				    		ApplicationConstants.signed_in_email,
				    		ApplicationConstants.signed_in_password
				    		).get();

					commentText.setText(getString(R.string.commentLabel));
					
					//List comments after last comment added.
					listComments(book_id, adderID);

					//TODO call list methods to show.
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
		}

	}

	public void listComments(Long bookId, Long adderID) throws InterruptedException, ExecutionException{
		
		List<CustomComment> commentList = new ListCommentsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"
				+WS_OPERATION_LIST_COMMENTS+"/"+bookId).get();

		comments = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < commentList.size(); i ++) {
			HashMap<String, String> map = new HashMap<String, String>();
			CommentSCR commentScr = new CommentSCR(commentList.get(i));

			map.put(KEY_DESC_LEFT, commentScr.getComment().getCommentText());
			map.put(KEY_BOOK_ADDER_ID_LEFT, commentScr.getComment().getCommenterId().toString());
			map.put(KEY_BOOK_ADDER_NAME_LEFT, commentScr.getComment().getCommenterName().toString());

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
