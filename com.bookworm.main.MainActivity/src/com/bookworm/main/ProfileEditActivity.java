package com.bookworm.main;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
//import com.bookworm.common.DeletetDataTask;
//import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.ImageLoader;
//import com.bookworm.common.InsertDataTask;
//import com.bookworm.common.SelectDataTask;
//import com.bookworm.common.UpdateDataTask;
//import com.netmera.mobile.NetmeraClient;
//import com.netmera.mobile.NetmeraContent;
//import com.netmera.mobile.NetmeraMedia;
//import com.netmera.mobile.NetmeraService;
//import com.netmera.mobile.util.HttpUtils;

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
   // private NetmeraContent user;
    private Uri profilePhotoUri; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	/*
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.profile_edit);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

		//NetmeraClient.init(this, apiKey);

		imageLoader=new ImageLoader(this.getApplicationContext());

        txtUsername = (EditText)findViewById(R.id.edit_profile_uname);
        txtAbout = (EditText)findViewById(R.id.edit_profile_about);
        imgProfilePhoto = (ImageView)findViewById(R.id.edit_profile_photo);
        btnBrowse = (Button)findViewById(R.id.btnBrowse);
        btnUpdate = (Button)findViewById(R.id.btnUpdateProfile);
        
		Intent myIntent = getIntent();
		user_param = myIntent.getStringExtra(ApplicationConstants.userEmailParam);

		
        
        NetmeraService service = new NetmeraService(ApplicationConstants.user);
        service.whereEqual(ApplicationConstants.user_email, user_param);
        try{
        	List<NetmeraContent > userList = new SelectDataTask(ProfileEditActivity.this).execute(service).get();
        	user = userList.get(0);
        	user.add(ApplicationConstants.generic_property, ApplicationConstants.user_userProfile);
        	
        	txtUsername.setText(user.get(ApplicationConstants.user_username).toString());
        	txtAbout.setText(user.get(ApplicationConstants.user_about).toString());
        	String profilePhotoURL = new GetNetmerMediaTask().execute(user).get();
        	if(profilePhotoURL!=null && !profilePhotoURL.equals("") && !profilePhotoURL.equals("-")){
        		imageLoader.DisplayImage(profilePhotoURL, imgProfilePhoto);
        	}else{
        		imgProfilePhoto.setImageResource(R.drawable.no_image);
        	}
        	
        }catch(Exception ex ){
        	ex.printStackTrace();
        }
        
        
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
					NetmeraContent newUser = new NetmeraContent(ApplicationConstants.user);
					newUser.add(ApplicationConstants.user_about, txtAbout.getText().toString());
					newUser.add(ApplicationConstants.user_username, txtUsername.getText().toString());
					newUser.add(ApplicationConstants.user_email,user.get(ApplicationConstants.user_email).toString());
					newUser.add(ApplicationConstants.generic_property,ApplicationConstants.user_userProfile);
					
					Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), profilePhotoUri);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
		    		bitmap.compress(CompressFormat.PNG, 0, bos);

		    		NetmeraMedia profilePhoto = new NetmeraMedia(bos.toByteArray());
		    		newUser.add(ApplicationConstants.user_userProfile, profilePhoto);
					
		    		new InsertDataTask().execute(newUser).get();
					new DeletetDataTask().execute(user).get();
					
					Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
					profileIntent.putExtra(ApplicationConstants.userEmailParam,newUser.get(ApplicationConstants.user_email).toString());
		    		startActivity(profileIntent);
				}catch(Exception ex){
					ex.printStackTrace();
				}
				
			}
		});
        */
        
        /**
         * footer buttons
         */
        setNavigationButtons();
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
