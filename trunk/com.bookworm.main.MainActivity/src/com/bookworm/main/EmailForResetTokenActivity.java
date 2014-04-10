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
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.model.User;
import com.bookworm.util.Validation;
import com.bookworm.ws.user.IsUserExistWS;
import com.bookworm.ws.user.SendResetPassTokenWS;

public class EmailForResetTokenActivity extends ActivityBase {


	private EditText emaill;
	private Button sendButton;
	private TextView explain;
	public String result="";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.email_pass_reset_token);
		
		emaill=(EditText)findViewById(R.id.EmailForResetPass);
		sendButton=(Button)findViewById(R.id.SendEmailforResetPass);
		explain=(TextView)findViewById(R.id.explainForEmailResetToken);
		
		emaill.addTextChangedListener(new TextWatcher() {
	            // after every change has been made to this editText, we would like to check validity
	            public void afterTextChanged(Editable s) {
	                Validation.isEmailValid(emaill);
	            }
	            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	            public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				User user=new User();
				user.setUserEmail(emaill.getText().toString());
				boolean isUserExist=false;
				try {
					isUserExist = isUserExist(user);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (ExecutionException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(!isUserExist){
					Toast.makeText(getApplicationContext(), "Wrong Email address", Toast.LENGTH_LONG).show();
					return;
				}
				//check validation 
				if(checkValidation()){
				    //send mail
					String email="";
					try {
						email=emaill.getText().toString();
						result=new SendResetPassTokenWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_SEND_RESET_TOKEN,
								email).get();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Intent resetPasswordPageIntent = new Intent(getApplicationContext(),
							ResetPasswordActivity.class);
					resetPasswordPageIntent.putExtra("userEmail", email);
					startActivity(resetPasswordPageIntent);
			    }
			}
		});
		
	}
	
	private boolean checkValidation(){
		
		if(!Validation.isEmailValid(emaill)){
		    return false;
		}
		    return true;
	}
	
	public boolean isUserExist(User user) throws InterruptedException, ExecutionException{
		boolean result=new IsUserExistWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_IS_USER_EXIST,
				user).get();
		
		return result;
	}

}