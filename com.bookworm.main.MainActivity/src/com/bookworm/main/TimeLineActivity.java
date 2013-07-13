package com.bookworm.main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.bookworm.common.ApplicationConstants;
import com.bookworm.common.CustomComparator;
import com.bookworm.common.GetNetmerMediaTask;
import com.bookworm.common.LazyAdapter;
import com.bookworm.common.SelectDataTask;
import com.bookworm.common.TimeLineAdapter;
import com.netmera.mobile.NetmeraClient;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;
import com.netmera.mobile.NetmeraService;
import com.netmera.mobile.NetmeraUser;

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
	//tablodaki kitapları satır satır olduğu gibi tutar
	List<NetmeraContent> commentList;
	List<NetmeraContent> commentBookList;
	NetmeraContent commentBook;
	//viewda gösterilecek olan satırları tutar  --book
	private ArrayList<HashMap<String, String>>  bookListToView;
	private ListView bookListView;
	//viewda gösterilecek olan satırları tutar --comment
	private ArrayList<HashMap<String, String>>  commentBookListToView;
	private HashMap<String,String> commentBookTempMap;
	private List<String> commentDateList;
	private String commendatorName;
	//follow ile alakal�
	List<NetmeraContent> followList;
	private ListView followListView;
	private ArrayList<HashMap<String,String>> followListToView;

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
	        
	        //kullanıcıyı çek
	        try {
				commendatorName=NetmeraUser.getCurrentUser().getEmail();
			} catch (NetmeraException e1) {
				e1.printStackTrace();
			}
	        
	        //kitap için bize yardımcı olacak servisimiz
	        NetmeraService servicer = new NetmeraService(ApplicationConstants.book);
			servicer.setMax(ApplicationConstants.item_count_per_page_for_main_page);
			
			//yorum için bize yardımcı olacak servisimiz
			NetmeraService servicerComment = new NetmeraService(ApplicationConstants.comment);
			servicerComment.setMax(ApplicationConstants.item_count_per_page_for_timeline_page);
			
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
					map.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_BOOK);
					map.put(ApplicationConstants.TYPE_COVER_URL, tempBook1CoverURL);
					map.put(ApplicationConstants.TYPE_BOOK_NAME, tempBook1.get(ApplicationConstants.book_name).toString());
					map.put(ApplicationConstants.TYPE_BOOK_DESC, tempBook1.get(ApplicationConstants.book_desc).toString());
					map.put(ApplicationConstants.TYPE_BOOK_ADDERID, tempBook1.get(ApplicationConstants.book_adderId).toString());
					map.put(ApplicationConstants.CREATE_DATE, tempBook1.getCreateDate().toString());
					bookListToView.add(map);
				}
				
					//comment ile alakalı verileri çekme işlemi 
					//başla
					commentDateList=new ArrayList<String>();
					//bu kullanıcıya ait tüm commentleri çekeceğiz
					//servise sadece bu kullanıcının yorum yaptığı satırları çekiyoruz
					servicerComment.whereEqual(ApplicationConstants.comment_er, NetmeraUser.getCurrentUser().getEmail());
					//elimizde bu kullanıcının yorum yaptığı liste var
					commentList=new SelectDataTask().execute(servicerComment).get();
					commentBookList=new ArrayList<NetmeraContent>();
					for(int j=0;j<commentList.size();j++){
						commentDateList.add(commentList.get(j).getCreateDate().toString());
						//book servisimize commentten gelen değerler doğrultusunda filtre koyup yorum yapılan kitapları tek tek çekiyoruz
						NetmeraService serviceBook = new NetmeraService(ApplicationConstants.book);
						serviceBook.setMax(ApplicationConstants.item_count_per_page_for_timeline_page);
						serviceBook.whereEqual(ApplicationConstants.book_adderId, commentList.get(j).get(ApplicationConstants.comment_edBookOwner).toString());
				        serviceBook.whereEqual(ApplicationConstants.book_name, commentList.get(j).get(ApplicationConstants.comment_edBook).toString());
				        //selectten gelen değer 1 tane olmalı. biz de bu gelen değerlerden 1. yi al dedik
				        commentBook=new SelectDataTask().execute(serviceBook).get().get(0);
					    commentBookList.add(commentBook);
					}
					//elimizde yorum yapılan kitap listesi commentBookList te tutuluyor
					commentBookListToView=new ArrayList<HashMap<String,String>>();
					for(int z=0;z<commentBookList.size();z++){
						//listview da gösterilen satıra ait elemanları tek tek tutacak
						commentBookTempMap=new HashMap<String, String>();
						commentBookTempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENDATOR, commendatorName);
						commentBookTempMap.put(ApplicationConstants.CREATE_DATE, commentDateList.get(z));
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, 
								commentBookList.get(z).get(ApplicationConstants.book_name).toString());
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER, 
								commentBookList.get(z).get(ApplicationConstants.book_adderId).toString());
						commentBookListToView.add(commentBookTempMap);
					}
					//commentlisttoview listemizi yani commentlerin listesini tutan view nesnelerini atama işlemi yapıyoruz
					bookListToView.addAll(commentBookListToView);
					Collections.sort(bookListToView,new CustomComparator());
					
					//bitir
					/*
					//follow ile alakalı verileri cekme islemi 
					//basla
					
					//bu kullaniciya ait tum followlar� cekecegiz
					NetmeraService serviceFollow = new NetmeraService(ApplicationConstants.followship);
					serviceFollow.setMax(ApplicationConstants.item_count_per_page_for_timeline_page);
					serviceFollow.whereEqual(ApplicationConstants.book_adderId, commentList.get(j).get(ApplicationConstants.comment_edBookOwner).toString());
			        serviceFollow.whereEqual(ApplicationConstants.book_name, commentList.get(j).get(ApplicationConstants.comment_edBook).toString());
					//elimizde bu kullanıcının takip listesi var
					followList=new SelectDataTask().execute(serviceFollow).get();
					for(int j=0;j<followList.size();j++){
						//listview da gösterilen satıra ait elemanları tek tek tutacak
						commentBookTempMap=new HashMap<String, String>();
						commentBookTempMap.put(ApplicationConstants.TYPE, ApplicationConstants.TYPE_COMMENT);
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENDATOR, commendatorName);
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENTDATE, commentDate);
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKNAME, 
								commentBookList.get(j).get(ApplicationConstants.book_name).toString());
						commentBookTempMap.put(ApplicationConstants.TYPE_COMMENTEDBOOKOWNER, 
								commentBookList.get(j).get(ApplicationConstants.book_adderId).toString());
						commentBookListToView.add(commentBookTempMap);
					}
					
					
					
					//bitir
				*/
				
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
