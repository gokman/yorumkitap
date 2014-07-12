package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_ACTION;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOKLIKE;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_COMMENT;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_ADD;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_DELETE;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_COMMENTS;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.CommentAdapter;
import com.bookworm.custom.object.CustomComment;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Comment;
import com.bookworm.so.CommentSCR;
import com.bookworm.ws.action.AddActionWS;
import com.bookworm.ws.comment.AddCommentWS;
import com.bookworm.ws.comment.DeleteCommentActionWS;
import com.bookworm.ws.comment.ListCommentsWS;

public class AddCommentActivity extends ActivityBase implements OnClickListener {

	private EditText commentText;
	private ListView commentsListView;
	private CommentAdapter adapter;
	private ArrayList<HashMap<String, String>> comments;
	//popup window and its elements
	private PopupWindow pwindo;
	private Button popupDeleteCommentButton;
	private Button popupGoProfileButton;
	
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
		commentsListView = (ListView) findViewById(R.id.comments_on_book);
		
          commentsListView.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long id) {
				
				final TextView commentIdElement=(TextView)view.findViewById(R.id.comment_id);
				final TextView commenterIdElement=(TextView)view.findViewById(R.id.comment_owner_left);
				
				if(isCommenterandUserSame(Long.parseLong(commenterIdElement.getText().toString()), 
						                   ApplicationConstants.signed_in_userid)){
				 LayoutInflater inflater = (LayoutInflater) AddCommentActivity.this
						 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						 View popupLayout = inflater.inflate(R.layout.delete_comment_popup,
						 (ViewGroup) findViewById(R.id.popup_comment_layout));
						 pwindo = new PopupWindow(popupLayout, WindowManager.LayoutParams.MATCH_PARENT, 
								 WindowManager.LayoutParams.WRAP_CONTENT, true);
						 pwindo.setOutsideTouchable(true);
						 pwindo.setBackgroundDrawable(new BitmapDrawable());
						 
						//click outside and close popup
							 pwindo.setTouchInterceptor(new OnTouchListener() {
								  
						            public boolean onTouch(View v, MotionEvent event)
						            {
						                if(event.getAction() == MotionEvent.ACTION_OUTSIDE)
						                {
						                    pwindo.dismiss();
						                    return true;
						                }
						                return false;
						            }
						 
						    });	 
							 
						pwindo.showAtLocation(popupLayout, Gravity.CENTER, 0, 0);
						
						popupDeleteCommentButton=(Button)popupLayout.findViewById(R.id.popup_delete_comment_button);
						popupGoProfileButton=(Button)popupLayout.findViewById(R.id.popup_profile_button);
						
						
						  popupDeleteCommentButton.setOnClickListener(new OnClickListener() 
				          { 
				              
				              public void onClick(View v) 
				              { 
				            	  //go web service and delete comment
				            	  Comment  comment = new Comment();
								    comment.setCommentId(Long.parseLong(commentIdElement.getText().toString()));
								    try {
										 new DeleteCommentActionWS().
												        execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"+WS_OPERATION_DELETE,
												        		comment).get();
									} catch (InterruptedException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} catch (ExecutionException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
										    //close popup
										    pwindo.dismiss();
										    try {
												listComments(book_id,adderID);
											} catch (InterruptedException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											} catch (ExecutionException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}
				              } 
				          }); 
						  
							  popupGoProfileButton.setOnClickListener(new OnClickListener() {
								
								public void onClick(View v) {
									 Intent profileIntent = new Intent(v.getContext(),ProfileActivity.class);
									 profileIntent.putExtra(ApplicationConstants.userEmailParam,
											                Long.parseLong(commenterIdElement.getText().toString()));
									 startActivity(profileIntent);
								}
							  });
						
				}			 
				// TODO Auto-generated method stub
				return false;
			
			}
		});
          
          
        
         
		
			commentText.setOnKeyListener(new View.OnKeyListener() {
			    public boolean onKey(View v, int keyCode, KeyEvent event) {
			        if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {	
			        	
				try {

					Comment comment = new Comment();
					comment.setCommentedBookId(book_id);
					comment.setCommentedBookAdderId(adderID);
					comment.setCommenterId(ApplicationConstants.signed_in_userid);
					comment.setCommentText(commentText.getText().toString());
					comment.setCreationDate(ApplicationConstants.currentDateString);
					comment = new AddCommentWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"+WS_OPERATION_ADD,comment).get();
				    
				    commentText.setText(null);
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
				    return true;
			        }
			        return false;
			         
			    }
		});
		
		try {
			listComments(book_id, adderID);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e){
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
			map.put(ApplicationConstants.TYPE_COMMENT_ID, commentScr.getComment().getCommentId().toString());

			comments.add(map);
		}		
			
		adapter = new CommentAdapter(this, comments);
		adapter.notifyDataSetChanged();
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
	
	/* yorumun üzerine uzun basan kullanıcının o yorumun sahibi olup olmadığını kontrol eder
	eğer yorum kullanıcıya ait değil ise uzun basma sonucunda hiçbir şey olmayacak. izni olmamalı yani*/
	public boolean isCommenterandUserSame(Long commenterId,Long userId){
		if(commenterId==userId){
			return true;
		}else{
			return false;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}
