package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.ImageLoader;
import com.bookworm.model.Book;
import com.bookworm.model.Hashtag;
import com.bookworm.model.User;
import com.bookworm.util.ApplicationUtil;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.book.ListBooksWS;
import com.bookworm.ws.hashtag.ListHashtagsWS;
import com.bookworm.ws.user.ListUsersWS;

public class ExploreActivity extends ActivityBase implements OnClickListener{

	
    public ImageLoader imageLoader;
    
	private Button btnExploreBooks;
	private Button btnExploreUsers;
	private EditText searchText ;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.explore_page);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

        imageLoader=new ImageLoader(this.getApplicationContext());
        
		btnExploreBooks = (Button)findViewById(R.id.exploreBooks);
        btnExploreUsers = (Button)findViewById(R.id.exploreUsers);
        searchText = (EditText)findViewById(R.id.searchText);

        preparelistItems();
        makeAllInvisible();
        
        btnExploreUsers.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				String searchKey = searchText.getText().toString();

				if(searchKey!=null && !searchKey.equals(ApplicationConstants.EMPTY_STRING)){
					if (searchKey.substring(0, 1).equals(ApplicationConstants.AT_SIGN)){
						searchKey = searchKey.substring(1);
					}
				}
				SearchCriteria sc = new SearchCriteria();
				sc.setPageSize(item_count_per_page_for_explore_page);
				sc.setUserName(searchKey);
				try{
					List<User> usersList = new ListUsersWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LIST+"/",
							sc,
							ApplicationConstants.signed_in_email,
							ApplicationConstants.signed_in_password
							).get();
					makeAllInvisible();
					//TODO user search 
					applyDataToTable(usersList);
					
					if(usersList.size()==0){
						getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
						
						Toast toast= Toast.makeText(getApplicationContext(),
								getString(R.string.noResultFound), Toast.LENGTH_LONG);
								toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
								toast.show();

					}					
					
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
		});
		btnExploreBooks.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String searchKey = searchText.getText().toString();

				if (searchKey != null
						&& !searchKey.equals(ApplicationConstants.EMPTY_STRING)) {
					if (searchKey.substring(0, 1).equals(
							ApplicationConstants.DIESIS_SIGN)) {
						searchKey = searchKey.substring(1);
					}
					try {
						
						List<Hashtag>hashtagList = new ListHashtagsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_HASHTAG+"/"
								+WS_OPERATION_LIST_BY_TEXT+"/"+searchKey).get();
						List <Book> bookList = new ArrayList<Book>();
						
						if (hashtagList != null && hashtagList.size() > 0) {
							List<Long> bookIdList = ApplicationUtil.getIdList(hashtagList,"bookId");
							SearchCriteria sc = new SearchCriteria();
							sc.setBookIdList(bookIdList);
							bookList = new ListBooksWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"+WS_OPERATION_LIST+"/",sc).get();							
						}
						
						makeAllInvisible();
						applyDataToTable(bookList);
						if(bookList.size()==0){
							getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

							Toast toast= Toast.makeText(getApplicationContext(),
									getString(R.string.noResultFound), Toast.LENGTH_LONG);
									toast.setGravity(Gravity.CENTER|Gravity.CENTER_HORIZONTAL, 0, 0);
									toast.show();
	
						}

					} catch (Exception ex) {
						ex.printStackTrace();
					}

					/*
					 * Hashtag le arama yap�yoruz sadece NetmeraService service
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
		setFooterButtonState(getExplore_button());
    }
	public void onClick(View v) {
		
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
    }

    
    private void applyDataToTable(List dataList){
    	for(int k = 0 ; k < dataList.size() ; k++){
//			content.add(ApplicationConstants.generic_property, imageProperty);
			String textProperty = EMPTY_STRING;
			if(dataList.get(k) instanceof Book){
				textProperty = ((Book)dataList.get(k)).getName();
			}else if(dataList.get(k) instanceof User){
				textProperty = ((User)dataList.get(k)).getUserName();
			}

			switch (k) {
			case 0:
				exp_1_1.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_1);
				text_1_1.setVisibility(View.VISIBLE);
				text_1_1.setText(textProperty);
				break;
			case 1:
				exp_1_2.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_2);					
				text_1_2.setVisibility(View.VISIBLE);
				text_1_2.setText(textProperty);
				break;

			case 2:
				exp_1_3.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_1_3);
				text_1_3.setVisibility(View.VISIBLE);
				text_1_3.setText(textProperty);
				break;

			case 3:
				exp_2_1.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_1);
				text_2_1.setVisibility(View.VISIBLE);
				text_2_1.setText(textProperty);
				break;

			case 4:
				exp_2_2.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_2);
				text_2_2.setVisibility(View.VISIBLE);
				text_2_2.setText(textProperty);
				break;

			case 5:
				exp_2_3.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_2_3);
				text_2_3.setVisibility(View.VISIBLE);
				text_2_3.setText(textProperty);
				break;

			case 6:
				exp_3_1.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_1);
				text_3_1.setVisibility(View.VISIBLE);
				text_3_1.setText(textProperty);
				break;

			case 7:
				exp_3_2.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_2);
				text_3_2.setVisibility(View.VISIBLE);
				text_3_2.setText(textProperty);
				break;

			case 8:
				exp_3_3.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_3_3);
				text_3_3.setVisibility(View.VISIBLE);
				text_3_3.setText(textProperty);
				break;
			case 9:
				exp_4_1.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_1);
				text_4_1.setVisibility(View.VISIBLE);
				text_4_1.setText(textProperty);
				break;

			case 10:
				exp_4_2.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_2);
				text_4_2.setVisibility(View.VISIBLE);
				text_4_2.setText(textProperty);
				break;

			case 11:
				exp_4_3.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_4_3);
				text_4_3.setVisibility(View.VISIBLE);
				text_4_3.setText(textProperty);
				break;
			case 12:
				exp_5_1.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_1);
				text_5_1.setVisibility(View.VISIBLE);
				text_5_1.setText(textProperty);
				break;

			case 13:
				exp_5_2.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_2);
				text_5_2.setVisibility(View.VISIBLE);
				text_5_2.setText(textProperty);
				break;

			case 14:
				exp_5_3.setVisibility(View.VISIBLE);
//				imageLoader.DisplayImage(new GetNetmerMediaTask().execute(content).get(), exp_5_3);
				text_5_3.setVisibility(View.VISIBLE);
				text_5_3.setText(textProperty);
				break;

			default:
				break;

			}
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
		text_5_1.setVisibility(View.INVISIBLE);
		text_5_2.setVisibility(View.INVISIBLE);
		text_5_3.setVisibility(View.INVISIBLE);
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
