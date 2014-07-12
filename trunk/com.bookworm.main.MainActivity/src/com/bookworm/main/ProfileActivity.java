package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_ACTION;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOK;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_FOLLOWSHIP;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_ADD;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_DELETE;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_COMMENTED_BOOKS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST_FOLLOWSHIPS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_UNFOLLOW;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_UPDATE;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.ImageLoader;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Book;
import com.bookworm.model.Followship;
import com.bookworm.model.User;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.action.AddActionWS;
import com.bookworm.ws.action.DeleteActionWS;
import com.bookworm.ws.book.GetCommentedBooksWS;
import com.bookworm.ws.book.ListBooksWS;
import com.bookworm.ws.followship.AddFollowshipWS;
import com.bookworm.ws.followship.CheckFollowshipWS;
import com.bookworm.ws.followship.UnfollowWS;
import com.bookworm.ws.user.ListUsersWS;
import com.bookworm.ws.user.UpdateUserWS;

public class ProfileActivity extends ActivityBase implements OnClickListener{

	private Long profileUserId;
	private Long currentUserId;
	private TextView txtBookCount;
	private TextView txtFollowingsCount;
	private TextView txtFollowersCount;
	private TextView txtBooksText;
	private TextView txtFollowingsText;
	private TextView txtFollowersText;
	private ImageView imgProfileImage;
	private Button btnAddedBooks;
	private Button btnCommentedBooks;
	private ImageView statusView;

	private Long followshipStatus; 
	public ImageLoader imageLoader;
	private final int ACTIVITY_CHOOSE_PHOTO = 41;
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.profile_page);
	        
	        imageLoader=new ImageLoader(this.getApplicationContext());

			statusView = (ImageView)findViewById(R.id.status);
			
	        preparelistItems();
	        //TODO listeleme sayfalar�ndan biriyle gelinirse  userEmail sessiondan alinmali.
			Intent myIntent = getIntent();
			profileUserId = myIntent.getLongExtra((ApplicationConstants.userEmailParam),0);
			
	        txtBookCount = (TextView)findViewById(R.id.booksCount);
	        txtFollowersCount = (TextView)findViewById(R.id.followerCount);
	        txtFollowingsCount= (TextView)findViewById(R.id.followingCount);

	        txtBooksText = (TextView)findViewById(R.id.books);
	        txtFollowersText = (TextView)findViewById(R.id.follower);
	        txtFollowingsText= (TextView)findViewById(R.id.following);	        
	        
	        imgProfileImage = (ImageView) findViewById(R.id.profileImage);
	        btnAddedBooks = (Button)findViewById(R.id.addedBooks);
	        btnCommentedBooks = (Button)findViewById(R.id.commentedBooks);

	        currentUserId = ApplicationConstants.signed_in_userid;
	        
	        if(profileUserId==null || profileUserId.longValue()==-1 || profileUserId.longValue()==0){
	        	profileUserId = currentUserId;
	        }
	        
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

			try {
		    	
				//profile sahibinin bilgileri
		    	SearchCriteria scForProfileUser = new SearchCriteria();
		    	List<Long> profileUserIdList = new ArrayList<Long>();
		    	profileUserIdList.add(profileUserId);
		    	scForProfileUser.setUserIdList(profileUserIdList);
				List<User> profileUser = new ListUsersWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LIST+"/",
						scForProfileUser,
						ApplicationConstants.signed_in_email,
						ApplicationConstants.signed_in_password
						).get();
				
				if(profileUser != null && profileUser.size()==1){
				      	User profUser = profileUser.get(0);
				        if(profUser.getProfileImage()!=null){

				        	Bitmap bm = BitmapFactory.decodeByteArray(profUser.getProfileImage(), 0, profUser.getProfileImage().length);
					        DisplayMetrics dm = new DisplayMetrics();
					        getWindowManager().getDefaultDisplay().getMetrics(dm);
	
					        imgProfileImage.setMinimumHeight(dm.heightPixels);
					        imgProfileImage.setMinimumWidth(dm.widthPixels);
					        imgProfileImage.setImageBitmap(bm);
				        }
				}
				//profil sahibinin ekledigi kitaplar
		    	SearchCriteria sc = new SearchCriteria();
		    	sc.setAdderId(profileUserId);
		    	
		    	final List<Book> booksOfUser = (List<Book>) new ListBooksWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST,
		    			sc,
		    			ApplicationConstants.signed_in_email,
		    			ApplicationConstants.signed_in_password
		    			).get();
				final Long addedBookCount = (long)booksOfUser.size();
	    
	    		//profil kullanicisinin takip ettikleri
	    		SearchCriteria fsc = new SearchCriteria();
	    		fsc.setFollowerId(profileUserId);
	    		
				final List <Followship> followingsList = (List<Followship>) new CheckFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_LIST_FOLLOWSHIPS+"/",
						fsc,
						ApplicationConstants.signed_in_email,
						ApplicationConstants.signed_in_password).get();

		    	final Long followingsCount = (long)followingsList.size();


	    		//profil kullanicisinin takipcileri
	    		SearchCriteria followerSc = new SearchCriteria();
	    		followerSc.setFollowingId(profileUserId);
	    		
				final List <Followship> followersList = (List<Followship>) new CheckFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_LIST_FOLLOWSHIPS+"/",
						followerSc,
						ApplicationConstants.signed_in_email,
						ApplicationConstants.signed_in_password).get();

		    	final Long followersCount = (long)followersList.size();

	    		SearchCriteria fCriteria = new SearchCriteria();
	    		fCriteria.setFollowerId(currentUserId);
	    		fCriteria.setFollowingId(profileUserId);

		    	final List <Followship> followList = (List<Followship>) new CheckFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_LIST_FOLLOWSHIPS+"/",
						fCriteria,
						ApplicationConstants.signed_in_email,
						ApplicationConstants.signed_in_password).get();

		    	
		    	if(currentUserId.longValue()==profileUserId.longValue()){
		    		//profil sayfasini goruntuleyen kullanici profilin sahibiyse edit iconu gosteriliyor.
		    		statusView.setImageResource(R.drawable.edit_icon);
		    	}else{
		    		//profil sayfasini goruntuleyen kullanici profilin sahibi degilse bu profili takip edip etmedigine bakiliyor.
		    		
	
					if(followList!=null && followList.size() > 0){
						statusView.setImageResource(R.drawable.following);
					}else{
						statusView.setImageResource(R.drawable.follow);
					}
		    		
		    	}
	    	statusView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramView) {
			    	if(currentUserId.longValue()== profileUserId.longValue()){
			    	 //TODO profile Edit page	
			         Intent bookDetailIntent = new Intent(getApplicationContext(),ProfileEditActivity.class);
			   		 bookDetailIntent.putExtra(ApplicationConstants.userEmailParam,profileUserId);
					 paramView.getContext().startActivity(bookDetailIntent);

			    	}else{
			    		SearchCriteria fCriteria = new SearchCriteria();
			    		fCriteria.setFollowerId(currentUserId);
			    		fCriteria.setFollowingId(profileUserId);

			    		List <Followship> infollowList = null;
				    	try {
							infollowList = (List<Followship>) new CheckFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_LIST_FOLLOWSHIPS+"/",
									fCriteria,
									ApplicationConstants.signed_in_email,
									ApplicationConstants.signed_in_password).get();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}			    		
			    		

			    		//profil kullanicisinin takipcileri
			    		SearchCriteria followerSc = new SearchCriteria();
			    		followerSc.setFollowingId(profileUserId);
			    		
						List<Followship> followers = new ArrayList<Followship>();
						try {
							followers = (List<Followship>) new CheckFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_LIST_FOLLOWSHIPS+"/",
									followerSc,
									ApplicationConstants.signed_in_email,
									ApplicationConstants.signed_in_password).get();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (ExecutionException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
 
			    		if(infollowList!=null && infollowList.size() > 0){ 						//Unfollow Action
							try {

								new UnfollowWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_UNFOLLOW+"/"+currentUserId+"/"+profileUserId).get();
								new DeleteActionWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_DELETE+"/"+currentUserId+"/"+profileUserId).get();								

								statusView.setImageResource(R.drawable.follow);	

//								follower sayisini simdilik +1 olarak degistiriyoruz.								
								txtFollowersCount.setText(new Long(followers.size()-1).toString());
//								
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						}else{											//Follow Action
							Followship fw = new Followship(currentUserId, profileUserId, new Date());

							try {
								fw = new AddFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"+WS_OPERATION_ADD,
										fw,
							    		ApplicationConstants.signed_in_email,
							    		ApplicationConstants.signed_in_password
							    		).get();								

							    Action addFollowAction = new Action(ActionType.FOLLOW.asCode(), currentUserId,profileUserId); 
							    addFollowAction = new AddActionWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_ADD,
							    		addFollowAction,
							    		ApplicationConstants.signed_in_email,
							    		ApplicationConstants.signed_in_password
							    		).get();								
								
								txtFollowersCount.setText(new Long(followers.size()+1).toString());
								
								statusView.setImageResource(R.drawable.following);	
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
			    		
			    	}
				}
			});
	    	
			txtBookCount.setText(addedBookCount.toString());
			txtFollowersCount.setText(followersCount.toString());
			txtFollowingsCount.setText(followingsCount.toString());
			applyDataToTable(booksOfUser);			
	    	

			
			
			View.OnClickListener addedBooksListener = new View.OnClickListener() {
				
				public void onClick(View v) {
						makeAllInvisible();
						applyDataToTable(booksOfUser);
				}
			};
			btnAddedBooks.setOnClickListener(addedBooksListener);
			txtBookCount.setOnClickListener(addedBooksListener);
			txtBooksText.setOnClickListener(addedBooksListener);
			
			
			
			final List<Book> commentedBooks = new GetCommentedBooksWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST_COMMENTED_BOOKS+"/"+profileUserId).get();

			btnCommentedBooks.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
						makeAllInvisible();
						applyDataToTable(commentedBooks);
				}
			});
			
			
			
			View.OnClickListener followersListener = new View.OnClickListener() {
				public void onClick(View v) {
					try {
						List<Long> followerUserIds = new ArrayList<Long>();
						for(Followship fw : followingsList){
							followerUserIds.add(fw.getFollowerUserId());
						}
						SearchCriteria sc = new SearchCriteria();
						sc.setUserIdList(followerUserIds);
						List<User> usersList = new ListUsersWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LIST+"/",
								sc,
								ApplicationConstants.signed_in_email,
								ApplicationConstants.signed_in_password
								).get();
						
						makeAllInvisible();
						applyUserDataToTable(usersList);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			txtFollowersCount.setOnClickListener(followersListener);
			txtFollowersText.setOnClickListener(followersListener);

			View.OnClickListener followingsListener = new View.OnClickListener() {
				public void onClick(View v) {
					try {
						List<Long> followingUserIds = new ArrayList<Long>();
						for(Followship fw : followingsList){
							followingUserIds.add(fw.getFollowedUserId());
						}
						SearchCriteria sc = new SearchCriteria();
						sc.setUserIdList(followingUserIds);
						List<User> usersList = new ListUsersWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LIST+"/",
								sc,
								ApplicationConstants.signed_in_email,
								ApplicationConstants.signed_in_password
								).get();
						
						makeAllInvisible();
						applyUserDataToTable(usersList);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};			
			
			txtFollowingsText.setOnClickListener(followingsListener);
			txtFollowingsCount.setOnClickListener(followingsListener);

			if(currentUserId.longValue() == profileUserId.longValue()){
				imgProfileImage.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
	
						 Intent intent = new Intent();
		                 intent.setType("image/*");
		                 intent.setAction(Intent.ACTION_GET_CONTENT);
		                 startActivityForResult(Intent.createChooser(intent,"Select Picture"), ACTIVITY_CHOOSE_PHOTO);
	
						
					}
				});
			}
			setNavigationButtons();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }

	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {
			return false;
	    }

	    public void onClick(View v) {
		}

	    private void applyUserDataToTable(List <User> dataList){

	    	for(int k = 0 ; k < dataList.size() ; k++){
				User user = dataList.get(k);
				
				switch (k) {
				case 0:
					table_1_1.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_1);
					text_1_1.setVisibility(View.VISIBLE);
					text_1_1.setText(user.getUserName());
					break;
				case 1:
					table_1_2.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_2);					
					text_1_2.setVisibility(View.VISIBLE);
					text_1_2.setText(user.getUserName());
					break;

				case 2:
					table_1_3.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_3);
					text_1_3.setVisibility(View.VISIBLE);
					text_1_3.setText(user.getUserName());
					break;

				case 3:
					table_2_1.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_1);
					text_2_1.setVisibility(View.VISIBLE);
					text_2_1.setText(user.getUserName());
					break;

				case 4:
					table_2_2.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_2);
					text_2_2.setVisibility(View.VISIBLE);
					text_2_2.setText(user.getUserName());
					break;

				case 5:
					table_2_3.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_3);
					text_2_3.setVisibility(View.VISIBLE);
					text_2_3.setText(user.getUserName());
					break;

				case 6:
					table_3_1.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_1);
					text_3_1.setVisibility(View.VISIBLE);
					text_3_1.setText(user.getUserName());
					break;

				case 7:
					table_3_2.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_2);
					text_3_2.setVisibility(View.VISIBLE);
					text_3_2.setText(user.getUserName());
					break;

				case 8:
					table_3_3.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_3);
					text_3_3.setVisibility(View.VISIBLE);
					text_3_3.setText(user.getUserName());
					break;
				case 9:
					table_4_1.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_1);
					text_4_1.setVisibility(View.VISIBLE);
					text_4_1.setText(user.getUserName());
					break;

				case 10:
					table_4_2.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_2);
					text_4_2.setVisibility(View.VISIBLE);
					text_4_2.setText(user.getUserName());
					break;

				case 11:
					table_4_3.setVisibility(View.VISIBLE);
//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_3);
					text_4_3.setVisibility(View.VISIBLE);
					text_4_3.setText(user.getUserName());
					break;

				default:
					break;
				}
	    	}
	    }	    
	    private void applyDataToTable(List <Book> dataList){
	    	if(dataList!=null){
		    	for(int k = 0 ; k < dataList.size() ; k++){
					Book book = dataList.get(k);
					
					switch (k) {
					case 0:
						table_1_1.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_1);
						text_1_1.setVisibility(View.VISIBLE);
						text_1_1.setText(book.getName());
						break;
					case 1:
						table_1_2.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_2);					
						text_1_2.setVisibility(View.VISIBLE);
						text_1_2.setText(book.getName());
						break;
	
					case 2:
						table_1_3.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_3);
						text_1_3.setVisibility(View.VISIBLE);
						text_1_3.setText(book.getName());
						break;
	
					case 3:
						table_2_1.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_1);
						text_2_1.setVisibility(View.VISIBLE);
						text_2_1.setText(book.getName());
						break;
	
					case 4:
						table_2_2.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_2);
						text_2_2.setVisibility(View.VISIBLE);
						text_2_2.setText(book.getName());
						break;
	
					case 5:
						table_2_3.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_3);
						text_2_3.setVisibility(View.VISIBLE);
						text_2_3.setText(book.getName());
						break;
	
					case 6:
						table_3_1.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_1);
						text_3_1.setVisibility(View.VISIBLE);
						text_3_1.setText(book.getName());
						break;
	
					case 7:
						table_3_2.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_2);
						text_3_2.setVisibility(View.VISIBLE);
						text_3_2.setText(book.getName());
						break;
	
					case 8:
						table_3_3.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_3);
						text_3_3.setVisibility(View.VISIBLE);
						text_3_3.setText(book.getName());
						break;
					case 9:
						table_4_1.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_1);
						text_4_1.setVisibility(View.VISIBLE);
						text_4_1.setText(book.getName());
						break;
	
					case 10:
						table_4_2.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_2);
						text_4_2.setVisibility(View.VISIBLE);
						text_4_2.setText(book.getName());
						break;
	
					case 11:
						table_4_3.setVisibility(View.VISIBLE);
	//					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_3);
						text_4_3.setVisibility(View.VISIBLE);
						text_4_3.setText(book.getName());
						break;
	
					default:
						break;
					}
		    	}
	    	}
	    }
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent intent) {
			super.onActivityResult(requestCode, resultCode, intent);
			
			switch (requestCode ){
				case ACTIVITY_CHOOSE_PHOTO:
					Uri selectedImageUri = intent.getData();
					imgProfileImage.setImageURI(selectedImageUri);
					
			        String [] proj={MediaStore.Images.Media.DATA};
			        Cursor cursor = managedQuery( selectedImageUri,
			                        proj, // Which columns to return
			                        null,       // WHERE clause; which rows to return (all rows)
			                        null,       // WHERE clause selection arguments (none)
			                        null); // Order-by clause (ascending by name)
			        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			        cursor.moveToFirst();

			        //save image into database
			    	File file = new File(cursor.getString(column_index));
			        byte[] bFile = new byte[(int) file.length()];
			 
			        try {
				     FileInputStream fileInputStream = new FileInputStream(file);
				     //convert file into array of bytes
				     fileInputStream.read(bFile);
				     fileInputStream.close();
			        } catch (Exception e) {
				     e.printStackTrace();
			        }
			 
			        User user = new User();
			        user.setUserId(currentUserId);
			        user.setProfileImage(Base64.encode(bFile, Base64.NO_WRAP));

				try {
					new UpdateUserWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_UPDATE,
							user,
							ApplicationConstants.signed_in_email,
							ApplicationConstants.signed_in_password
							).get();

				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
					
				break;
		}
			
			
		}
		private void preparelistItems(){
			table_1_1 = (ImageView)findViewById(R.id.explore_1_1);
			table_1_2 = (ImageView)findViewById(R.id.explore_1_2);
			table_1_3= (ImageView)findViewById(R.id.explore_1_3);
			table_2_1= (ImageView)findViewById(R.id.explore_2_1);
			table_2_2= (ImageView)findViewById(R.id.explore_2_2); 
			table_2_3= (ImageView)findViewById(R.id.explore_2_3);
			table_3_1= (ImageView)findViewById(R.id.explore_3_1);
			table_3_2= (ImageView)findViewById(R.id.explore_3_2);
			table_3_3= (ImageView)findViewById(R.id.explore_3_3);
			table_4_1= (ImageView)findViewById(R.id.explore_4_1);
			table_4_2= (ImageView)findViewById(R.id.explore_4_2);
			table_4_3= (ImageView)findViewById(R.id.explore_4_3);
			
			text_1_1= (TextView)findViewById(R.id.bookname_1_1);
			text_1_2= (TextView)findViewById(R.id.bookname_1_2);
			text_1_3= (TextView)findViewById(R.id.bookname_1_3);
			text_2_1= (TextView)findViewById(R.id.bookname_2_1);
			text_2_2= (TextView)findViewById(R.id.bookname_2_2);
			text_2_3= (TextView)findViewById(R.id.bookname_2_3);
			text_3_1= (TextView)findViewById(R.id.bookname_3_1);
			text_3_2= (TextView)findViewById(R.id.bookname_3_2);
			text_3_3= (TextView)findViewById(R.id.bookname_3_3);
			text_4_1= (TextView)findViewById(R.id.bookname_4_1);
			text_4_2= (TextView)findViewById(R.id.bookname_4_2);
			text_4_3= (TextView)findViewById(R.id.bookname_4_3);			
		}
		private void makeAllInvisible(){
			table_1_1.setVisibility(View.INVISIBLE);
			table_1_2.setVisibility(View.INVISIBLE);
			table_1_3.setVisibility(View.INVISIBLE);
			table_2_1.setVisibility(View.INVISIBLE);
			table_2_2.setVisibility(View.INVISIBLE);
			table_2_3.setVisibility(View.INVISIBLE);
			table_3_1.setVisibility(View.INVISIBLE);
			table_3_2.setVisibility(View.INVISIBLE);
			table_3_3.setVisibility(View.INVISIBLE);
			table_4_1.setVisibility(View.INVISIBLE);
			table_4_2.setVisibility(View.INVISIBLE);
			table_4_3.setVisibility(View.INVISIBLE);
			
			text_1_1.setVisibility(View.INVISIBLE);
			text_1_2.setVisibility(View.INVISIBLE);
			text_1_3.setVisibility(View.INVISIBLE);
			text_2_1.setVisibility(View.INVISIBLE);
			text_2_2.setVisibility(View.INVISIBLE);
			text_2_3.setVisibility(View.INVISIBLE);
			text_3_1.setVisibility(View.INVISIBLE);
			text_3_2.setVisibility(View.INVISIBLE);
			text_3_3.setVisibility(View.INVISIBLE);
			text_4_1.setVisibility(View.INVISIBLE);
			text_4_2.setVisibility(View.INVISIBLE);
			text_4_3.setVisibility(View.INVISIBLE);
	}
	private ImageView table_1_1;
	private ImageView table_1_2;
	private ImageView table_1_3;
	private ImageView table_2_1;
	private ImageView table_2_2;
	private ImageView table_2_3;
	private ImageView table_3_1;
	private ImageView table_3_2;
	private ImageView table_3_3;
	private ImageView table_4_1;
	private ImageView table_4_2;
	private ImageView table_4_3;

	private TextView text_1_1;
	private TextView text_1_2;
	private TextView text_1_3;
	private TextView text_2_1;
	private TextView text_2_2;
	private TextView text_2_3;
	private TextView text_3_1;
	private TextView text_3_2;
	private TextView text_3_3;
	private TextView text_4_1;
	private TextView text_4_2;
	private TextView text_4_3;	

}
