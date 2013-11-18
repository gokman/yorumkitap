package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.DeletetDataTask;
import com.bookworm.common.ExpandableListAdapter;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.GroupEntity;
import com.bookworm.common.GroupEntity.GroupItemEntity;
import com.bookworm.common.ImageLoader;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.SelectDataTask;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraService.SortOrder;
import com.netmera.mobile.NetmeraUser;

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
	private Bitmap bitmap;
	private String coverPhotoUrl;
	private String profilePhotoUrl;
	public ImageLoader imageLoader;
	private ExpandableListView mExpandableListView;
	private List<GroupEntity> mGroupCollection;
	private TextView likeBook;
	private String book_name;
	private String adderID;
	private ExpandableListView commentsArena; // :))
	private LinearLayout profileLayout;
	private String currentUser;

	
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
		book_name = myIntent.getStringExtra(ApplicationConstants.book_name);
		adderID = myIntent.getStringExtra(ApplicationConstants.book_adderId);

		NetmeraService service = new NetmeraService(ApplicationConstants.book);
		service.whereEqual(ApplicationConstants.book_name, book_name);
		service.whereEqual(ApplicationConstants.book_adderId, adderID);
		
		NetmeraService bookLikeService = new NetmeraService(ApplicationConstants.bookLike);
		//bookLikeService.whereEqual(ApplicationConstants.bookLike_bookID,book_name );
		try {
			bookLikeService.whereEqual(ApplicationConstants.bookLike_r_id, NetmeraUser.getCurrentUser().getEmail().toString());
		} catch (NetmeraException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		//likeBook.setText(book_name, null);
		try{
		List<NetmeraContent> bookLikeList = new SelectDataInnerTask().execute(bookLikeService).get();
			if (bookLikeList.size() > 0){	//Hali haz�rda be�enmi� eleman.. 
				NetmeraContent bookLikeElement = bookLikeList.get(0);
				
				likeBook.setText(bookLikeElement.get(ApplicationConstants.bookLike_bookID).toString(), null);
			}
		}catch (Exception e) {
			// TODO: handle exception
		} 
		
		
		
		try {

			List<NetmeraContent> bookList = new SelectDataInnerTask().execute(service).get();
			if (bookList.size() == 1) {
				NetmeraContent book = bookList.get(0);

				bookTitle.setText(book.get(ApplicationConstants.book_name)
						.toString());
				bookWriter.setText(book.get(ApplicationConstants.book_writer)
						.toString());
				bookDescription.setText(book
						.get(ApplicationConstants.book_desc).toString());
				bookTags.setText(book.get(ApplicationConstants.book_tags)
						.toString());
				userName.setText(adderID);

				currentUser = NetmeraUser.getCurrentUser().getEmail().toString();
				profileLayout.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {

						 Intent profileIntent = new Intent(v.getContext(),ProfileActivity.class);
						 profileIntent.putExtra(ApplicationConstants.userEmailParam,adderID );
						 startActivity(profileIntent);
						
					}
				});
				// bookCover.setImageBitmap(bitmap);

				NetmeraService userService = new NetmeraService(ApplicationConstants.user);
				userService.whereEqual(ApplicationConstants.user_email, adderID);
				List<NetmeraContent> usersList = new SelectDataTask(BookDetailActivity.this).execute(userService).get();
				NetmeraContent user = usersList.get(0);
				user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
				profilePhotoUrl  = new GetNetmerMediaTask().execute(user).get();
				
				
				NetmeraService commentService = new NetmeraService(ApplicationConstants.comment);
				commentService.whereEqual(ApplicationConstants.comment_edBook, book_name);
				commentService.whereEqual(ApplicationConstants.comment_edBookOwner, adderID);
				commentService.setSortOrder(SortOrder.ascending);
				commentService.setSortBy(ApplicationConstants.comment_create_date);
				imageLoader.DisplayImage(coverPhotoUrl, bookCover);
				imageLoader.DisplayImage(profilePhotoUrl, profileImage);

				
				List<NetmeraContent> commentList= new SelectDataTask(BookDetailActivity.this).execute(commentService).get();
				prepareResource(commentList);
//				initComments();
				
				commentImage.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						
				         Intent addCommentIntent = new Intent(getApplicationContext(),AddCommentActivity.class);
				   		 addCommentIntent.putExtra(ApplicationConstants.book_name,book_name);
				   		 addCommentIntent.putExtra(ApplicationConstants.book_adderId,adderID);
				   		 
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
						
						
							try {
							String	bookLikerId = NetmeraUser.getCurrentUser().getEmail();
							
								
							
						    NetmeraContent bookLike = new NetmeraContent(ApplicationConstants.bookLike);
						    bookLike.add(ApplicationConstants.bookLike_id, "1".toString());
						    bookLike.add(ApplicationConstants.bookLike_r_id, bookLikerId);
						    bookLike.add(ApplicationConstants.bookLike_bookID, bookTitle.getText().toString());
						    bookLike.add(ApplicationConstants.bookLike_date, new Date());
						   
						    
					    	new InsertDataTask().execute(bookLike).get();
					    	
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
						
						}else{
							
							try {
								String	bookLikerId = NetmeraUser.getCurrentUser().getEmail();
								NetmeraContent bookLikeDelete = new NetmeraContent(ApplicationConstants.bookLike);
								bookLikeDelete.add(ApplicationConstants.bookLike_r_id, bookLikerId);								
								
								new DeletetDataTask().execute(bookLikeDelete).get();
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
								catch (NetmeraException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							
						}
					}
				});

			}

			setNavigationButtons();
			
		} catch (NetmeraException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

	private class SelectDataInnerTask extends
			AsyncTask<NetmeraService, Void, List<NetmeraContent>> {

		// can use UI thread here
		protected void onPreExecute() {
		}

		// automatically done on worker thread (separate from UI thread)
		protected List<NetmeraContent> doInBackground(
				final NetmeraService... args) {
			NetmeraService service = null;
			List<NetmeraContent> searchResultList = null;
			if (args.length == 1)
				service = args[0];

			if (service != null) {
				try {
					searchResultList = service.search();
					if (searchResultList != null
							&& searchResultList.size() == 1) {
						NetmeraContent content = searchResultList.get(0);
						NetmeraMedia netPhoto;

						netPhoto = content
								.getNetmeraMedia(ApplicationConstants.book_coverPhoto);
						BookDetailActivity.this.coverPhotoUrl = netPhoto
								.getUrl(NetmeraMedia.PhotoSize.LARGE);

					}
				} catch (NetmeraException e) {
					e.printStackTrace();

				}
			}
			return searchResultList;
		}

		// can use UI thread here
		protected void onPostExecute(final List<NetmeraContent> result) {

		}
	}

	private void prepareResource(List <NetmeraContent> commentList) throws NetmeraException {

		mGroupCollection = new ArrayList<GroupEntity>();

		GroupEntity ge = new GroupEntity();
		ge.Name = "Yorumlar";

		for (NetmeraContent comment : commentList) {
			GroupItemEntity gi = ge.new GroupItemEntity();
			gi.Name = comment.getString(ApplicationConstants.comment_text);
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
