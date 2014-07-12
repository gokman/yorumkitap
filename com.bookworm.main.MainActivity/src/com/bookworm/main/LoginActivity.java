package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.EMPTY_STRING;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_LOGIN;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_REGISTER;
import static com.bookworm.common.ApplicationConstants.password;
import static com.bookworm.common.ApplicationConstants.sharedPrefName;
import static com.bookworm.common.ApplicationConstants.unregistered_password;
import static com.bookworm.common.ApplicationConstants.unregistered_username;
import static com.bookworm.common.ApplicationConstants.username;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.LoginPlatform;
import com.bookworm.model.User;
import com.bookworm.operation.UserOperation;
import com.bookworm.util.Validation;
import com.bookworm.ws.user.LoginWS;
import com.bookworm.ws.user.RegisterWS;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

public class LoginActivity extends Activity implements OnClickListener {

	static String resultEmail;
	boolean registered = false;

	private SharedPreferences SP;
	private EditText _email;
	private EditText _password;
	private CheckBox _rememberMe;
	private TextView _forgotPassword;
	private String savedUsername = EMPTY_STRING;
	private String savedPassword = EMPTY_STRING;
	private String loginResult="a";
	
	//facebook variables
	private MainFragment mainFragment;
	private LoginButton faceLoginBtn;
	private static final List<String> PERMISSIONS = new ArrayList<String>() {
        {
            add("email");
            add("public_profile");
        }
    };
    private UiLifecycleHelper uiHelper;
    private static final String TAG = "MainFragment";
    
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
		
		//facebook elemanları
		faceLoginBtn = (LoginButton)findViewById(R.id.facebookLoginButton);
		faceLoginBtn.setReadPermissions(PERMISSIONS);
		uiHelper = new UiLifecycleHelper(this, new Session.StatusCallback() {       
	            public void call(Session session, SessionState state, Exception exception) {
	                onSessionStateChange(session, state, exception);
	            }
	        });
	        uiHelper.onCreate(savedInstanceState);

		SP = getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);

		savedUsername = SP.getString(username,unregistered_username);
		savedPassword = SP.getString(password,unregistered_password);
		
		_rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		_forgotPassword = (TextView) findViewById(R.id.forgotPassword);
		
		_email = (EditText)findViewById(R.id.emailText);
		_password = (EditText)findViewById(R.id.passwordText);
		
		if(!savedUsername.equals(unregistered_username)){
			_email.setText(savedUsername);
		}
		if(!savedPassword.equals(unregistered_password)){
			_password.setText(savedPassword);
		}		
		
		// Listening to register new account link
		TextView registerScreen = (TextView) findViewById(R.id.link_to_register);
		registerScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(getApplicationContext(),
						RegisterActivity.class);
				startActivity(i);
			}
		});
		
		 _email.addTextChangedListener(new TextWatcher() {
	            // after every change has been made to this editText, we would like to check validity
	            public void afterTextChanged(Editable s) {
	                Validation.isEmailValid(_email);
	            }
	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
	        
	        _password.addTextChangedListener(new TextWatcher() {
	            // after every change has been made to this editText, we would like to check validity
	            public void afterTextChanged(Editable s) {
	                Validation.isPasswordValid(_password);
	            }
	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });

		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(checkValidation()){
				
				try {
					String credentials []= {_email.getText().toString(),_password.getText().toString()};
					//new LoginUserDataTask().execute(credentials).get();
					loginResult=new LoginWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LOGIN,
							_email.getText().toString(),_password.getText().toString()).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				//if (!registered) {
				if(!loginResult.substring(0, 3).equals("200")){
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					Toast.makeText(getApplicationContext(),
							getString(R.string.wrongEmailPassword), Toast.LENGTH_LONG)
							.show();
				} else {
					/*
					 * Eger login olan kullanici;
					 * sifresi kayitli olan kullan�c�ysa ve "beni hatirla" secenegi secilmemisse
					 * kayitli sifre ve kullanici bilgileri
					 * else kisminda siliniyor.
					 */
					if(_rememberMe.isChecked()){
						String _emailToStore = _email.getText().toString();
						String _passwordToStore = _password.getText().toString();

						SharedPreferences.Editor editit = SP.edit();
						editit.putString(username, _emailToStore);
						editit.putString(password,_passwordToStore);
						editit.commit();

					}else{
						if(savedUsername.equals(_email.getText().toString())){
							SharedPreferences.Editor editit = SP.edit();
							editit.remove(username);
							editit.remove(password);
							editit.commit();
						}
						
					}
					SharedPreferences.Editor editit = SP.edit();
					editit.clear();
					//email ve kullanici adi kaydedilir
					
					saveLoggedInUser(loginResult.substring(loginResult.indexOf(":",loginResult.indexOf(":")+1)+1,loginResult.lastIndexOf(":")), 
							        _password.getText().toString(), 
							        Long.parseLong(loginResult.substring(loginResult.lastIndexOf(":")+1)), 
							        loginResult.substring(loginResult.indexOf(":")+1,loginResult.indexOf(":",loginResult.indexOf(":")+1)));
					//ApplicationConstants.signed_in_email=loginResult.substring(loginResult.indexOf(":")+1,loginResult.indexOf(":",loginResult.indexOf(":")+1));
					//ApplicationConstants.signed_in_username=loginResult.substring(loginResult.indexOf(":",loginResult.indexOf(":")+1)+1,loginResult.lastIndexOf(":"));
					//ApplicationConstants.signed_in_userid=Long.parseLong(loginResult.substring(loginResult.lastIndexOf(":")+1));
					//ApplicationConstants.signed_in_password=_password.getText().toString();
					
					//Intent mainPageIntent = new Intent(getApplicationContext(),
					//		MainActivity.class);
					//startActivity(mainPageIntent);
				}
				}
			}
		});
		
		if(!savedUsername.equals(unregistered_username) && !savedPassword.equals(unregistered_password)){
			btnLogin.performClick();
		}
		_forgotPassword.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {
				Intent mainPageIntent = new Intent(getApplicationContext(),
						EmailForResetTokenActivity.class);
				startActivity(mainPageIntent);
				
			}	
		});
	
		//facebook login başla
		/* if (savedInstanceState == null) {
		        // Add the fragment on initial activity setup
		        mainFragment = new MainFragment();
		        getSupportFragmentManager()
		        .beginTransaction()
		        .add(android.R.id.content, mainFragment)
		        .commit();
		    } else {
		        // Or set the fragment from restored state info
		        mainFragment = (MainFragment) getSupportFragmentManager()
		        .findFragmentById(android.R.id.content);
		    }*/
		
		//facebook login bitir

	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuInflater = getMenuInflater();
		menuInflater.inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_about:
			return true;
		default:
			super.onContextItemSelected(item);
			return true;

		}
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	    Session.getActiveSession().onActivityResult(this, 
	    		                                    requestCode, resultCode, intent);
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {

			}
		}
		uiHelper.onActivityResult(requestCode, resultCode, intent);
	}

	@Override
	public void onSaveInstanceState(final Bundle b) {
		super.onSaveInstanceState(b);
	}

	@Override
	public void onRestoreInstanceState(final Bundle b) {
		super.onRestoreInstanceState(b);
	}

	private boolean checkValidation(){
		
		if(!Validation.isEmailValid(_email))
		return false;
		
		if(!Validation.isPasswordValid(_password))
		return false;
		
		return true;
	}


	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	//facebook methods
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
		   /* if (state.isOpened() && !sessionHasNecessaryPerms(session)) {
		    	 session.requestNewReadPermissions(
	                     new NewPermissionsRequest(
	                             MainFragment.this, 
	                             getMissingPermissions(session)));   */
			if (state.isOpened()){
		        Log.i(TAG, "Logged in...");
		        makeMeRequest(session);
		    } else if (state.isClosed()) {
		        Log.i(TAG, "Logged out...");
		    }
    }
	
	private void makeMeRequest(final Session session) {
		// Make an API call to get user data and define a
		// new callback to handle the response.
		Request request = Request.newMeRequest(session,
		new Request.GraphUserCallback() {
		
		public void onCompleted(GraphUser user, Response response) {
		// If the response is successful
		if (session == Session.getActiveSession()) {
		if (user != null) {

		String fullName = user.getName();
		String firstName = user.getFirstName();
		String lastName = user.getLastName();
		String fbId = user.getId();
		String username = user.getUsername();
		String email = (String) user.asMap().get("email");
		
		
			try {
				    String loginResult="";
					//böyle bir kullanıcı var ise login ol
					if(new UserOperation().isUserExist(new User(email))){
						
						try {
							String credentials []= {email,fbId};
							//new LoginUserDataTask().execute(credentials).get();
							loginResult=new LoginWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LOGIN,
									credentials[0],credentials[1]).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
						saveLoggedInUser(username, fbId, Long.parseLong(fbId), email);
						
					//kullanıcı mevcut değil ise kaydet ve login ol
					}else{
						User facebookUser = new User(fullName,email,fbId,"",
								                      Integer.valueOf(1),
								                      Integer.valueOf(1));
						
						String result=new RegisterWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_REGISTER,
			            		                 facebookUser).get();
			                
			            try {
							String credentials []= {email,fbId};
							//new LoginUserDataTask().execute(credentials).get();
							loginResult=new LoginWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_LOGIN,
									credentials[0],credentials[1]).get();
						} catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						}
			            
			            saveLoggedInUser(username, fbId, Long.parseLong(fbId), email);
			            
					}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		}
		if (response.getError() != null) {
		// Handle errors, will do so later.
		}
		}

		});

		request.executeAsync();
		} 
	
	@Override
	public void onResume() {
	    super.onResume();
	    
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }
	    
	    uiHelper.onResume();
	}

	@Override
	public void onPause() {
	    super.onPause();
	    uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
	    super.onDestroy();
	    uiHelper.onDestroy();
	}
	
	public void saveLoggedInUser(String username,String password,Long userId,String email){
		
		ApplicationConstants.signed_in_email=email;
		ApplicationConstants.signed_in_username=username;
		ApplicationConstants.signed_in_userid=userId;
		ApplicationConstants.signed_in_password=password;
		
		Intent mainPageIntent = new Intent(getApplicationContext(),MainActivity.class);
		startActivity(mainPageIntent);
		
		
	}
	
}