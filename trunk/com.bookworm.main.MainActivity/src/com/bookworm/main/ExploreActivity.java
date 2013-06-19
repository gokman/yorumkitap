package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.ImageLoader;
import com.bookworm.common.SelectDataTask;
import com.bookworm.model.Book;
import com.bookworm.util.ApplicationUtil;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraUser;

public class ExploreActivity extends ActivityBase implements OnClickListener{

	
    public ImageLoader imageLoader;
    
    NetmeraUser user;
    
	private Button btnExploreBooks;
	private Button btnExploreUsers;
	private EditText searchText ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.explore_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

		NetmeraClient.init(this, apiKey);
		try {
			user = NetmeraUser.getCurrentUser();
		} catch (NetmeraException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        imageLoader=new ImageLoader(this.getApplicationContext());
        
        setExplore_button((ImageView)findViewById(R.id.explore_button));
		setHome_button((ImageView)findViewById(R.id.home_button));
		setAdd_book_button((ImageView)findViewById(R.id.add_button));
		setProfile_button((ImageView)findViewById(R.id.profile_button));
		
		btnExploreBooks = (Button)findViewById(R.id.exploreBooks);
        btnExploreUsers = (Button)findViewById(R.id.exploreUsers);
        searchText = (EditText)findViewById(R.id.searchText);
        preparelistItems();
		/*TODO Get latest 15 records to show in explore page (must be integrated with refresh button)
		 * Stub booklist object */
		List<Book> bookList = new ArrayList<Book>();

		/*
		*/		
		btnExploreUsers.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String searchKey = searchText.getText().toString();
				NetmeraService service = new NetmeraService(ApplicationConstants.user);
				if(searchKey!=null && !searchKey.equals(ApplicationConstants.EMPTY_STRING)){
					String regEx = "*"+searchKey+ "*";
					service.whereMatches(ApplicationConstants.user_username, regEx);
				}
				service.setMax(ApplicationConstants.item_count_per_page_for_explore_page);
				try{
					List<NetmeraContent> usersList = new SelectDataTask().execute(service).get();
					makeAllInvisible();
					applyDataToTable(usersList,ApplicationConstants.user_username,ApplicationConstants.user_userProfile);
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
		});
		btnExploreBooks.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String searchKey = searchText.getText().toString();

				NetmeraService service = new NetmeraService(ApplicationConstants.hashtable);
				NetmeraService bookService = new NetmeraService(ApplicationConstants.book);
				if (searchKey != null
						&& !searchKey.equals(ApplicationConstants.EMPTY_STRING)) {
					if (!searchKey.substring(0, 1).equals(
							ApplicationConstants.DIESIS_SIGN)) {
						searchKey = ApplicationConstants.DIESIS_SIGN
								+ searchKey;
					}
					service.whereEqual(ApplicationConstants.hashtable_tag,
							searchKey);
					service.setMax(ApplicationConstants.item_count_per_page_for_explore_page);
					try {
						List<NetmeraContent> hashtagList = new SelectDataTask().execute(service).get();
						List <NetmeraContent> bookList = new ArrayList<NetmeraContent>();
						
						if (hashtagList != null && hashtagList.size() > 0) {
							String[] bookPathList = ApplicationUtil.convertObjectListToInputList(hashtagList, ApplicationConstants.hashtable_book_path);
							
							bookService.whereContainedIn(ApplicationConstants.object_path, Arrays.asList(bookPathList));
							bookList = new SelectDataTask().execute(bookService).get();	
						}
						makeAllInvisible();
						applyDataToTable(bookList,ApplicationConstants.book_name,ApplicationConstants.book_coverPhoto);

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					/*
					 * Hashtag le arama yapýyoruz sadece NetmeraService service
					 * = new NetmeraService(ApplicationConstants.book); //
					 * if(searchKey!=null &&
					 * !searchKey.equals(ApplicationConstants.EMPTY_STRING)){ //
					 * String regEx = "*"+searchKey+ "*"; //
					 * service.whereMatches(ApplicationConstants.book_name,
					 * regEx); // } // service.setMax(ApplicationConstants.
					 * item_count_per_page_for_explore_page); // try{ //
					 * List<NetmeraContent> usersList = new
					 * SelectDataTask().execute(service).get(); //
					 * makeAllInvisible(); //
					 * applyDataToTable(usersList,ApplicationConstants
					 * .book_name,ApplicationConstants.book_coverPhoto); // //
					 * }catch(Exception ex){ // ex.printStackTrace(); // }
					 */

				}
			}


		});
		setNavigationButtons();
    }
	public void onClick(View v) {
		
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
    }

    
    private void applyDataToTable(List <NetmeraContent> dataList,String fieldName,String imageProperty) throws NetmeraException{
    	try {
    	for(int k = 0 ; k < dataList.size() ; k++){
			NetmeraContent content = dataList.get(k);
			content.add(ApplicationConstants.generic_property, imageProperty);
			
			switch (k) {
			case 0:
				exp_1_1.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_1);
				text_1_1.setVisibility(View.VISIBLE);
				text_1_1.setText(content.get(fieldName).toString());
				break;
			case 1:
				exp_1_2.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_2);					
				text_1_2.setVisibility(View.VISIBLE);
				text_1_2.setText(content.get(fieldName).toString());
				break;

			case 2:
				exp_1_3.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_3);
				text_1_3.setVisibility(View.VISIBLE);
				text_1_3.setText(content.get(fieldName).toString());
				break;

			case 3:
				exp_2_1.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_1);
				text_2_1.setVisibility(View.VISIBLE);
				text_2_1.setText(content.get(fieldName).toString());
				break;

			case 4:
				exp_2_2.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_2);
				text_2_2.setVisibility(View.VISIBLE);
				text_2_2.setText(content.get(fieldName).toString());
				break;

			case 5:
				exp_2_3.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_3);
				text_2_3.setVisibility(View.VISIBLE);
				text_2_3.setText(content.get(fieldName).toString());
				break;

			case 6:
				exp_3_1.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_1);
				text_3_1.setVisibility(View.VISIBLE);
				text_3_1.setText(content.get(fieldName).toString());
				break;

			case 7:
				exp_3_2.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_2);
				text_3_2.setVisibility(View.VISIBLE);
				text_3_2.setText(content.get(fieldName).toString());
				break;

			case 8:
				exp_3_3.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_3);
				text_3_3.setVisibility(View.VISIBLE);
				text_3_3.setText(content.get(fieldName).toString());
				break;
			case 9:
				exp_4_1.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_1);
				text_4_1.setVisibility(View.VISIBLE);
				text_4_1.setText(content.get(fieldName).toString());
				break;

			case 10:
				exp_4_2.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_2);
				text_4_2.setVisibility(View.VISIBLE);
				text_4_2.setText(content.get(fieldName).toString());
				break;

			case 11:
				exp_4_3.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_3);
				text_4_3.setVisibility(View.VISIBLE);
				text_4_3.setText(content.get(fieldName).toString());
				break;
			case 12:
				exp_5_1.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_1);
				text_5_1.setVisibility(View.VISIBLE);
				text_5_1.setText(content.get(fieldName).toString());
				break;

			case 13:
				exp_5_2.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_2);
				text_5_2.setVisibility(View.VISIBLE);
				text_5_2.setText(content.get(fieldName).toString());
				break;

			case 14:
				exp_5_3.setVisibility(View.VISIBLE);
				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_3);
				text_5_3.setVisibility(View.VISIBLE);
				text_5_3.setText(content.get(fieldName).toString());
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
	private void makeAllInvisible(){
		exp_1_1.setVisibility(View.INVISIBLE);
		exp_1_2.setVisibility(View.INVISIBLE);
		exp_1_3.setVisibility(View.INVISIBLE);
		exp_2_1.setVisibility(View.INVISIBLE);
		exp_2_2.setVisibility(View.INVISIBLE);
		exp_2_3.setVisibility(View.INVISIBLE);
		exp_3_1.setVisibility(View.INVISIBLE);
		exp_3_2.setVisibility(View.INVISIBLE);
		exp_3_3.setVisibility(View.INVISIBLE);
		exp_4_1.setVisibility(View.INVISIBLE);
		exp_4_2.setVisibility(View.INVISIBLE);
		exp_4_3.setVisibility(View.INVISIBLE);
		exp_5_1.setVisibility(View.INVISIBLE);
		exp_5_2.setVisibility(View.INVISIBLE);
		exp_5_3.setVisibility(View.INVISIBLE);
		
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
	private void preparelistItems(){
		exp_1_1 = (ImageView)findViewById(R.id.explore_1_1);
		exp_1_2 = (ImageView)findViewById(R.id.explore_1_2);
		exp_1_3= (ImageView)findViewById(R.id.explore_1_3);
		exp_2_1= (ImageView)findViewById(R.id.explore_2_1);
		exp_2_2= (ImageView)findViewById(R.id.explore_2_2); 
		exp_2_3= (ImageView)findViewById(R.id.explore_2_3);
		exp_3_1= (ImageView)findViewById(R.id.explore_3_1);
		exp_3_2= (ImageView)findViewById(R.id.explore_3_2);
		exp_3_3= (ImageView)findViewById(R.id.explore_3_3);
		exp_4_1= (ImageView)findViewById(R.id.explore_4_1);
		exp_4_2= (ImageView)findViewById(R.id.explore_4_2);
		exp_4_3= (ImageView)findViewById(R.id.explore_4_3);
		exp_5_1= (ImageView)findViewById(R.id.explore_5_1);
		exp_5_2= (ImageView)findViewById(R.id.explore_5_2);
		exp_5_3= (ImageView)findViewById(R.id.explore_5_3);
		
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
		text_5_1= (TextView)findViewById(R.id.bookname_5_1);
		text_5_2= (TextView)findViewById(R.id.bookname_5_2);
		text_5_3= (TextView)findViewById(R.id.bookname_5_3);		
	}
    private ImageView exp_1_1;
    private ImageView exp_1_2;
    private ImageView exp_1_3;
    private ImageView exp_2_1;
    private ImageView exp_2_2;
    private ImageView exp_2_3;
    private ImageView exp_3_1;
    private ImageView exp_3_2;
    private ImageView exp_3_3;
    private ImageView exp_4_1;
    private ImageView exp_4_2;
    private ImageView exp_4_3;
    private ImageView exp_5_1;
    private ImageView exp_5_2;
    private ImageView exp_5_3;
	
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
	private TextView text_5_1;
	private TextView text_5_2;
	private TextView text_5_3;	
}
