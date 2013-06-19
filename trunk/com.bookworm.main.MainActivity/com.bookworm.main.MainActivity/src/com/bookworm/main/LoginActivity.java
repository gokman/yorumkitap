package com.bookworm.main;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

	private DataHelper dataHelper;

	private static final String NAME = "NAME";

	private EditText _email;
	private EditText _password;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		this.dataHelper = new DataHelper(this);

		
		setContentView(R.layout.login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);

		_email = (EditText)findViewById(R.id.emailText);
		_password = (EditText)findViewById(R.id.passwordText);
		
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
					Intent mainPageIntent = new Intent(getApplicationContext(),
							MainActivity.class);
					startActivity(mainPageIntent);
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