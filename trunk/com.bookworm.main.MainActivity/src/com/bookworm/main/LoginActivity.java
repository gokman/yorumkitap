package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.bookworm.util.Validation;
import com.bookworm.ws.user.LoginWS;
public class LoginActivity extends ActivityBase implements OnClickListener {

	static String resultEmail;
	boolean registered = false;

	private SharedPreferences SP;

	private EditText _email;
	private EditText _password;
	private CheckBox _rememberMe;
	private CheckBox _forgotPassword;
	private String savedUsername = EMPTY_STRING;
	private String savedPassword = EMPTY_STRING;
	private String loginResult="a";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);

		
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		SP = getSharedPreferences(sharedPrefName, 0);

		savedUsername = SP.getString(username,unregistered_username);
		savedPassword = SP.getString(password,unregistered_password);
		
		_rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		_forgotPassword = (CheckBox) findViewById(R.id.forgotPassword);

		_forgotPassword.setChecked(false);
		
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
					ApplicationConstants.signed_in_email=loginResult.substring(loginResult.indexOf(":")+1, loginResult.lastIndexOf(":"));
					ApplicationConstants.signed_in_username=loginResult.substring(loginResult.lastIndexOf(":")+1);
					
					Intent mainPageIntent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(mainPageIntent);
				}
				}
			}
		});
		
		if(!savedUsername.equals(unregistered_username) && !savedPassword.equals(unregistered_password)){
			btnLogin.performClick();
		}
		_forgotPassword.setOnClickListener( new OnClickListener() {
			
			public void onClick(View v) {

				if(((CheckBox)v).isChecked()){
					
				}
			}
		});

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

	public void onClick(View v) {
	}

	
	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		if (requestCode == 100) {
			if (resultCode == RESULT_OK) {

			}
		}

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
}