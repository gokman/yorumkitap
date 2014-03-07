package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOK;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOKLIKE;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_COMMENT;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_HASHTAG;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_ADD;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_GET_BY_ID;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_COMMENTS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_LIKES;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GroupEntity;
import com.bookworm.common.GroupEntity.GroupItemEntity;
import com.bookworm.common.ImageLoader;
import com.bookworm.model.Book;
import com.bookworm.model.BookLike;
import com.bookworm.model.Comment;
import com.bookworm.model.Hashtag;
import com.bookworm.ws.book.GetBookInfoWS;
import com.bookworm.ws.booklike.AddBookLikeActionWS;
import com.bookworm.ws.comment.ListCommentsWS;
import com.bookworm.ws.hashtag.ListHashtagsWS;
import com.bookworm.ws.like.GetBookLikeWS;

public class BookDetailActivity extends ActivityBase implements OnClickListener {

	private TextView bookTitle;
	private TextView bookWriter;
	private TextView bookDescription;
	private TextView bookTags;
	private TextView userName;
	private ImageView bookCover;
	private ImageView likeImage;
	private ImageView commentImage;
	private ImageView profileImage;
	private String coverPhotoUrl;
	private String profilePhotoUrl;
	public ImageLoader imageLoader;
	private List<GroupEntity> mGroupCollection;
	private TextView likeBook;
	private Long bookId;
	private Long adderId;
	private LinearLayout profileLayout;
	private String currentUser;
	Book addedBook; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.book_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.window_title);

		imageLoader = new ImageLoader(this.getApplicationContext());
		bookTitle = (TextView) findViewById(R.id.bookNameText);
		bookWriter = (TextView) findViewById(R.id.writer);
		bookDescription = (TextView) findViewById(R.id.sh_description);
		bookTags = (TextView) findViewById(R.id.tags);
		bookCover = (ImageView) findViewById(R.id.newbookImg);
		profileImage = (ImageView) findViewById(R.id.profileImg);
		userName = (TextView) findViewById(R.id.userName);
		likeImage = (ImageView) findViewById(R.id.like_button);
		commentImage = (ImageView)findViewById(R.id.comment_button);
		profileLayout = (LinearLayout) findViewById(R.id.profileLayout);
		likeBook = (TextView) findViewById(R.id.btnLikeBook);
		
		Intent myIntent = getIntent();
		bookId = myIntent.getLongExtra((ApplicationConstants.book_id), 0);
		adderId = myIntent.getLongExtra(ApplicationConstants.book_adderId,0);
		List<BookLike> likes = null;

		try {
			likes = new GetBookLikeWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST_LIKES+"/"+adderId+"/"+bookId).get();
			if (likes !=null && likes.size() > 0){ 
				BookLike like  = likes.get(0);
				//TODO burda napiliyo anlamad�m. fyesildal
				likeBook.setText(like.getBookId().toString());
			}
			addedBook  = new GetBookInfoWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_GET_BY_ID+"/"+bookId).get();
	
			if (addedBook !=null) {
				bookTitle.setText(addedBook.getName());
				bookWriter.setText(addedBook.getWriter());
				bookDescription.setText(addedBook.getDescription());
	
				List<Hashtag> tags = new ListHashtagsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_HASHTAG+"/"+WS_OPERATION_LIST +"/"+bookId).get();
				String tagString = ApplicationConstants.EMPTY_STRING;
				for(Hashtag tag : tags){
					tagString += ApplicationConstants.SPACE + tag.getTag();
				}
				bookTags.setText(tagString);
				userName.setText("f2");
				//TODO current user
				profileLayout.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						 Intent profileIntent = new Intent(v.getContext(),ProfileActivity.class);
						 profileIntent.putExtra(ApplicationConstants.userEmailParam,addedBook.getAdderId());
						 startActivity(profileIntent);
						
					}
				});
	//				 bookCover.setImageBitmap(bitmap);
	
	//			NetmeraService userService = new NetmeraService(ApplicationConstants.user);
	//			userService.whereEqual(ApplicationConstants.user_email, "f2@f2.com");
	//			List<NetmeraContent> usersList = new SelectDataTask(BookDetailActivity.this).execute(userService).get();
	//			NetmeraContent user = usersList.get(0);
	//			user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
	//			profilePhotoUrl  = new GetNetmerMediaTask().execute(user).get();
				
				List<Comment> comments = new ListCommentsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"
						+WS_OPERATION_LIST_COMMENTS+"/"+bookId).get();
	
				imageLoader.DisplayImage(coverPhotoUrl, bookCover);
				imageLoader.DisplayImage(profilePhotoUrl, profileImage);
	
				prepareResource(comments);
	//				initComments();
				
				commentImage.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
				         Intent addCommentIntent = new Intent(getApplicationContext(),AddCommentActivity.class);
				   		 addCommentIntent.putExtra(ApplicationConstants.book_id,addedBook.getBookId());
				   		 addCommentIntent.putExtra(ApplicationConstants.book_adderId,addedBook.getAdderId());
						 v.getContext().startActivity(addCommentIntent);
					}
				});
				
				likeImage.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						//TODO like event
						
						
					}
				});
				
				
				likeBook.setOnClickListener(new View.OnClickListener() {
	
					public void onClick(View v) {
	
						if (likeBook.getText().toString().equals("Be�en")) {
							likeBook.setText("Be�enmekten Vazge�", null);		
							BookLike  bookLike = new BookLike();
							bookLike.setBookLikeDate(new Date());
						    bookLike.setBookId(addedBook.getBookId());
						    bookLike.setBookLikerId(1L);
						    try {
								bookLike = new AddBookLikeActionWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOKLIKE+"/"+WS_OPERATION_ADD,bookLike).get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							//TODO unlike
						}
					}
				});
	
			}
	
				setNavigationButtons();
		} catch (InterruptedException e2) {
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			e2.printStackTrace();
		}
			

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}


	private void prepareResource(List <Comment> commentList){

		mGroupCollection = new ArrayList<GroupEntity>();

		GroupEntity ge = new GroupEntity();
		ge.Name = "Yorumlar";

		for (Comment comment : commentList) {
			GroupItemEntity gi = ge.new GroupItemEntity();
			gi.Name = comment.getCommentText();
			ge.GroupItemCollection.add(gi);
		}
		mGroupCollection.add(ge);
	}

//	private void initComments() {
//		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
//		ExpandableListAdapter adapter = new ExpandableListAdapter(this,
//				mExpandableListView, mGroupCollection);
//		mExpandableListView.setAdapter(adapter);
//	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
	}
	
}
