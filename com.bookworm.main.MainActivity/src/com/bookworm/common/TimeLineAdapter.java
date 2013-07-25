package com.bookworm.common;

import java.util.ArrayList;

import java.util.HashMap;

import com.bookworm.main.BookDetailActivity;
import com.bookworm.main.MainActivity;
import com.bookworm.main.ProfileActivity;
import com.bookworm.main.TimeLineActivity;
import com.bookworm.main.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class TimeLineAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    public ImageLoader imageLoader; 
    private Intent bookDetailIntent;
    private Intent adderProfileIntent; 
    private Context context;
    
    public TimeLineAdapter(Context context,Activity a, ArrayList<HashMap<String,String>> d) {
        activity = a;
        data=d;
        mInflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        this.context=context;
        
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

	public View getView(int position, View convertView,ViewGroup parent) {
		
		//listelenen elemanları sıra ile çekiyoruz. Ona göre şekil vereceğiz
		final HashMap<String,String> eldekiSatir=data.get(position);
		//kitap için
		if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("BOOK")){
		convertView=mInflater.inflate(R.layout.timeline_page_row1, null);
		convertView.setTag(ApplicationConstants.TYPE_BOOK);
		  TextView titleLeft = (TextView)convertView.findViewById(R.id.timeline_book_title); 
		  TextView bookAdderId = (TextView)convertView.findViewById(R.id.timeline_book_adderId); 
	      TextView descLeft = (TextView)convertView.findViewById(R.id.timeline_desc); 
	      TextView bookOwner = (TextView)convertView.findViewById(R.id.timeline_book_adderId);
	      ImageView image_left =(ImageView)convertView.findViewById(R.id.timeline_list_image);
	      
	        //bütün değerleri listviewdaki elemana ata
	        bookAdderId.setText(eldekiSatir.get(ApplicationConstants.book_adderId));
	        titleLeft.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_NAME));
	        descLeft.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_DESC));
	        bookOwner.setText(eldekiSatir.get(ApplicationConstants.TYPE_BOOK_OWNER));
	        imageLoader.DisplayImage(eldekiSatir.get(ApplicationConstants.TYPE_COVER_URL), image_left);
		
		//comment için
		}else if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("COMMENT")){
			convertView=mInflater.inflate(R.layout.timeline_page_row3, null);
			convertView.setTag(ApplicationConstants.TYPE_COMMENT);
			  TextView commendator = (TextView)convertView.findViewById(R.id.textCommentator); 
		      TextView commentedBookOwner = (TextView)convertView.findViewById(R.id.bookOwner); 
		      TextView commentedBookName = (TextView)convertView.findViewById(R.id.commentedBookName); 
		      TextView commentDate = (TextView)convertView.findViewById(R.id.tl_commentDate); 
		      TextView commentedBookAdderId = (TextView)convertView.findViewById(R.id.timeline_follow_book_adderId); 
		      
		        //bütün değerleri listviewdaki elemana ata
		        commendator.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENDATOR));
		        commentedBookOwner.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER));
		        commentedBookName.setText(eldekiSatir.get(ApplicationConstants.TYPE_COMMENTEDBOOKNAME));
		        commentDate.setText(eldekiSatir.get(ApplicationConstants.CREATE_DATE));
		        commentedBookAdderId.setText(eldekiSatir.get(ApplicationConstants.book_adderId));
		        
		//takip için
		}else if(eldekiSatir.get(TimeLineActivity.ListElementType).toString().equals("FOLLOW")){
			convertView=mInflater.inflate(R.layout.timeline_page_row2, null);
			convertView.setTag(ApplicationConstants.TYPE_FOLLOW);
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


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

}
