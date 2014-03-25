package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;

import java.util.concurrent.ExecutionException;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bookworm.custom.object.CustomResetPassword;
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
		
		email=getEmailAddress(getIntent().getExtras());
			
        resetButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
			
				String password="",result="";
				long token;
				try {
					password=reNewPassword.getText().toString();
					token=Long.parseLong(resetToken.getText().toString());
					result=new ResetPasswordWS().execute(WS_ENDPOINT_ADRESS+"/"+BOOKLET_ITEM_USER+"/"+WS_OPERATION_RESET_PASSWORD,
							new CustomResetPassword(email,password,token)).get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Intent loginPageIntent = new Intent(getApplicationContext(),
						LoginActivity.class);
				startActivity(loginPageIntent);
			}
		});
	}
	
	public String getEmailAddress(Bundle bundle){
		
		return bundle.getString("userEmail");
	}

}