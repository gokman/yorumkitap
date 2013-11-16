package com.bookworm.common;
 
import java.util.ArrayList;

import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bookworm.main.BookDetailActivity;
import com.bookworm.main.MainActivity;
import com.bookworm.main.ProfileActivity;
import com.bookworm.main.R;
 
public class CommentAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    
    public CommentAdapter(Activity a, ArrayList<HashMap<String,String>> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
        
    }
    public int getCount() {
        return data.size();
    }
 
    public Object getItem(int position) {
        return position;
    }
 
    public long getItemId(int position) {
        return position;
    }
 
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.comment_row, null);
        
      
        TextView commentOwnerLeft = (TextView)vi.findViewById(R.id.comment_owner_left); 
        TextView commentLeft = (TextView)vi.findViewById(R.id.comment_left); 
        ImageView image_left =(ImageView)vi.findViewById(R.id.list_imageLeft);
        
        TextView commentOwnerRight = (TextView)vi.findViewById(R.id.comment_owner_right); 
        TextView commentRight= (TextView)vi.findViewById(R.id.comment_right); 
        ImageView image_right =(ImageView)vi.findViewById(R.id.list_imageRight);
        
        LinearLayout layoutRight = (LinearLayout)vi.findViewById(R.id.coverRight);
        View secondDivider = (View)vi.findViewById(R.id.secondDivider);
        
        final  HashMap<String, String> book = data.get(position);

        
        
        // Setting all values in listview
        commentLeft.setText(book.get(MainActivity.KEY_DESC_LEFT));
        commentOwnerLeft.setText(book.get(MainActivity.KEY_BOOK_ADDER_ID_LEFT));
//        imageLoader.DisplayImage(book.get(MainActivity.KEY_COVER_LEFT), image_left);
        

		 
        if(book.containsKey(MainActivity.KEY_DESC_RIGHT)){
	        commentRight.setText(book.get(MainActivity.KEY_DESC_RIGHT));
	        commentOwnerRight.setText(book.get(MainActivity.KEY_BOOK_ADDER_ID_RIGHT));
//	        imageLoader.DisplayImage(book.get(MainActivity.KEY_COVER_RIGHT), image_right);
	        commentRight.setVisibility(View.VISIBLE);
    		image_right.setVisibility(View.VISIBLE);
    		commentOwnerRight.setVisibility(View.VISIBLE);
    		layoutRight.setVisibility(View.VISIBLE);
    		secondDivider.setVisibility(View.VISIBLE);
         

        }else{
    		commentRight.setVisibility(View.INVISIBLE);
    		image_right.setVisibility(View.INVISIBLE);
    		commentOwnerRight.setVisibility(View.INVISIBLE);
    		layoutRight.setVisibility(View.INVISIBLE);
    		secondDivider.setVisibility(View.INVISIBLE);
    	}
        return vi;
    }
}