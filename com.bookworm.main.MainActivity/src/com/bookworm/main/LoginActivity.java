package com.bookworm.main;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
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
import com.bookworm.test.DataHelper;
import com.bookworm.test.MyApplication;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraUser;
public class LoginActivity extends ActivityBase implements OnClickListener {

	static String resultEmail;
	static NetmeraContent temp;
	boolean registered = false;

	public static String filename = "let_the_books_begin";
	private SharedPreferences SP;
	private DataHelper dataHelper;

	private EditText _email;
	private EditText _password;
	private CheckBox _rememberMe;
	private CheckBox _forgotPassword;
	private String savedUsername = ApplicationConstants.EMPTY_STRING;
	private String savedPassword = ApplicationConstants.EMPTY_STRING;;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.dataHelper = new DataHelper(this);

		
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		SP = getSharedPreferences(filename, 0);

		savedUsername = SP.getString(ApplicationConstants.username,ApplicationConstants.unregistered_username);
		savedPassword = SP.getString(ApplicationConstants.password,ApplicationConstants.unregistered_password);
		
		_rememberMe = (CheckBox) findViewById(R.id.rememberMe);
		_forgotPassword = (CheckBox) findViewById(R.id.forgotPassword);

		_forgotPassword.setChecked(false);
		
		_email = (EditText)findViewById(R.id.emailText);
		_password = (EditText)findViewById(R.id.passwordText);
		
		if(!savedUsername.equals(ApplicationConstants.unregistered_username)){
			_email.setText(savedUsername);
		}
		if(!savedPassword.equals(ApplicationConstants.unregistered_password)){
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

		NetmeraClient.init(this, apiKey);
		Button btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				try {
					String credentials []= {_email.getText().toString(),_password.getText().toString()};
					new LoginUserDataTask().execute(credentials).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				if (!registered) {
					getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
					Toast.makeText(getApplicationContext(),
							"Email or password is wrong", Toast.LENGTH_LONG)
							.show();
				} else {
					/*
					 * Eger login olan kullanici;
					 * sifresi kayitli olan kullanýcýysa ve "beni hatirla" secenegi secilmemisse
					 * kayitli sifre ve kullanici bilgileri
					 * else kisminda siliniyor.
					 */
					if(_rememberMe.isChecked()){
						String _emailToStore = _email.getText().toString();
						String _passwordToStore = _password.getText().toString();

						SharedPreferences.Editor editit = SP.edit();
						editit.putString(ApplicationConstants.username, _emailToStore);
						editit.putString(ApplicationConstants.password,_passwordToStore);
						editit.commit();

					}else{
						if(savedUsername.equals(_email.getText().toString())){
							SharedPreferences.Editor editit = SP.edit();
							editit.remove(ApplicationConstants.username);
							editit.remove(ApplicationConstants.password);
							editit.commit();
						}
					}
					Intent mainPageIntent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(mainPageIntent);
				}		
			}
		});
		
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
		Log.d(MyApplication.APP_NAME, "onSaveInstanceState");
		super.onSaveInstanceState(b);
	}

	public DataHelper getDataHelper() {
		return this.dataHelper;
	}

	public void setDataHelper(DataHelper dataHelper) {
		this.dataHelper = dataHelper;
	}

	@Override
	public void onRestoreInstanceState(final Bundle b) {
		super.onRestoreInstanceState(b);
	}

	private class LoginUserDataTask extends AsyncTask<String , Void, String> {
		protected void onPreExecute() {
		}
		protected String doInBackground(final String... credentials) {
			String email = credentials[0];
			String pword = credentials[1];
			try {
			    NetmeraUser.login(email,pword);
			} catch (NetmeraException e) {
			} 
			try {
				if(NetmeraUser.getCurrentUser()!=null){
					LoginActivity.this.registered=true;
				}else{
					LoginActivity.this.registered=false;
				}
			} catch (NetmeraException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(final String result) {
		}
	}
}