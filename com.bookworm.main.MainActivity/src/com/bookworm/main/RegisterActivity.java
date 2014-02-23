package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.BOOKLET_ITEM_USER;
import static com.bookworm.common.ApplicationConstants.WS_ENDPOINT_ADRESS;
import static com.bookworm.common.ApplicationConstants.WS_OPERATION_REGISTER;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.model.User;
import com.bookworm.util.Validation;
import com.bookworm.ws.user.RegisterWS;

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
	private String result;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.register);
		
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
        
        usernameTextView.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isUserNameValid(usernameTextView);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        
        emailTextView.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isEmailValid(emailTextView);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        
        passwordTextView.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
                Validation.isPasswordValid(passwordTextView);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        
		registerButton = (Button) findViewById(R.id.btnRegister);
			registerButton.setOnClickListener(new View.OnClickListener() {
				public void onClick(View paramView) {
					
	                
					//TODO User already exists and form validation
					if(checkValidation()){
						try {
							
						User user = new User(usernameTextView.getText().toString(),
								             emailTextView.getText().toString(),
								             passwordTextView.getText().toString(),
								             new java.sql.Date(ApplicationConstants.dateFormat.getCalendar().getTime().getTime()),
								             new java.sql.Date(ApplicationConstants.dateFormat.getCalendar().getTime().getTime()),
								             0);		
		
						result=new RegisterWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_REGISTER,user).get();
							
						}catch (InterruptedException e) {
							e.printStackTrace();
						} catch (ExecutionException e) {
							e.printStackTrace();
						} 
		
						Toast.makeText(getApplicationContext(),getString(R.string.completionRegister), Toast.LENGTH_LONG)
								.show();
		
						Intent i = new Intent(getApplicationContext(),LoginActivity.class);
						startActivity(i);
						
					    }
				    }
			});
			
	}
	
	private boolean checkValidation(){
		if(!Validation.isUserNameValid(usernameTextView))
		return false;
		
		if(!Validation.isEmailValid(emailTextView))
		return false;
		
		if(!Validation.isPasswordValid(passwordTextView))
		return false;
		
		return true;
	}
	
}