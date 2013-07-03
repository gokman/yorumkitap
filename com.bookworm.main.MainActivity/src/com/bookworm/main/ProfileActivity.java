package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.CountDataTask;
import com.bookworm.common.DeletetDataTask;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.ImageLoader;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.SelectDataTask;
import com.bookworm.util.ApplicationUtil;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraUser;

public class ProfileActivity extends ActivityBase implements OnClickListener{

	private String userEmail;
	private String currentUserEmail;
	private TextView txtBookCount;
	private TextView txtFollowingsCount;
	private TextView txtFollowersCount;
	private TextView txtBooksText;
	private TextView txtFollowingsText;
	private TextView txtFollowersText;
	private ImageView imgProfileImage;
	private Button btnAddedBooks;
	private Button btnCommentedBooks;
	private List<NetmeraContent> commentedBooks;
	private List<NetmeraContent> followersTransactionList;
	private List<NetmeraContent> followingTransactionList;
	private List<NetmeraContent> followersList;
	private List<NetmeraContent> booksAddedByUser;
	private List<NetmeraContent> followedUsers; 
	private List<NetmeraContent> followingUsers;
	private ImageView statusView;

	private Long followshipStatus; 
	public ImageLoader imageLoader;
	
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {

	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.profile_page);

	        imageLoader=new ImageLoader(this.getApplicationContext());

	        setExplore_button((ImageView)findViewById(R.id.explore_button));
			setHome_button((ImageView)findViewById(R.id.home_button));
			setAdd_book_button((ImageView)findViewById(R.id.add_button));
			setProfile_button((ImageView)findViewById(R.id.profile_button));
			setTimeline_button((ImageView)findViewById(R.id.timeline_button));
			
			statusView = (ImageView)findViewById(R.id.status);
			
	        preparelistItems();
	        //TODO listeleme sayfalar�ndan biriyle gelinirse  userEmail sessiondan alinmali.
			Intent myIntent = getIntent();
			userEmail = myIntent.getStringExtra(ApplicationConstants.userEmailParam);
			
	        txtBookCount = (TextView)findViewById(R.id.booksCount);
	        txtFollowersCount = (TextView)findViewById(R.id.followerCount);
	        txtFollowingsCount= (TextView)findViewById(R.id.followingCount);

	        txtBooksText = (TextView)findViewById(R.id.books);
	        txtFollowersText = (TextView)findViewById(R.id.follower);
	        txtFollowingsText= (TextView)findViewById(R.id.following);	        
	        
	        imgProfileImage = (ImageView) findViewById(R.id.profileImage);
	        btnAddedBooks = (Button)findViewById(R.id.addedBooks);
	        btnCommentedBooks = (Button)findViewById(R.id.commentedBooks);

	        try {
	        	currentUserEmail = NetmeraUser.getCurrentUser().getEmail();
	        
	        if(userEmail==null || userEmail.equals("")){
	        	userEmail = currentUserEmail;
	        }
	        
			getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        
	    	NetmeraClient.init(this, apiKey);
	    	
	    	
	    	NetmeraService servicer = new NetmeraService(ApplicationConstants.book);
	    	servicer.whereEqual(ApplicationConstants.book_adderId, userEmail);
			Long addedBookCount = new CountDataTask().execute(servicer).get();
			
			servicer = new NetmeraService(ApplicationConstants.followship);
			servicer.whereEqual(ApplicationConstants.followship_user_id,userEmail);
			Long followingsCount = new CountDataTask().execute(servicer).get();
			
			servicer = new NetmeraService(ApplicationConstants.followship);
			servicer.whereEqual(ApplicationConstants.followship_follows,userEmail);
			Long followersCount = new CountDataTask().execute(servicer).get();			
			
			servicer = new NetmeraService(ApplicationConstants.user);
			servicer.whereEqual(ApplicationConstants.user_email,userEmail);
			List<NetmeraContent> usersList = new SelectDataTask().execute(servicer).get();
			NetmeraContent user = usersList.get(0);
			String userProfileImageURL = new GetNetmerMediaTask().execute(user).get();
			
			imageLoader.DisplayImage(userProfileImageURL,imgProfileImage);
			
	    	if(currentUserEmail.equals(userEmail)){
	    		statusView.setImageResource(R.drawable.edit_icon);
	    	}else{
				servicer = new NetmeraService(ApplicationConstants.followship);
				servicer.whereEqual(ApplicationConstants.followship_user_id,currentUserEmail);
				servicer.whereEqual(ApplicationConstants.followship_follows,userEmail);
				followshipStatus = new CountDataTask().execute(servicer).get();
				if(followshipStatus > 0){
					statusView.setImageResource(R.drawable.following);
				}else{
					statusView.setImageResource(R.drawable.follow);
				}
	    		
	    	}
	    	statusView.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramView) {
			    	if(currentUserEmail.equals(userEmail)){
			         Intent bookDetailIntent = new Intent(getApplicationContext(),ProfileEditActivity.class);
			   		 bookDetailIntent.putExtra(ApplicationConstants.userEmailParam,userEmail);
					 paramView.getContext().startActivity(bookDetailIntent);

			    	}else{
						NetmeraService countService = new NetmeraService(ApplicationConstants.followship);
						countService.whereEqual(ApplicationConstants.followship_user_id,currentUserEmail);
						countService.whereEqual(ApplicationConstants.followship_follows,userEmail);
						try {
							followshipStatus = new CountDataTask().execute(countService).get();
						} catch (Exception e1) {
							e1.printStackTrace();
						}

			    		if(followshipStatus > 0){ 						//Unfollow Action
							try {
								NetmeraService servicer = new NetmeraService(ApplicationConstants.followship);
								servicer.whereEqual(ApplicationConstants.followship_user_id, currentUserEmail);
								servicer.whereEqual(ApplicationConstants.followship_follows, userEmail);
								followedUsers = new SelectDataTask().execute(servicer).get();
								for(int k = 0 ; k <followedUsers.size() ; k++){
									NetmeraContent content = followedUsers.get(k);
									new DeletetDataTask().execute(content).get();
								}
								statusView.setImageResource(R.drawable.follow);	
								
								servicer = new NetmeraService(ApplicationConstants.followship);
								servicer.whereEqual(ApplicationConstants.followship_follows,userEmail);
								Long followersCount = new CountDataTask().execute(servicer).get();			
								txtFollowersCount.setText(followersCount.toString());
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						
						}else{											//Follow Action
							NetmeraContent followship = new NetmeraContent(ApplicationConstants.followship);
							try {
								followship.add(ApplicationConstants.followship_user_id,currentUserEmail);
								followship.add(ApplicationConstants.followship_follows,userEmail);
								new InsertDataTask().execute(followship).get();

								NetmeraService servicer = new NetmeraService(ApplicationConstants.followship);
								servicer.whereEqual(ApplicationConstants.followship_follows,userEmail);
								Long followersCount = new CountDataTask().execute(servicer).get();			
								txtFollowersCount.setText(followersCount.toString());
								
								statusView.setImageResource(R.drawable.following);	
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
			    		
			    	}
				}
			});
			
			servicer = new NetmeraService(ApplicationConstants.book);
			servicer.whereEqual(ApplicationConstants.book_adderId, userEmail); 
			servicer.setMax(ApplicationConstants.item_count_per_page);
			servicer.setPage(0);
			booksAddedByUser = new SelectDataTask().execute(servicer).get();
			
			servicer = new NetmeraService(ApplicationConstants.comment);
			servicer.whereEqual(ApplicationConstants.comment_er, userEmail);
			servicer.setMax(ApplicationConstants.item_count_per_page_for_comments);
			servicer.setPage(0);
			List<NetmeraContent> comments = new SelectDataTask().execute(servicer).get();
			commentedBooks = new ArrayList<NetmeraContent>(); 

			HashMap<String,String> existing = new HashMap<String, String>();
			for(NetmeraContent content : comments){

				if(commentedBooks.size()< ApplicationConstants.item_count_per_page_for_comments){ //TODO bu kalkmal� sayfalama filan olmali
					
					NetmeraService service = new NetmeraService(ApplicationConstants.book);
					
					service.whereEqual(ApplicationConstants.book_adderId, content.get(ApplicationConstants.comment_edBookOwner).toString());
					service.whereEqual(ApplicationConstants.book_name, content.get(ApplicationConstants.comment_edBook).toString());
					
					List<NetmeraContent> tempBookList = new SelectDataTask().execute(service).get(); 
					
					if(tempBookList.size()==0)
						continue;
					
					NetmeraContent tempBook = tempBookList.get(0);
					
					if(!existing.containsKey(tempBook.get(ApplicationConstants.book_adderId).toString()) || !tempBook.get(ApplicationConstants.book_name).toString().equals(existing.get(tempBook.get(ApplicationConstants.book_adderId)))){
						
						existing.put(content.get(ApplicationConstants.comment_edBookOwner).toString(),content.get(ApplicationConstants.comment_edBook).toString());
						commentedBooks.add(tempBook);
					}
				}
				
			}
			//book list and comments list are prepared.

			txtBookCount.setText(addedBookCount.toString());
			txtFollowersCount.setText(followersCount.toString());
			txtFollowingsCount.setText(followingsCount.toString());
			applyDataToTable(booksAddedByUser,ApplicationConstants.book_name,ApplicationConstants.book_coverPhoto);
			
			View.OnClickListener addedBooksListener = new View.OnClickListener() {
				
				public void onClick(View v) {
					try {
						makeAllInvisible();
						applyDataToTable(booksAddedByUser,ApplicationConstants.book_name,ApplicationConstants.book_coverPhoto);
					} catch (NetmeraException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
			btnAddedBooks.setOnClickListener(addedBooksListener);
			txtBookCount.setOnClickListener(addedBooksListener);
			txtBooksText.setOnClickListener(addedBooksListener);
			
			btnCommentedBooks.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
					try {
						makeAllInvisible();
						applyDataToTable(commentedBooks,ApplicationConstants.book_name,ApplicationConstants.book_coverPhoto);
					} catch (NetmeraException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
			
			
			View.OnClickListener followersListener = new View.OnClickListener() {
				public void onClick(View v) {
					try {
						NetmeraService followerListService = new NetmeraService(ApplicationConstants.followship);
						followerListService.whereEqual(ApplicationConstants.followship_follows,userEmail);
						followersTransactionList = new SelectDataTask().execute(followerListService).get();
						String[] userEmailList = ApplicationUtil.convertObjectListToInputList(followersTransactionList,ApplicationConstants.followship_user_id);
						
						/*TODO 
						 * profile image will be kept in User table unavailable in NetmeraUser.(todo)
						 */
						followerListService = new NetmeraService(ApplicationConstants.user);
						followerListService.whereContainedIn(ApplicationConstants.netmera_user_email, Arrays.asList(userEmailList));
						followersList = new SelectDataTask().execute(followerListService).get();

						makeAllInvisible();
						applyDataToTable(followersList,ApplicationConstants.netmera_user_username,null);
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
						NetmeraService followingListService = new NetmeraService(ApplicationConstants.followship);
						followingListService.whereEqual(ApplicationConstants.followship_user_id, userEmail);
						followingTransactionList = new SelectDataTask().execute(followingListService).get();
						String[] userEmailList = ApplicationUtil.convertObjectListToInputList(followingTransactionList, ApplicationConstants.followship_follows);

						/*TODO 
						 * profile image will be kept in User table unavailable in NetmeraUser.(todo)
						 */
						followingListService = new NetmeraService(ApplicationConstants.user);
						followingListService.whereContainedIn(ApplicationConstants.netmera_user_email, Arrays.asList(userEmailList));
						followingUsers = new SelectDataTask().execute(followingListService).get();

						makeAllInvisible();
						applyDataToTable(followingUsers,ApplicationConstants.netmera_user_username,null);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};			
			
			txtFollowingsText.setOnClickListener(followingsListener);
			txtFollowingsCount.setOnClickListener(followingsListener);
			
			
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
			setNavigationButtons();
	    	
	    }

	    /**
	     * 
	     * @param netmeraContentList
	     * @return
	     * 
	     */


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

	    private void applyDataToTable(List <NetmeraContent> dataList,String fieldName,String imageProperty) throws NetmeraException{
	    	try {
	    	for(int k = 0 ; k < dataList.size() ; k++){
				NetmeraContent content = dataList.get(k);
				content.add(ApplicationConstants.generic_property, imageProperty);
				
				switch (k) {
				case 0:
					table_1_1.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_1);
					text_1_1.setVisibility(View.VISIBLE);
					text_1_1.setText(content.get(fieldName).toString());
					break;
				case 1:
					table_1_2.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_2);					
					text_1_2.setVisibility(View.VISIBLE);
					text_1_2.setText(content.get(fieldName).toString());
					break;

				case 2:
					table_1_3.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_1_3);
					text_1_3.setVisibility(View.VISIBLE);
					text_1_3.setText(content.get(fieldName).toString());
					break;

				case 3:
					table_2_1.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_1);
					text_2_1.setVisibility(View.VISIBLE);
					text_2_1.setText(content.get(fieldName).toString());
					break;

				case 4:
					table_2_2.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_2);
					text_2_2.setVisibility(View.VISIBLE);
					text_2_2.setText(content.get(fieldName).toString());
					break;

				case 5:
					table_2_3.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_2_3);
					text_2_3.setVisibility(View.VISIBLE);
					text_2_3.setText(content.get(fieldName).toString());
					break;

				case 6:
					table_3_1.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_1);
					text_3_1.setVisibility(View.VISIBLE);
					text_3_1.setText(content.get(fieldName).toString());
					break;

				case 7:
					table_3_2.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_2);
					text_3_2.setVisibility(View.VISIBLE);
					text_3_2.setText(content.get(fieldName).toString());
					break;

				case 8:
					table_3_3.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_3_3);
					text_3_3.setVisibility(View.VISIBLE);
					text_3_3.setText(content.get(fieldName).toString());
					break;
				case 9:
					table_4_1.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_1);
					text_4_1.setVisibility(View.VISIBLE);
					text_4_1.setText(content.get(fieldName).toString());
					break;

				case 10:
					table_4_2.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_2);
					text_4_2.setVisibility(View.VISIBLE);
					text_4_2.setText(content.get(fieldName).toString());
					break;

				case 11:
					table_4_3.setVisibility(View.VISIBLE);
					imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), table_4_3);
					text_4_3.setVisibility(View.VISIBLE);
					text_4_3.setText(content.get(fieldName).toString());
					break;

				default:
					break;
				}
			}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected void onActivityResult(int requestCode, int resultCode,
				Intent intent) {
			super.onActivityResult(requestCode, resultCode, intent);
			super.onActivityResult(requestCode, resultCode);
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
