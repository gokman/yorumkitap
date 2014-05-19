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
        TextView commenterUserName = (TextView)vi.findViewById(R.id.commenter_username);
        TextView commentId = (TextView)vi.findViewById(R.id.comment_id);
        
        final  HashMap<String, String> comment = data.get(position);
        
        // Setting all values in listview
        commentLeft.setText(comment.get(MainActivity.KEY_DESC_LEFT));
        commentOwnerLeft.setText(comment.get(MainActivity.KEY_BOOK_ADDER_ID_LEFT));
        commenterUserName.setText(comment.get(MainActivity.KEY_BOOK_ADDER_NAME_LEFT));
        commentId.setText(comment.get(ApplicationConstants.TYPE_COMMENT_ID));
//        imageLoader.DisplayImage(book.get(MainActivity.KEY_COVER_LEFT), image_left);
       
        return vi;
    }
}