package com.bookworm.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;

import com.bookworm.main.ActivityBase;
import com.bookworm.main.MainActivity;
import com.bookworm.main.TimeLineActivity;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.bookworm.common.DatabaseProcess;

public class GeneralUseObject {
	
	public Date getMinDate(List<NetmeraContent> content) throws NetmeraException{
		Date tempDate=new Date();
		for(int i=0;i<content.size();i++){
			
			if(i==0){
				tempDate=content.get(i).getCreateDate();
			}
			//elimizde kitap paylaşımı hareketi var
			if(content.get(i).getCreateDate().before(tempDate)){
				tempDate=content.get(i).getCreateDate();
			}
		}
		return tempDate;
	}
	
	public void addListToTimeLineListView(List<NetmeraContent> actionList,ArrayList<HashMap<String, String>> actionListToView,ActivityBase activity){
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
					map.put(ApplicationConstants.TYPE_BOOK_OWNER, new DatabaseProcess().getUserName(tempBook1.get(ApplicationConstants.action_book_adderId).toString(),activity));
					map.put(ApplicationConstants.book_adderId, tempBook1.get(ApplicationConstants.action_book_adderId).toString());
					map.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempBook1.getCreateDate()).toString());
					actionListToView.add(map);
					//yorum hareketi var
				}else if (actionList.get(i).get(ApplicationConstants.ACTION_TYPE).toString().equals("2")){
					//listview da gösterilen satıra ait elemanları tek tek tutacak
					NetmeraContent tempComment=actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
					tempMap.put(ApplicationConstants.TYPE_COMMENDATOR, new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_er).toString(),activity));
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempComment.getCreateDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, 
							tempComment.get(ApplicationConstants.action_comment_edBook).toString());
					tempMap.put(ApplicationConstants.book_adderId, tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString());
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER, 
							new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString(),activity));
					actionListToView.add(tempMap);
					//follow hareketi var
				}else if (actionList.get(i).get(ApplicationConstants.ACTION_TYPE).toString().equals("3")){
					//listview da gösterilen satıra ait elemanları tek tek tutacak
					NetmeraContent tempFollow=actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_FOLLOW);
					tempMap.put(ApplicationConstants.TYPE_FOLLOWER, new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_follower_id).toString(),activity));
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempFollow.getCreateDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_FOLLOWED, 
							new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_followed_id).toString(),activity));
					actionListToView.add(tempMap);
				}
			
			}
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public void addListToMainBookListView(List<NetmeraContent> mainBookList,ArrayList<HashMap<String, String>> mainListToView,ActivityBase activity){
		try {

		//latestBooksList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < mainBookList.size(); i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			NetmeraContent tempBook1 = mainBookList.get(i);
//			String tem= tempBook1.get("Path").toString();
//			String tem2 = tempBook1.get("path").toString();
			tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
			NetmeraContent tempBook2 = null;
			if (i != mainBookList.size() - 1) {
				tempBook2 = mainBookList.get(i + 1);
				tempBook2.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
			}
			
			String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();//.getNetmeraMedia(ApplicationConstants.book_coverPhoto);
			
			map.put(ActivityBase.KEY_COVER_LEFT, tempBook1CoverURL);
			map.put(ActivityBase.KEY_BOOK_TITLE_LEFT, tempBook1.get(ApplicationConstants.book_name).toString());
			map.put(ActivityBase.KEY_DESC_LEFT, tempBook1.get(ApplicationConstants.book_desc).toString());
			map.put(ActivityBase.KEY_BOOK_ADDER_ID_LEFT, tempBook1.get(ApplicationConstants.book_adderId).toString());
			if (tempBook2 != null) {
				String tempBook2CoverURL = new GetNetmerMediaTask().execute(tempBook2).get();//getNetmeraMedia(ApplicationConstants.book_coverPhoto);

				map.put(ActivityBase.KEY_COVER_RIGHT, tempBook2CoverURL);
				map.put(ActivityBase.KEY_BOOK_TITLE_RIGHT, tempBook2.get(ApplicationConstants.book_name).toString());
				map.put(ActivityBase.KEY_DESC_RIGHT, tempBook2.get(ApplicationConstants.book_desc).toString());
				map.put(ActivityBase.KEY_BOOK_ADDER_ID_RIGHT, tempBook2.get(ApplicationConstants.book_adderId).toString());
			}

			mainListToView.add(map);
		}
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
	}

}