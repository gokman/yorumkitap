package com.bookworm.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.DatabaseProcess;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.SelectDataTask;
import com.bookworm.common.TimeLineAdapter;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraService.SortOrder;
import com.netmera.mobile.NetmeraUser;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;


public class TimeLineActivity extends ActivityBase implements OnClickListener{
	//listelenen elemanların tipini tutacak
	public static final String ListElementType="TYPE";
	private TimeLineAdapter adapter;
	//tablodaki kitapları satır satır olduğu gibi tutar
	List<NetmeraContent> actionList;
	//viewda gösterilecek olan satırları tutar  --book
	private ArrayList<HashMap<String, String>>  actionListToView;
	private ListView actionListView;
	//viewda gösterilecek olan satırları tutar --comment

	@Override
    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.timeline_page);
	        
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.window_title);

	        NetmeraClient.init(this, apiKey);
	        setNavigationButtons();
	        
	       
	        //kitap için bize yardımcı olacak servisimiz
	        NetmeraService servicer = new NetmeraService(ApplicationConstants.action);
	        try {
				servicer.whereEqual(ApplicationConstants.ACTION_OWNER, NetmeraUser.getCurrentUser().getEmail());
			} catch (NetmeraException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
			servicer.setSortBy("creationdate");
	        servicer.setSortOrder(SortOrder.ascending);
			servicer.setMax(ApplicationConstants.item_count_per_page_for_timeline_page);
			
			try {
				actionList=new SelectDataTask(TimeLineActivity.this).execute(servicer).get();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try{
			actionListToView=new ArrayList<HashMap<String, String>>();
			HashMap<String,String> tempMap=new HashMap<String, String>();
			for(int i=0;i<actionList.size();i++){
				//elimizde kitap paylaşımı hareketi var
				if(actionList.get(i).get(ApplicationConstants.ACTION_TYPE).toString().equals("1")){

					NetmeraContent tempBook1=actionList.get(i);
					//duzgunce adapter a uygun yerlestirecegimiz map imiz var
					HashMap<String,String> map=new HashMap<String,String>();
					//BUNU BiLMiYORUM ????
					tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
					String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();
					
					
					//artik elimde bir satira ait hersey var. bunu simdi map e atacagiz.
					map.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_BOOK);
					map.put(ApplicationConstants.TYPE_COVER_URL, tempBook1CoverURL);
					map.put(ApplicationConstants.TYPE_BOOK_NAME, tempBook1.get(ApplicationConstants.action_book_name).toString());
					map.put(ApplicationConstants.TYPE_BOOK_DESC, tempBook1.get(ApplicationConstants.action_book_desc).toString());
					map.put(ApplicationConstants.TYPE_BOOK_OWNER, new DatabaseProcess().getUserName(tempBook1.get(ApplicationConstants.action_book_adderId).toString(),TimeLineActivity.this));
					map.put(ApplicationConstants.book_adderId, tempBook1.get(ApplicationConstants.action_book_adderId).toString());
					map.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempBook1.getCreateDate()).toString());
					actionListToView.add(map);
					//yorum hareketi var
				}else if (actionList.get(i).get(ApplicationConstants.ACTION_TYPE).toString().equals("2")){
					//listview da gösterilen satıra ait elemanları tek tek tutacak
					NetmeraContent tempComment=actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
					tempMap.put(ApplicationConstants.TYPE_COMMENDATOR, tempComment.get(ApplicationConstants.action_comment_er).toString());
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempComment.getCreateDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, 
							tempComment.get(ApplicationConstants.action_comment_edBook).toString());
					tempMap.put(ApplicationConstants.book_adderId, tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString());
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER, 
							new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString(),TimeLineActivity.this));
					actionListToView.add(tempMap);
					//follow hareketi var
				}else if (actionList.get(i).get(ApplicationConstants.ACTION_TYPE).toString().equals("3")){
					//listview da gösterilen satıra ait elemanları tek tek tutacak
					NetmeraContent tempFollow=actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_FOLLOW);
					tempMap.put(ApplicationConstants.TYPE_FOLLOWER, tempFollow.get(ApplicationConstants.action_follower_id).toString());
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempFollow.getCreateDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_FOLLOWED, 
							tempFollow.get(ApplicationConstants.action_followed_id).toString());
					actionListToView.add(tempMap);
				}
			
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			
			//elimizdeki listviewı ekliyoruz
			actionListView=(ListView)findViewById(R.id.timeline_elements);
	        adapter=new TimeLineAdapter(getApplicationContext(),this, actionListToView);
	        actionListView.setAdapter(adapter);
	        
	        
	        //listedeki elemanlara t�kland���nda yap�lacak i�lemler
	      /*  actionListView.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
			
				if(view.getTag().toString().equals(ApplicationConstants.TYPE_BOOK)){
							
					Intent bookDetailIntent = new Intent(getApplicationContext(), BookDetailActivity.class);
			    	bookDetailIntent.putExtra(ApplicationConstants.book_name, ((TextView)view.findViewById(R.id.timeline_book_title)).getText().toString());
			    	bookDetailIntent.putExtra(ApplicationConstants.book_adderId, ((TextView)view.findViewById(R.id.timeline_book_adderId)).getText().toString());
			    	startActivity(bookDetailIntent);
					
				}else if(view.getTag().toString().equals(ApplicationConstants.TYPE_COMMENT)){
					
					Intent bookDetailIntent = new Intent(getApplicationContext(), BookDetailActivity.class);
			    	bookDetailIntent.putExtra(ApplicationConstants.book_name, ((TextView)view.findViewById(R.id.commentedBookName)).getText().toString());
			    	bookDetailIntent.putExtra(ApplicationConstants.book_adderId, ((TextView)view.findViewById(R.id.timeline_follow_book_adderId)).getText().toString());
			    	startActivity(bookDetailIntent);
					
				}else if(view.getTag().toString().equals(ApplicationConstants.TYPE_FOLLOW)){
					
				}
					
				}
			});*/
	                
	}

	public void onClick(View v) {
		
	}
	

}
