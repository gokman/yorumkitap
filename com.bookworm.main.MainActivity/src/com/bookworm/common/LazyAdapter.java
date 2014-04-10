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
 
public class LazyAdapter extends BaseAdapter {
 
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    private Intent bookDetailIntent;
    private Intent adderProfileIntent; 
    
    
    public LazyAdapter(Activity a, ArrayList<HashMap<String,String>> d) {
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
        	try{
            vi = inflater.inflate(R.layout.main_page_row, null);
        	}catch(Exception e){
        		System.out.println(e.toString());
        	}
        
      
        TextView titleLeft = (TextView)vi.findViewById(R.id.book_title_left); 
        TextView descLeft = (TextView)vi.findViewById(R.id.desc_left); 
        TextView bookAdderIdLeft = (TextView)vi.findViewById(R.id.book_adder_id_left); 
        ImageView image_left =(ImageView)vi.findViewById(R.id.list_imageLeft);
        
        TextView titleRigth = (TextView)vi.findViewById(R.id.book_title_right); 
        TextView descRight = (TextView)vi.findViewById(R.id.desc_right); 
        TextView bookAdderIdRight = (TextView)vi.findViewById(R.id.book_adder_id_right); 
        ImageView image_right =(ImageView)vi.findViewById(R.id.list_imageRight);
        
        LinearLayout layoutRight = (LinearLayout)vi.findViewById(R.id.coverRight);
        View secondDivider = (View)vi.findViewById(R.id.secondDivider);
        
        final  HashMap<String, String> book = data.get(position);

        final String leftBookId = book.get(MainActivity.KEY_BOOK_ID_LEFT).toString();
        final String leftAdderId = book.get(MainActivity.KEY_BOOK_ADDER_ID_LEFT).toString();
        
        
        // Setting all values in listview
        titleLeft.setText(book.get(MainActivity.KEY_BOOK_TITLE_LEFT));
        descLeft.setText(book.get(MainActivity.KEY_DESC_LEFT));
        bookAdderIdLeft.setText(book.get(MainActivity.KEY_BOOK_ADDER_ID_LEFT));
        imageLoader.DisplayImage(book.get(MainActivity.KEY_COVER_LEFT), image_left);
        

        titleLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
	         bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
			 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(leftBookId));
			 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(leftAdderId));

				paramView.getContext().startActivity(bookDetailIntent);
			}
		});
        descLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
		         bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
				 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(leftBookId));
				 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(leftAdderId));				
				 paramView.getContext().startActivity(bookDetailIntent);
			}
		});
        image_left.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
		         bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
				 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(leftBookId));
				 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(leftAdderId));
				 paramView.getContext().startActivity(bookDetailIntent);
			}

        });
        bookAdderIdLeft.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {
		         adderProfileIntent = new Intent(paramView.getContext(),ProfileActivity.class);
		         adderProfileIntent.putExtra(ApplicationConstants.userEmailParam, Long.parseLong(leftAdderId));
				 paramView.getContext().startActivity(adderProfileIntent);
			}
		});        
        if(book.containsKey(MainActivity.KEY_BOOK_TITLE_RIGHT)){
	        titleRigth.setText(book.get(MainActivity.KEY_BOOK_TITLE_RIGHT));
	        descRight.setText(book.get(MainActivity.KEY_DESC_RIGHT));
	        bookAdderIdRight.setText(book.get(MainActivity.KEY_BOOK_ADDER_ID_RIGHT));
	        imageLoader.DisplayImage(book.get(MainActivity.KEY_COVER_RIGHT), image_right);
    		titleRigth.setVisibility(View.VISIBLE);
    		descRight.setVisibility(View.VISIBLE);
    		image_right.setVisibility(View.VISIBLE);
    		bookAdderIdRight.setVisibility(View.VISIBLE);
    		layoutRight.setVisibility(View.VISIBLE);
    		secondDivider.setVisibility(View.VISIBLE);
         
            final String rightBookId = book.get(MainActivity.KEY_BOOK_ID_RIGHT).toString();
            final String rightAdderId = book.get(MainActivity.KEY_BOOK_ADDER_ID_RIGHT).toString();

         adderProfileIntent = new Intent(vi.getContext(),ProfileActivity.class);
         adderProfileIntent.putExtra(ApplicationConstants.userEmailParam, book.get(MainActivity.KEY_BOOK_ADDER_ID_RIGHT));
   		 
           titleRigth.setOnClickListener(new View.OnClickListener() {
   			public void onClick(View paramView) {
   	    	 bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
			 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(rightBookId));
			 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(rightAdderId));
   				paramView.getContext().startActivity(bookDetailIntent);
   			}
   		});
           descRight.setOnClickListener(new View.OnClickListener() {
   			public void onClick(View paramView) {
      	    	 bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
    			 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(rightBookId));
    			 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(rightAdderId));
       	   		 paramView.getContext().startActivity(bookDetailIntent);
   			}
   		});
           image_right.setOnClickListener(new View.OnClickListener() {
   			public void onClick(View paramView) {
      	    	 bookDetailIntent = new Intent(paramView.getContext(),BookDetailActivity.class);
    			 bookDetailIntent.putExtra(ApplicationConstants.book_id, Long.parseLong(rightBookId));
    			 bookDetailIntent.putExtra(ApplicationConstants.book_adderId,Long.parseLong(rightAdderId));
       	   		 paramView.getContext().startActivity(bookDetailIntent);
   			}
   		});
           bookAdderIdRight.setOnClickListener(new View.OnClickListener() {
   			public void onClick(View paramView) {
		         adderProfileIntent = new Intent(paramView.getContext(),ProfileActivity.class);
		         adderProfileIntent.putExtra(ApplicationConstants.userEmailParam, Long.parseLong(rightAdderId));
				 paramView.getContext().startActivity(adderProfileIntent);   			}
   		});            		
    		
        }else{
    		titleRigth.setVisibility(View.INVISIBLE);
    		descRight.setVisibility(View.INVISIBLE);
    		image_right.setVisibility(View.INVISIBLE);
    		bookAdderIdRight.setVisibility(View.INVISIBLE);
    		layoutRight.setVisibility(View.INVISIBLE);
    		secondDivider.setVisibility(View.INVISIBLE);
    	}
        return vi;
    }
}