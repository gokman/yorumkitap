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
import com.bookworm.common.DeletetDataTask;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.ImageLoader;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.SelectDataTask;
import com.bookworm.common.UpdateDataTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraMedia;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.util.HttpUtils;

public class ProfileEditActivity extends ActivityBase implements OnClickListener{

	
    public ImageLoader imageLoader;
    private Uri fileUri;
    
    private String user_param;
    private ImageView explore_button;
    private ImageView home_button;
    private ImageView add_book_button;
	private ImageView profile_button;
	private EditText txtUsername;
	private EditText txtAbout;
	private ImageView imgProfilePhoto;
	private Button btnBrowse;
	private Button btnUpdate;
    private static final int REQUEST_ID = 1;
    private static final int HALF = 2;
    private NetmeraContent user;
    private Uri profilePhotoUri; 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        setContentView(R.layout.profile_edit);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);        

		NetmeraClient.init(this, apiKey);

		imageLoader=new ImageLoader(this.getApplicationContext());

		explore_button = (ImageView)findViewById(R.id.explore_button);
		home_button = (ImageView)findViewById(R.id.home_button);
        add_book_button = (ImageView)findViewById(R.id.add_button);
        profile_button = (ImageView)findViewById(R.id.profile_button);
        
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
        	List<NetmeraContent > userList = new SelectDataTask().execute(service).get();
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
        
        
        /**
         * footer buttons
         */
		profile_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(profileIntent);
				
			}
		});
		
		explore_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent exploreIntent = new Intent(getApplicationContext(), ProfileEditActivity.class);
				startActivity(exploreIntent);
			}
		});
		home_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(homeIntent);
			}
		});		
		add_book_button.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					PackageManager packageManager = getPackageManager();
					boolean doesHaveCamera = packageManager
							.hasSystemFeature(PackageManager.FEATURE_CAMERA);

					if (doesHaveCamera) {
						Camera mCamera = Camera.open();
						// start the image capture Intent
						Camera.Parameters cp = mCamera.getParameters();

						Size cameraResolution = cp.getPictureSize();
						mCamera.release();
						if (cameraResolution.height > 1024
								&& cameraResolution.width > 1024) {
							Toast.makeText(getApplicationContext(),
									"Camera resolution must be decreased.",
									Toast.LENGTH_LONG).show();
						} else {
							Intent intent = new Intent(
									MediaStore.ACTION_IMAGE_CAPTURE);
							// Get our fileURI
							fileUri = getOutputMediaFile();
							intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
							startActivityForResult(intent, 100);
						}
					}
				} catch (Exception ex) {
					Toast.makeText(getApplicationContext(),
							"There was an error with the camera.",
							Toast.LENGTH_LONG).show();
				}
			}
		});

    }
	public void onClick(View v) {
		
	}

    @Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		 InputStream stream = null;
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {

				Intent newbookIntent = new Intent(this, AddBookActivity.class);
				newbookIntent.putExtra("newBookImageURI", fileUri.toString());

				startActivity(newbookIntent);

			}
		}
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
    
    
	private Uri getOutputMediaFile() throws IOException {
		File mediaStorageDir = new File(
				Environment
						.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
				"DayTwentyNine");
		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(new Date());
		File mediaFile;
		mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");

		if (mediaFile.exists() == false) {
			mediaFile.getParentFile().mkdirs();
			mediaFile.createNewFile();
		}
		return Uri.fromFile(mediaFile);
	}
}
