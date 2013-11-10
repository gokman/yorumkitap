package com.bookworm.main;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.InsertDataTask;
import com.bookworm.common.InsertUserTask;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraUser;

public class RegisterActivity extends ActivityBase {
    /*
     * GUI Components
     */
	private EditText usernameTextView ;
	private EditText emailTextView ;
	private EditText passwordTextView ;
	private TextView loginScreen; 
	private Button registerButton;
	private boolean isRegSuccessfull;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
		NetmeraClient.init(this, apiKey);
		
		//Initialize variables
        loginScreen = (TextView) findViewById(R.id.link_to_login);
        usernameTextView = (EditText) findViewById(R.id.reg_fullname);
        emailTextView = (EditText) findViewById(R.id.reg_email);
        passwordTextView = (EditText) findViewById(R.id.reg_password);
        isRegSuccessfull = false;
        
        // Listening to Login Screen link
        loginScreen.setOnClickListener(new View.OnClickListener() {
			public void onClick(View arg0) {
				Intent i = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(i);
			}
		});
        
		registerButton = (Button) findViewById(R.id.btnRegister);
		registerButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View paramView) {

				//TODO User already exists and form validation
				try {
					
				NetmeraUser user = new NetmeraUser();
		    	  user.setEmail(emailTextView.getText().toString());
		    	  user.setPassword(passwordTextView.getText().toString());
		    	  user.setNickname(usernameTextView.getText().toString());

		    	NetmeraContent userContent = new NetmeraContent(ApplicationConstants.user);
		    	userContent.add(ApplicationConstants.user_email, emailTextView.getText().toString());
		    	userContent.add(ApplicationConstants.user_username, usernameTextView.getText().toString());
		    	
				
					new InsertUserTask().execute(user).get();
					new InsertDataTask().execute(userContent).get();
					
				}catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				} catch (NetmeraException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				Toast.makeText(getApplicationContext(),getString(R.string.completionRegister), Toast.LENGTH_LONG)
						.show();

				Intent i = new Intent(getApplicationContext(),LoginActivity.class);
				startActivity(i);
				
			}
		});
	}
	
}