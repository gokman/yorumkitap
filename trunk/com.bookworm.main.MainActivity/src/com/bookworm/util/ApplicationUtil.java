package com.bookworm.util;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_BOOK;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_COMMENT;
import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_FOLLOWSHIP;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_GET_BY_ID;
import static com.bookworm.common.ApplicationConstants.dateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.main.ActivityBase;
import com.bookworm.model.Action;
import com.bookworm.model.ActionType;
import com.bookworm.model.Book;
import com.bookworm.model.Comment;
import com.bookworm.model.Followship;
import com.bookworm.model.Hashtag;
import com.bookworm.ws.book.GetBookInfoWS;
import com.bookworm.ws.comment.GetCommentWS;
import com.bookworm.ws.followship.GetFollowshipWS;

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

    
	public static void addListToTimeLineListView(List<Action> actionList,ArrayList<HashMap<String, String>> actionListToView){
         try{
			HashMap<String,String> tempMap=new HashMap<String, String>();
			for(int i=0;i<actionList.size();i++){
				if(actionList.get(i).getActionType() == ActionType.ADD_BOOK.asCode()){
					
					Action addBookAction =actionList.get(i);
					Book book  = new GetBookInfoWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"
							+WS_OPERATION_GET_BY_ID+"/"+addBookAction.getActionDetailId()).get();
					
					HashMap<String,String> map=new HashMap<String,String>();
//					tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
//					String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();
					map.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_BOOK);
//					map.put(ApplicationConstants.TYPE_COVER_URL, tempBook1CoverURL);
					map.put(ApplicationConstants.TYPE_BOOK_NAME, book.getName());
					map.put(ApplicationConstants.TYPE_BOOK_DESC, book.getDescription());
					map.put(ApplicationConstants.TYPE_BOOK_OWNER, book.getAdderId().toString());
					map.put(ApplicationConstants.book_adderId, book.getAdderId().toString());
					map.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(addBookAction.getActionDate()).toString());
					actionListToView.add(map);
					
				}else if (actionList.get(i).getActionType()==ActionType.ADD_COMMENT.asCode()){

					Action addCommentAction =actionList.get(i);
					Comment comment = new GetCommentWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_COMMENT+"/"
							+WS_OPERATION_GET_BY_ID+"/"+addCommentAction.getActionDetailId()).get();
					Book commentedBook = new GetBookInfoWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_BOOK+"/"
							+WS_OPERATION_GET_BY_ID+"/"+comment.getCommentedBookId()).get();
					
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
					tempMap.put(ApplicationConstants.TYPE_COMMENDATOR,comment.getCommenterId().toString());
					tempMap.put(ApplicationConstants.CREATE_DATE, ApplicationConstants.dateFormat.format(addCommentAction.getActionDate()));
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, commentedBook.getName());
					tempMap.put(ApplicationConstants.book_adderId, commentedBook.getAdderId().toString());
					tempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER,commentedBook.getAdderId().toString());
					actionListToView.add(tempMap);

				}else if (actionList.get(i).getActionType() == ActionType.FOLLOW.asCode()){
					Action action =actionList.get(i);
					Followship followship = new GetFollowshipWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_FOLLOWSHIP+"/"
							+WS_OPERATION_GET_BY_ID+"/"+action.getActionDetailId()).get();
					tempMap=new HashMap<String, String>();
					tempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_FOLLOW);
					tempMap.put(ApplicationConstants.TYPE_FOLLOWER, followship.getFollowerUserId().toString());
					tempMap.put(ApplicationConstants.CREATE_DATE, dateFormat.format(action.getActionDate()).toString());
					tempMap.put(ApplicationConstants.TYPE_FOLLOWED,followship.getFollowedUserId().toString());
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
			
			map.put(ActivityBase.KEY_BOOK_ID_LEFT, tempBook1.getBookId().toString());
			map.put(ActivityBase.KEY_COVER_LEFT, tempBook1.getCoverPhoto());
			map.put(ActivityBase.KEY_BOOK_TITLE_LEFT, tempBook1.getName());
			map.put(ActivityBase.KEY_DESC_LEFT, tempBook1.getDescription());
			map.put(ActivityBase.KEY_BOOK_ADDER_ID_LEFT, tempBook1.getAdderId().toString());
			if (tempBook2 != null) {
//				String tempBook2CoverURL = new GetNetmerMediaTask().execute(tempBook2).get();//getNetmeraMedia(ApplicationConstants.book_coverPhoto);
				map.put(ActivityBase.KEY_BOOK_ID_RIGHT, tempBook2.getBookId().toString());
				map.put(ActivityBase.KEY_COVER_RIGHT, tempBook2.getCoverPhoto());
				map.put(ActivityBase.KEY_BOOK_TITLE_RIGHT, tempBook2.getName());
				map.put(ActivityBase.KEY_DESC_RIGHT, tempBook2.getDescription());
				map.put(ActivityBase.KEY_BOOK_ADDER_ID_RIGHT, tempBook2.getAdderId().toString());
			}

			mainListToView.add(map);
		}
	}
    
}
