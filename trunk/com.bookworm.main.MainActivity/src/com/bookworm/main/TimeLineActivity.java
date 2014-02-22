package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_ACTION;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST;
import static com.bookworm.common.ApplicationConstants.item_count_per_page_for_timeline_page;

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

import com.bookworm.common.TimeLineAdapter;
import com.bookworm.model.Action;
import com.bookworm.util.ApplicationUtil;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.action.ListActionsWS;
import com.netmera.mobile.NetmeraClient;


public class TimeLineActivity extends ActivityBase implements OnClickListener{

	private TimeLineAdapter adapter;
	List<Action> actionList;
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

			SearchCriteria sc = new SearchCriteria();
			sc.setPageNumber(pageNumber);
			sc.setPageSize(item_count_per_page_for_timeline_page);
			//TODO current user id 
			sc.setUserId(1L);
//			sc.setOrderByCrit(GENERAL_COLUMN_ACTION_DATE);
//			sc.setOrderByDrc(ORDER_BY_DIRECTION_DESCENDING);
			try {
				actionList = new ListActionsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_LIST+"/",sc).get();
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ExecutionException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}	        

	        ApplicationUtil.addListToTimeLineListView(actionList, actionListToView);
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
//					sc.setOrderByCrit(GENERAL_COLUMN_ACTION_DATE);
//					sc.setOrderByDrc(ORDER_BY_DIRECTION_DESCENDING);
					try {
						actionList = new ListActionsWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_ACTION+"/"+WS_OPERATION_LIST+"/",sc).get();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (ExecutionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			        pageNumber++;

					ApplicationUtil.addListToTimeLineListView(actionList, actionListToView);
					adapter.add(actionListToView);
					adapter.notifyDataSetChanged();
			        
				}
			});			
			
			
	}

	public void onClick(View v) {
		
	}
	

}
