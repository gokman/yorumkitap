package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.TextView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.DatabaseProcess;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.SelectDataTask;
import com.bookworm.common.TimeLineAdapter;
import com.bookworm.model.Action;
import com.bookworm.util.ApplicationUtil;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.action.ListActionsWS;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraService.SortOrder;
import com.netmera.mobile.NetmeraUser;


public class TimeLineActivity extends ActivityBase implements OnClickListener{

	private TimeLineAdapter adapter;
	List<NetmeraContent> actionList;
	private ArrayList<HashMap<String, String>>  actionListToView=new ArrayList<HashMap<String, String>>();
	private ListView actionListView;
	private TextView timelineNextButton;
	private int pageNumber = 0;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.timeline_page);
	        
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.window_title);

	        NetmeraClient.init(this, apiKey);
	        setNavigationButtons();
	        actionListView=(ListView)findViewById(R.id.timeline_elements);
	        timelineNextButton=(TextView)findViewById(R.id.timeLineList_next_button);
	        
	        NetmeraService servicer = new NetmeraService(ApplicationConstants.action);
	        try {
				servicer.whereEqual(ApplicationConstants.ACTION_OWNER, NetmeraUser.getCurrentUser().getEmail());
			} catch (NetmeraException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        
			servicer.setSortBy(ApplicationConstants.GENERAL_COLUMN_CREATE_DATE);
	        servicer.setSortOrder(SortOrder.descending);
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
					tempMap.put(ApplicationConstants.TYPE_COMMENDATOR, new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_er).toString(),TimeLineActivity.this));
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
					tempMap.put(ApplicationConstants.TYPE_FOLLOWER, new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_follower_id).toString(),TimeLineActivity.this));
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempFollow.getCreateDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_FOLLOWED, 
							new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_followed_id).toString(),TimeLineActivity.this));
					actionListToView.add(tempMap);
				}
			
			}
			}catch(Exception e){
				e.printStackTrace();
			}
			adapter=new TimeLineAdapter(getApplicationContext(),this, actionListToView);

			actionListView.setAdapter(adapter);
			
	        timelineNextButton.setOnClickListener(new OnClickListener() {
	        	
				public void onClick(View v) {
					List<Action> actionList = Collections.emptyList();
					SearchCriteria sc = new SearchCriteria();
					sc.setPageNumber(pageNumber);
					sc.setPageSize(item_count_per_page_for_timeline_page);
					//TODO current user id 
					sc.setUserId(1L);
					sc.setOrderByCrit(GENERAL_COLUMN_ACTION_DATE);
					sc.setOrderByDrc(ORDER_BY_DIRECTION_DESCENDING);
					try {
						actionList = new ListActionsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_LIST+"/",sc).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			        ApplicationConstants.timelineListStatus+=1;
			        
					ArrayList<HashMap<String, String>>  tempActionListToView=new ArrayList<HashMap<String, String>>();
					ApplicationUtil.addListToTimeLineListView(actionList, tempActionListToView);
					adapter.add(tempActionListToView);
					adapter.notifyDataSetChanged();
			        
				}
			});			
			
			
	}

	public void onClick(View v) {
		
	}
	

}
