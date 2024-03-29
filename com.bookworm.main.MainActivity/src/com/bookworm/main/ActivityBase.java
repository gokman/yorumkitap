package com.bookworm.main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;

public class ActivityBase extends Activity{


	private Uri fileUri;
	final Context context = this; 
	private ImageButton explore_button;
	private  ImageButton home_button;
	private ImageButton profile_button;
	private ImageButton timeline_button;
	private ImageView logout_button;
	private ImageView language_button;
	private View layout;
	RadioGroup  languageRadioGroup;
	RadioButton turkishRadio;
	RadioButton englishRadio;
	PopupWindow pwindo;
	
	private final int ACTIVITY_CHOOSE_PHOTO = 41;
	public static final String KEY_COVER_LEFT = "coverLeft";
	public static final String KEY_BOOK_ID_LEFT = "book_id_left";
	public static final String KEY_COVER_RIGHT = "coverRight";
	public static final String KEY_BOOK_TITLE_LEFT = "book_title_left";
	public static final String KEY_BOOK_TITLE_RIGHT = "book_title_right";
	public static final String KEY_DESC_LEFT = "descLeft";
	public static final String KEY_DESC_RIGHT = "descRight";
	public static final String KEY_BOOK_ADDER_ID_RIGHT = "book_adder_id_right";
	public static final String KEY_BOOK_ADDER_ID_LEFT = "book_adder_id_left";
	public static final String KEY_BOOK_ADDER_NAME_RIGHT = "book_adder_name_right";
	public static final String KEY_BOOK_ADDER_NAME_LEFT = "book_adder_name_left";
	public static final String KEY_BOOK_ID_RIGHT = "book_id_right";	
	public void setNavigationButtons(){
     
        setExplore_button((ImageButton)findViewById(R.id.explore_button));
		setHome_button((ImageButton)findViewById(R.id.home_button));
		setProfile_button((ImageButton)findViewById(R.id.profile_button));
		setTimeline_button((ImageButton)findViewById(R.id.timeline_button));
		setLogout_button((ImageView)findViewById(R.id.logout));
		setLanguage_button((ImageView)findViewById(R.id.language_button));
		
		explore_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent exploreIntent = new Intent(getApplicationContext(), ExploreActivity.class);
				startActivity(exploreIntent);
				setFooterButtonState(explore_button);
			}
		});
		language_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				 LayoutInflater inflater = (LayoutInflater) ActivityBase.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						 layout = inflater.inflate(R.layout.language,
						 (ViewGroup) findViewById(R.id.popup_element));
						 pwindo = new PopupWindow(layout,150,100,true);
						 pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
						 languageRadioGroup=(RadioGroup)layout.findViewById(R.id.radioGroupLanguage);
						 turkishRadio=(RadioButton)layout.findViewById(R.id.radioButton1);
						 englishRadio=(RadioButton)layout.findViewById(R.id.radioButton2);
						 if(Locale.getDefault().toString().equals("en_US")){
							 englishRadio.setChecked(true);
						 }else{
							 turkishRadio.setChecked(true);
						 }
						 
						 languageRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
								
								public void onCheckedChanged(RadioGroup group, int checkedId) {
									switch (checkedId) {
									case R.id.radioButton1:
										
										// change language by onclick a button
							             Configuration newConfig = new Configuration();
							             newConfig.locale = new Locale("tr");
							             context.getResources().updateConfiguration(newConfig,context.getResources().getDisplayMetrics());
							             Locale.setDefault(newConfig.locale);
										
										pwindo.dismiss();
										break;
									case R.id.radioButton2:
										
										// change language by onclick a button
							             Configuration newConfig2 = new Configuration();
							             newConfig2.locale = new Locale("en");
							             context.getResources().updateConfiguration(newConfig2,context.getResources().getDisplayMetrics());
							             Locale.setDefault(newConfig2.locale);
										pwindo.dismiss();
										break;
									}
									
								}
							});
				
			}
		});
		
	
		
		timeline_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent timelineIntent = new Intent(getApplicationContext(), TimeLineActivity.class);
				startActivity(timelineIntent);
				setFooterButtonState(timeline_button);
			}
		});
		home_button.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent homeIntent = new Intent(getApplicationContext(),MainActivity.class);
				startActivity(homeIntent);
				setFooterButtonState(home_button);
			}
		});
		profile_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				Intent profileIntent = new Intent(getApplicationContext(), ProfileActivity.class);
				startActivity(profileIntent);
				setFooterButtonState(profile_button);
			}
		});
		
		logout_button.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				/*
				 * 1.remove saved info(password and username) from shared preferences
				 * 2.redirect to login page
				 */

				AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(context);
				 myAlertDialog.setTitle(getString(R.string.logoutWarningTitle));
				 myAlertDialog.setMessage(getString(R.string.logoutWarningMessage));
				 myAlertDialog.setPositiveButton(getString(R.string.okey), new DialogInterface.OnClickListener() {

				  public void onClick(DialogInterface arg0, int arg1) {

						 SharedPreferences sharedPref= getSharedPreferences(ApplicationConstants.sharedPrefName,0);
						 SharedPreferences.Editor editor = sharedPref.edit();
						 editor.remove(ApplicationConstants.username);
						 editor.remove(ApplicationConstants.password);
						 
						 editor.commit(); //Don't forgot to commit  SharedPreferences.
						
						Intent logoutIntent = new Intent(getApplicationContext(), LoginActivity.class);
						startActivity(logoutIntent);
					  
				  }});
				 myAlertDialog.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
				       
				  public void onClick(DialogInterface arg0, int arg1) {
				  // do something when the Cancel button is clicked
				  }});
				 myAlertDialog.show();				
				
			}
		});
		
	}
	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		switch (requestCode ){
			case 100:
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
				break;
			case ACTIVITY_CHOOSE_PHOTO:
				
			    String selectedImagePath;
			    //ADDED
			    String filemanagerstring;
			    
				   Uri selectedImageUri = data.getData();

	                //OI FILE Manager
	                filemanagerstring = selectedImageUri.getPath();

	                //MEDIA GALLERY
	                selectedImagePath = getPath(selectedImageUri);

	                //DEBUG PURPOSE - you can delete this if you want
	                if(selectedImagePath!=null)
	                    System.out.println(selectedImagePath);
	                else System.out.println("selectedImagePath is null");
	                if(filemanagerstring!=null)
	                    System.out.println(filemanagerstring);
	                else System.out.println("filemanagerstring is null");

	                //NOW WE HAVE OUR WANTED STRING
	                if(selectedImagePath!=null)
	                    System.out.println("selectedImagePath is the right one for you!");
	                else
	                    System.out.println("filemanagerstring is the right one for you!");
	            				
				break;
		}
		}

	public String getPath(Uri uri) {
	    String[] projection = { MediaStore.Images.Media.DATA };
	    Cursor cursor = managedQuery(uri, projection, null, null, null);
	    if(cursor!=null)
	    {
	        //HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
	        //THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
	        int column_index = cursor
	        .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
	    else return null;
	}	
	public Uri getFileUri() {
		return fileUri;
	}
	public void setFileUri(Uri fileUri) {
		this.fileUri = fileUri;
	}
	public ImageButton getExplore_button() {
		return explore_button;
	}
	public void setExplore_button(ImageButton explore_button) {
		this.explore_button = explore_button;
	}
	public ImageButton getHome_button() {
		return home_button;
	}
	public void setHome_button(ImageButton home_button) {
		this.home_button = home_button;
	}
	public ImageButton getProfile_button() {
		return profile_button;
	}
	public void setProfile_button(ImageButton profile_button) {
		this.profile_button = profile_button;
	}
	public ImageView getLanguage_button() {
		return language_button;
	}
	public void setLanguage_button(ImageView language_button) {
		this.language_button = language_button;
	}
	public ImageButton getTimeline_button() {
		return timeline_button;
	}
	public void setTimeline_button(ImageButton timeline_button) {
		this.timeline_button = timeline_button;
	}
	public ImageView getLogout_button() {
		return logout_button;
	}
	public void setLogout_button(ImageView logout_button) {
		this.logout_button = logout_button;
	}
	public RadioButton getTurkishRadio() {
		return turkishRadio;
	}
	public void setTurkishRadio(RadioButton turkishRadio) {
		this.turkishRadio = turkishRadio;
	}
	public RadioButton getEnglishRadio() {
		return englishRadio;
	}
	public void setEnglishRadio(RadioButton englishRadio) {
		this.englishRadio = englishRadio;
	}
	
	protected void setFooterButtonState(ImageButton button){
		if(button==getHome_button()){
			button.setPressed(true);
			getExplore_button().setPressed(false);
			getProfile_button().setPressed(false);
			getTimeline_button().setPressed(false);
		}else if(button==getExplore_button()){
			button.setPressed(true);
			getHome_button().setPressed(false);
			getProfile_button().setPressed(false);
			getTimeline_button().setPressed(false);
		}else if(button==getProfile_button()){
			button.setPressed(true);
			getExplore_button().setPressed(false);
			getHome_button().setPressed(false);
			getTimeline_button().setPressed(false);
		}else if(button==getTimeline_button()){
			button.setPressed(true);
			getExplore_button().setPressed(false);
			getProfile_button().setPressed(false);
			getHome_button().setPressed(false);
		}
	
	}
	
}
