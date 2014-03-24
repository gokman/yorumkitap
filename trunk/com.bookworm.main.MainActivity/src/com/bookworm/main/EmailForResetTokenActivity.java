package com.bookworm.main;

import static com.bookworm.common.ApplicationConstants.*;
import java.util.concurrent.ExecutionException;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
		
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
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
		});
		
	}

}