package com.bookworm.main;

import com.netmera.mobile.NetmeraClient;
import android.os.Bundle;
import android.view.Window;

public class TimeLineActivity extends ActivityBase{

	@Override
    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.timeline_page);
	        
	        NetmeraClient.init(this, apiKey);
	}
}
