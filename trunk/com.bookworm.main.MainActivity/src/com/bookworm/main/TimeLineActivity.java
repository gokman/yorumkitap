package com.bookworm.main;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.bookworm.common.TimeLineAdapter;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;

import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

public class TimeLineActivity extends ActivityBase{
	//listelenen elemanların tipini tutacak
	public static final String ListElementType="TYPE";
	private TimeLineAdapter adapter;
	//tablodaki kitapları satır satır olduğu gibi tutar
	List<NetmeraContent> bookList;
	//viewda gösterilecek olan satırları tutar
	private ArrayList<HashMap<String, String>>  bookListToView;
	private ListView bookListView;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		    super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	        setContentView(R.layout.timeline_page);
	        
	        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
					R.layout.window_title);

	        setExplore_button((ImageView)findViewById(R.id.explore_button));
			setHome_button((ImageView)findViewById(R.id.home_button));
			setAdd_book_button((ImageView)findViewById(R.id.add_button));
			setProfile_button((ImageView)findViewById(R.id.profile_button));
			setTimeline_button((ImageView)findViewById(R.id.timeline_button));
	        
	        NetmeraClient.init(this, apiKey);
	        setNavigationButtons();
	        
	        NetmeraService servicer = new NetmeraService(ApplicationConstants.book);
			servicer.setMax(ApplicationConstants.item_count_per_page_for_main_page);
			
			//servis ile kitapları tek tek çek
			try {
				//tüm kitap tablosu elimde
				bookList=new SelectDataTask().execute(servicer).get();
				//viewa dolacak liste hazırda bekliyor
				bookListToView=new ArrayList<HashMap<String, String>>();
				for(int i=0;i<bookList.size();i++){
					//tek tek tempBook1 e ata
					NetmeraContent tempBook1=bookList.get(i);
					//düzgünce adapter a uygun yerleştireceğimiz map imiz var
					HashMap<String,String> map=new HashMap<String,String>();
					//BUNU BİLMİYORUM ????
					tempBook1.add(ApplicationConstants.generic_property, ApplicationConstants.book_coverPhoto);
					String tempBook1CoverURL = new GetNetmerMediaTask().execute(tempBook1).get();
					//artık elimde bir satıra ait herşey var. bunu şimdi map e atacağız.
					map.put("TYPE", "BOOK");
					map.put("COVER_URL", tempBook1CoverURL);
					map.put("BOOK_NAME", tempBook1.get(ApplicationConstants.book_name).toString());
					map.put("BOOK_DESC", tempBook1.get(ApplicationConstants.book_desc).toString());
					map.put("BOOK_ADDERID", tempBook1.get(ApplicationConstants.book_adderId).toString());
					bookListToView.add(map);
				}
				
			}catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NetmeraException e){
				e.printStackTrace();
			}
			//elimizdeki listviewı ekliyoruz
			bookListView=(ListView)findViewById(R.id.timeline_elements);
	        adapter=new TimeLineAdapter(this, bookListToView);
	        bookListView.setAdapter(adapter);
	}
}
