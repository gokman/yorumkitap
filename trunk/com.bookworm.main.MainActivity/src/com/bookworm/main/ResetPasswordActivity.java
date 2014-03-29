package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bookworm.custom.object.CustomResetPassword;
import com.bookworm.util.Validation;
import com.bookworm.ws.user.ResetPasswordWS;

public class ResetPasswordActivity extends ActivityBase {

	private EditText resetToken;
	private EditText newPassword;
	private EditText reNewPassword;
	private Button resetButton;
	private String email="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password);
		
		resetToken=(EditText)findViewById(R.id.resetToken);
		newPassword=(EditText)findViewById(R.id.newPassword);
		reNewPassword=(EditText)findViewById(R.id.retypeNewPassword);
		resetButton=(Button)findViewById(R.id.resetPasswordButton);
		
		 newPassword.addTextChangedListener(new TextWatcher() {
	            // after every change has been made to this editText, we would like to check validity
	            public void afterTextChanged(Editable s) {
	                Validation.isPasswordValid(newPassword);
	            }
	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
		 
		 reNewPassword.addTextChangedListener(new TextWatcher() {
	            // after every change has been made to this editText, we would like to check validity
	            public void afterTextChanged(Editable s) {
	                Validation.isPasswordValid(reNewPassword);
	            }
	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	        });
		
		email=getEmailAddress(getIntent().getExtras());
			
        resetButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				//we have controlled password fields now control 
				//resetToken field whether it is empty or not
				if(Validation.isEmpty(resetToken)){
					return;
				}
			
				if(checkValidation()){
					String password="",result="";
					long token;
					try {
						password=reNewPassword.getText().toString();
						token=Long.parseLong(resetToken.getText().toString());
						result=new ResetPasswordWS().
								    execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_RESET_PASSWORD,
								            new CustomResetPassword(email,password,token)).get();
						if(result.substring(4).equals("not_found")){
							resetToken.setError("wrong token");
							return;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (ExecutionException e) {
						e.printStackTrace();
					}
					Intent loginPageIntent = new Intent(getApplicationContext(),
							                            LoginActivity.class);
					startActivity(loginPageIntent);
			    }
			}
		});
	}
	
	public String getEmailAddress(Bundle bundle){
		
		return bundle.getString("userEmail");
	}
	
    private boolean checkValidation(){
		
		if(!Validation.isTwoFieldsSame(newPassword, reNewPassword)){
			return false;
		}
		if(!Validation.isPasswordValid(newPassword)){
			return false;
		}
		if(!Validation.isPasswordValid(reNewPassword)){
			return false;
		}
		
		return true;
	}

}