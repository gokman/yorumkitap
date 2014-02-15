package com.bookworm.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.DatabaseProcess;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.main.ActivityBase;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Book;
import com.bookworm.model.Hashtag;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;

public class ApplicationUtil {

    public static List<Long> getIdList(List <Hashtag> list,String inputField ){
    	List<Long> outputList = new ArrayList<Long>();
    	if(list!=null && list.size()>=0){
    		for(int i = 0 ; i < list.size(); i++){
					outputList.add(list.get(i).getBookId());
    		}
    	}
    	return outputList;
    }	

    
	public static Date getMinDate(List<NetmeraContent> content) throws NetmeraException{
		Date tempDate=new Date();
		for(int i=0;i<content.size();i++){
			if(i==0){
				tempDate=content.get(i).getCreateDate();
			}
			if(content.get(i).getCreateDate().before(tempDate)){
				tempDate=content.get(i).getCreateDate();
			}
		}
		return tempDate;
	}
	
	public static void addListToTimeLineListView(List<Action> actionList,ArrayList<HashMap<String, String>> actionListToView){
         try{
			HashMap<String,String> tempMap=new HashMap<String, String>();
			for(int i=0;i<actionList.size();i++){
				if(actionList.get(i).getActionType().equals(ActionType.ADD_BOOK)){

					Action tempAction1=actionList.get(i);
					HashMap<String,String> map=new HashMap<String,String>();
//					tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
//					String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();
					
					map.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_BOOK);
//					map.put(ApplicationConstants.TYPE_COVER_URL, tempBook1CoverURL);
//					map.put(ApplicationConstants.TYPE_BOOK_NAME, tempBook1.get(ApplicationConstants.action_book_name).toString());
//					map.put(ApplicationConstants.TYPE_BOOK_DESC, tempBook1.get(ApplicationConstants.action_book_desc).toString());
//					map.put(ApplicationConstants.TYPE_BOOK_OWNER, new DatabaseProcess().getUserName(tempBook1.get(ApplicationConstants.action_book_adderId).toString(),activity));
//					map.put(ApplicationConstants.book_adderId, tempBook1.get(ApplicationConstants.action_book_adderId).toString());
//					map.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempBook1.getCreateDate()).toString());
					actionListToView.add(map);
				}else if (actionList.get(i).getActionType().equals(ActionType.ADD_COMMENT)){

					Action tempAction2=actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
//					tempMap.put(ApplicationConstants.TYPE_COMMENDATOR, new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_er).toString(),activity));
//					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempComment.getCreateDate()).toString());
//					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, 
//							tempComment.get(ApplicationConstants.action_comment_edBook).toString());
//					tempMap.put(ApplicationConstants.book_adderId, tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString());
//					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER, 
//							new DatabaseProcess().getUserName(tempComment.get(ApplicationConstants.action_comment_edBookOwner).toString(),activity));
					actionListToView.add(tempMap);

				}else if (actionList.get(i).getActionType().equals(ActionType.FOLLOW)){
					Action action =actionList.get(i);
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_FOLLOW);
//					tempMap.put(ApplicationConstants.TYPE_FOLLOWER, new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_follower_id).toString(),activity));
//					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(tempFollow.getCreateDate()).toString());
//					tempMap.put(ApplicationConstants.TYPE_FOLLOWED, 
//							new DatabaseProcess().getUserName(tempFollow.get(ApplicationConstants.action_followed_id).toString(),activity));
					actionListToView.add(tempMap);
				}
			
			}
			}catch(Exception e){
				e.printStackTrace();
			}
	}
	
	public static void addListToMainBookListView(List<Book> mainBookList,ArrayList<HashMap<String, String>> mainListToView){

		//latestBooksList = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < mainBookList.size(); i += 2) {
			HashMap<String, String> map = new HashMap<String, String>();
			Book tempBook1 = mainBookList.get(i);
//			String tem= tempBook1.get("Path").toString();
//			String tem2 = tempBook1.get("path").toString();
			Book tempBook2 = null;
			if (i != mainBookList.size() - 1) {
				tempBook2 = mainBookList.get(i + 1);
			}
			
//			String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();//.getNetmeraMedia(ApplicationConstants.book_coverPhoto);
			
			map.put(ActivityBase.KEY_COVER_LEFT, tempBook1.getCoverPhoto());
			map.put(ActivityBase.KEY_BOOK_TITLE_LEFT, tempBook1.getName());
			map.put(ActivityBase.KEY_DESC_LEFT, tempBook1.getDescription());
			map.put(ActivityBase.KEY_BOOK_ADDER_ID_LEFT, tempBook1.getAdderId().toString());
			if (tempBook2 != null) {
//				String tempBook2CoverURL = new GetNetmerMediaTask().execute(tempBook2).get();//getNetmeraMedia(ApplicationConstants.book_coverPhoto);

				map.put(ActivityBase.KEY_COVER_RIGHT, tempBook2.getCoverPhoto());
				map.put(ActivityBase.KEY_BOOK_TITLE_RIGHT, tempBook2.getName());
				map.put(ActivityBase.KEY_DESC_RIGHT, tempBook2.getDescription());
				map.put(ActivityBase.KEY_BOOK_ADDER_ID_RIGHT, tempBook2.getAdderId().toString());
			}

			mainListToView.add(map);
		}
	}
    
}
