package com.bookworm.common;

import java.util.ArrayList;

import java.util.HashMap;

import com.bookworm.main.MainActivity;
import com.bookworm.main.TimeLineActivity;

import com.bookworm.main.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimeLineAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    public ImageLoader imageLoader; 
    private Intent bookDetailIntent;
    private Intent adderProfileIntent; 
    
    public TimeLineAdapter(Activity a, ArrayList<HashMap<String,String>> d) {
        activity = a;
        data=d;
        mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
    }
    

	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		
		//listelenen elemanları sıra ile çekiyoruz. Ona göre şekil vereceğiz
		final HashMap<String,String> eldekiSatir=data.get(position);
		//kitap için
		if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("BOOK")){
		convertView=mInflater.inflate(R.layout.timeline_page_row1, null);
		
		  TextView hiddenVal = (TextView)convertView.findViewById(R.id.tl_book_hidden);
		  TextView titleLeft = (TextView)convertView.findViewById(R.id.timeline_book_title); 
	      TextView descLeft = (TextView)convertView.findViewById(R.id.timeline_desc); 
	      TextView bookAdderIdLeft = (TextView)convertView.findViewById(R.id.timeline_book_owner); 
	      ImageView image_left =(ImageView)convertView.findViewById(R.id.timeline_list_image);
	      
	        //bütün değerleri listviewdaki elemana ata
	        hiddenVal.setText(ApplicationConstants.TYPE_BOOK);
	        titleLeft.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_NAME));
	        descLeft.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_DESC));
	        bookAdderIdLeft.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_OWNER));
	        imageLoader.DisplayImage(eldekiSatir.get(ApplicationConstants.TYPE_COVER_URL), image_left);
		
		//comment için
		}else if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("COMMENT")){
			convertView=mInflater.inflate(R.layout.timeline_page_row3, null);
			
			  TextView hiddenVal = (TextView)convertView.findViewById(R.id.tl_comment_hidden);
			  TextView commendator = (TextView)convertView.findViewById(R.id.textCommentator); 
		      TextView commentedBookOwner = (TextView)convertView.findViewById(R.id.bookOwner); 
		      TextView commentedBookName = (TextView)convertView.findViewById(R.id.commentedBookName); 
		      TextView commentDate = (TextView)convertView.findViewById(R.id.tl_commentDate); 
		      
		        //bütün değerleri listviewdaki elemana ata
		        hiddenVal.setText(ApplicationConstants.TYPE_COMMENT);
		        commendator.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENDATOR));
		        commentedBookOwner.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER));
		        commentedBookName.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENTEDBOOKNAME));
		        commentDate.setText(eldekiSatir.get(ApplicationConstants.CREATE_DATE));
		        
		//takip için
		}else if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("FOLLOW")){
			convertView=mInflater.inflate(R.layout.timeline_page_row2, null);
			
			  TextView follower = (TextView)convertView.findViewById(R.id.textFollower); 
		      TextView followed = (TextView)convertView.findViewById(R.id.textFollowed); 
		      TextView followship = (TextView)convertView.findViewById(R.id.textFollowship); 
		      TextView followdate = (TextView)convertView.findViewById(R.id.tl_follow_date); 
		      
		        //bütün değerleri listviewdaki elemana ata
		        follower.setText(eldekiSatir.get(ApplicationConstants.TYPE_FOLLOWER));
		        followed.setText(eldekiSatir.get(ApplicationConstants.TYPE_FOLLOWED));
		        followship.setText(eldekiSatir.get(ApplicationConstants.TYPE_FOLLOWSHIP));
		        followdate.setText(eldekiSatir.get(ApplicationConstants.TYPE_FOLLOWDATE));
		}
		return convertView;
	}

}
