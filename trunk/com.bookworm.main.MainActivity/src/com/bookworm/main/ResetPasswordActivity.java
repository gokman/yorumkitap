package com.bookworm.main;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class ResetPasswordActivity extends ActivityBase {

	private EditText resetToken;
	private EditText newPassword;
	private EditText reNewPassword;
	private Button resetButton;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reset_password);
		
		resetToken=(EditText)findViewById(R.id.resetToken);
		newPassword=(EditText)findViewById(R.id.newPassword);
		reNewPassword=(EditText)findViewById(R.id.retypeNewPassword);
		resetButton=(Button)findViewById(R.id.resetPasswordButton);
		
	}

	
}