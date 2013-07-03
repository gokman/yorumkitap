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
	private ImageView profileImage;
	private Bitmap bitmap;
	private String coverPhotoUrl;
	private String profilePhotoUrl;
	public ImageLoader imageLoader;
	private ExpandableListView mExpandableListView;
	private List<GroupEntity> mGroupCollection;
	private EditText commentText;
	private Button addComment;
	private String book_name;
	private String adderID;
	private ExpandableListView commentsArena; // :))
	private LinearLayout profileLayout;
	private String currentUser;
	
	private Uri fileUri;
	
	private ImageView explore_button;
	private ImageView home_button;
	private ImageView add_book_button;
	private ImageView profile_button;
	
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
		commentText = (EditText) findViewById(R.id.comment);
		addComment = (Button) findViewById(R.id.btnAddComment);
		commentsArena = (ExpandableListView) findViewById(R.id.expandableListView);
		profileLayout = (LinearLayout) findViewById(R.id.profileLayout);
		
        setExplore_button((ImageView)findViewById(R.id.explore_button));
		setHome_button((ImageView)findViewById(R.id.home_button));
		setAdd_book_button((ImageView)findViewById(R.id.add_button));
		setProfile_button((ImageView)findViewById(R.id.profile_button));
		setTimeline_button((ImageView)findViewById(R.id.timeline_button));
		
		Intent myIntent = getIntent();
		book_name = myIntent.getStringExtra(ApplicationConstants.book_name);
		adderID = myIntent.getStringExtra(ApplicationConstants.book_adderId);


		NetmeraService service = new NetmeraService(ApplicationConstants.book);
		service.whereEqual(ApplicationConstants.book_name, book_name);
		service.whereEqual(ApplicationConstants.book_adderId, adderID);
		try {

			List<NetmeraContent> bookList = new SelectDataInnerTask().execute(
					service).get();
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
				List<NetmeraContent> usersList = new SelectDataTask().execute(userService).get();
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

				
				List<NetmeraContent> commentList= new SelectDataTask().execute(commentService).get();
				prepareResource(commentList);
				initComments();
				
				addComment.setOnClickListener(new View.OnClickListener() {

					public void onClick(View v) {
						try {

							NetmeraContent comment = new NetmeraContent(
									ApplicationConstants.comment);
							comment.add(ApplicationConstants.comment_edBook,
									book_name);
							comment.add(
									ApplicationConstants.comment_edBookOwner,
									adderID);
							comment.add(ApplicationConstants.comment_er,
									NetmeraUser.getCurrentUser().getEmail());
							comment.add(ApplicationConstants.comment_text,
									commentText.getText().toString());
							new InsertDataTask().execute(comment).get();
							
							commentText.setText(ApplicationConstants.comment_write_comment);

							
							NetmeraService commentService = new NetmeraService(ApplicationConstants.comment);
							commentService.whereEqual(ApplicationConstants.comment_edBook, book_name);
							commentService.whereEqual(ApplicationConstants.comment_edBookOwner, adderID);
							commentService.setSortOrder(SortOrder.ascending);
							commentService.setSortBy(ApplicationConstants.comment_create_date);
							
							List<NetmeraContent> commentList= new SelectDataTask().execute(commentService).get();
							prepareResource(commentList);
							initComments();
														
							commentsArena.expandGroup(0);
							
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

	private void initComments() {
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		ExpandableListAdapter adapter = new ExpandableListAdapter(this,
				mExpandableListView, mGroupCollection);
		mExpandableListView.setAdapter(adapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
	}
	
}
