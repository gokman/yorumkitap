package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LIST;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_UPDATE;

import java.io.InputStream;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.ImageLoader;
import com.bookworm.model.User;
import com.bookworm.util.SearchCriteria;
import com.bookworm.ws.user.ListUsersWS;
import com.bookworm.ws.user.UpdateUserWS;

public class ProfileEditActivity extends ActivityBase implements OnClickListener{

	
    public ImageLoader imageLoader;
    private Uri fileUri;
    
    private String user_param;
	private EditText txtUsername;
	private EditText txtAbout;
	private ImageView imgProfilePhoto;
	private Button btnBrowse;
	private Button btnUpdate;
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;
    private Uri profilePhotoUri; 
    private User user ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.profile_edit);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

		imageLoader=new ImageLoader(this.getApplicationContext());

        txtUsername = (EditText)findViewById(R.id.edit_profile_uname);
        txtAbout = (EditText)findViewById(R.id.edit_profile_about);
        imgProfilePhoto = (ImageView)findViewById(R.id.edit_profile_photo);
        btnBrowse = (Button)findViewById(R.id.btnBrowse);
        btnUpdate = (Button)findViewById(R.id.btnUpdateProfile);
        
		SearchCriteria sc = new SearchCriteria();
		sc.setUserId(ApplicationConstants.signed_in_userid);
		List<User> usersList;
		try {
			usersList = new ListUsersWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LIST+"/",
					sc,
					ApplicationConstants.signed_in_email,
					ApplicationConstants.signed_in_password
					).get();
			
        user = usersList.get(0);
        
        
        	txtUsername.setText(user.getUserName());
        	txtAbout.setText(user.getAbout());
//        	String profilePhotoURL = new GetNetmerMediaTask().execute(user).get();
//        	if(profilePhotoURL!=null && !profilePhotoURL.equals("") && !profilePhotoURL.equals("-")){
//        		imageLoader.DisplayImage(profilePhotoURL, imgProfilePhoto);
//        	}else{
        		imgProfilePhoto.setImageResource(R.drawable.no_image);
//        	}
        	
        btnBrowse.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View paramView) {
		        Intent intent = new Intent();
		        intent.setAction(Intent.ACTION_GET_CONTENT);
		        intent.addCategory(Intent.CATEGORY_OPENABLE);
		        intent.setType("image/*");
		        startActivityForResult(intent, REQUEST_ID);			
				
			}
		});
        
        
        btnUpdate.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View paramView) {
				try{
					user.setAbout(txtAbout.getText().toString());
					user.setUserName(txtUsername.getText().toString());
					user.setUserId(ApplicationConstants.signed_in_userid);
					new UpdateUserWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_UPDATE,
							user,
							ApplicationConstants.signed_in_email,
							ApplicationConstants.signed_in_password
							).get();
					
					
					Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
					profileIntent.putExtra(ApplicationConstants.userEmailParam,ApplicationConstants.signed_in_userid);
		    		startActivity(profileIntent);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
		});
        
        
        /**
         * footer buttons
         */
        setNavigationButtons();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
	public void onClick(View v) {
		
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		super.onActivityResult(requestCode, resultCode);
		
		InputStream stream = null;
		
		 if (requestCode == REQUEST_ID && resultCode == Activity.RESULT_OK) {
			try {
				profilePhotoUri = intent.getData();
				stream = getContentResolver().openInputStream(intent.getData());
				Bitmap original = BitmapFactory.decodeStream(stream);
			    imgProfilePhoto.setImageBitmap(Bitmap.createScaledBitmap(original,
								original.getWidth() / HALF,
								original.getHeight() / HALF, true));
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (stream != null) {
				try {
					stream.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}		
		
	}
}
