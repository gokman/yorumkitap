package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;

public class ActivityBase extends Activity{
	static final String apiKey ="WVhCd1ZYSnNQV2gwZEhBbE0wRWxNa1lsTWtZek1UUTNNREUzTmk1dVpYUnRaWEpoTG1OdmJTVXpRVGd3SlRKR2JXOWlhVzFsY21FbE1rWm5ZV1JuWlhRbE1rWm9iMjFsTG5odGJDWnViVk5wZEdWVmNtdzlhSFIwY0NVelFTVXlSaVV5UmpNeE5EY3dNVGMyTG01bGRHMWxjbUV1WTI5dEpUTkJPREFtYlc5a2RXeGxTV1E5T1RReU15WmhjSEJKWkQwek1UUTNNREUzTmladWJWUmxiWEJzWVhSbFBXMXZZbWwwWlcxd2JHRjBaU1p2ZDI1bGNrbGtQV1poZEdsb2VXVnphV3hrWVd3bVpHOXRZV2x1UFc1bGRHMWxjbUV1WTI5dEptNXRVMmwwWlQwek1UUTNNREUzTmladmQyNWxjbEp2YkdWVWVYQmxQVEVtZG1sbGQyVnlVbTlzWlZSNWNHVTlNU1oyYVdWM1pYSkpaRDFtWVhScGFIbGxjMmxzWkdGc0pn";


	private Uri fileUri;
	final Context context = this; 
	private ImageView explore_button;
	private ImageView home_button;
	private ImageView add_book_button;
	private ImageView profile_button;
	private ImageView timeline_button;
	private ImageView logout_button;
	
	public void setNavigationButtons(){

        setExplore_button((ImageView)findViewById(R.id.explore_button));
		setHome_button((ImageView)findViewById(R.id.home_button));
		setAdd_book_button((ImageView)findViewById(R.id.add_button));
		setProfile_button((ImageView)findViewById(R.id.profile_button));
		setTimeline_button((ImageView)findViewById(R.id.timeline_button));
		setLogout_button((ImageView)findViewById(R.id.logout));
		
		explore_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent exploreIntent = new Intent(getApplicationContext(), ExploreActivity.class);
				startActivity(exploreIntent);
			}
		});
		timeline_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent timelineIntent = new Intent(getApplicationContext(), TimeLineActivity.class);
				startActivity(timelineIntent);
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
		profile_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(profileIntent);
				
			}
		});
		
		logout_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				/*
				 * 1.remove saved info(password and username) from shared preferences
				 * 2.redirect to login page
				 */

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
				 myAlertDialog.setTitle("Logout Warning");
				 myAlertDialog.setMessage("You're going to log out.Are you sure ?");
				 myAlertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

				  public void onClick(DialogInterface arg0, int arg1) {

						 SharedPreferences sharedPref= getSharedPreferences(ApplicationConstants.sharedPrefName,0);
						 SharedPreferences.Editor editor = sharedPref.edit();
						 editor.remove(ApplicationConstants.username);
						 editor.remove(ApplicationConstants.password);
						 
						 editor.commit(); //Don't forgot to commit  SharedPreferences.
						
						Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(logoutIntent);
					  
				  }});
				 myAlertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				       
				  public void onClick(DialogInterface arg0, int arg1) {
				  // do something when the Cancel button is clicked
				  }});
				 myAlertDialog.show();				
				
			}
		});
		
	}
	public void onActivityResult(int requestCode,int resultCode){
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {

				Matrix matrix = new Matrix();
				// rotate the Bitmap (there a problem with exif so we'll query the mediaStore for orientation
				Cursor cursor = getApplicationContext().getContentResolver().query(this.fileUri,
				      new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
				if (cursor !=null && cursor.getCount() == 1) {
				cursor.moveToFirst();
				    int orientation =  cursor.getInt(0);
				    matrix.preRotate(orientation);
				    }
				
				Intent newbookIntent = new Intent(this, AddBookActivity.class);
				newbookIntent.putExtra("newBookImageURI", fileUri.toString());

				startActivity(newbookIntent);

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

	
	public Uri getFileUri() {
		return fileUri;
	}
	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}
	public ImageView getExplore_button() {
		return explore_button;
	}
	public void setExplore_button(ImageView explore_button) {
		this.explore_button = explore_button;
	}
	public ImageView getHome_button() {
		return home_button;
	}
	public void setHome_button(ImageView home_button) {
		this.home_button = home_button;
	}
	public ImageView getAdd_book_button() {
		return add_book_button;
	}
	public void setAdd_book_button(ImageView add_book_button) {
		this.add_book_button = add_book_button;
	}
	public ImageView getProfile_button() {
		return profile_button;
	}
	public void setProfile_button(ImageView profile_button) {
		this.profile_button = profile_button;
	}
	public ImageView getTimeline_button() {
		return timeline_button;
	}
	public void setTimeline_button(ImageView timeline_button) {
		this.timeline_button = timeline_button;
	}
	public ImageView getLogout_button() {
		return logout_button;
	}
	public void setLogout_button(ImageView logout_button) {
		this.logout_button = logout_button;
	}

	
}
