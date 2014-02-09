package com.bookworm.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplicationConstants {

	//general table columns
	public static final String GENERAL_COLUMN_CREATE_DATE="creationdate";
	public static final String GENERAL_COLUMN_UPDATE_DATE="updatedate";

	public static final String EMPTY_STRING ="";
	public static final String SPACE =" ";
	public static final String DIESIS_SIGN = "#";
	public static final String AT_SIGN = "@";
	public static final String userEmailParam = "useremail";
	public static final String bookIdParam = "bookId";
	public static final String object_path ="path";
	
	public static final int item_count_per_page = 12;
	public static final int item_count_per_page_for_comments = 100;
	public static final int item_count_per_page_for_explore_page = 15;
	public static final int item_count_per_page_for_main_page = 2;
	public static final int item_count_per_page_for_timeline_page = 1;
	
	//Tables
	public static final String user	= "User";
	public static final String book	= "Book";
	public static final String BOOKLET_ITEM_ACTION	= "Action";
	public static final String BOOKLET_ITEM_BOOK	= "Book";
	public static final String BOOKLET_ITEM_BOOKLIKE= "BookLike";
	public static final String BOOKLET_ITEM_COMMENT = "Comment";
	public static final String BOOKLET_ITEM_HASHTAG = "Hashtag";
	public static final String followship = "Followship";
	public static final String netmera_user = "NetmeraUser";
	public static final String hashtable ="HashTable";
	public static final String action="Action";
	public static final String bookLike = "BookLike";
	
	
	//Generic object properties
	public static final String generic_property ="getter_prop";
	//Fields By tables
	//Book
	public static final String book_name = "bookName";
	public static final String book_desc = "desc";
	public static final String book_adderId = "adderId";
	public static final String book_writer = "writer";	
	public static final String book_coverPhoto = "coverPhoto";
	public static final String book_tags = "tags";
	public static final String book_create_date = "Create Date";
	public static final String book_owner="Owner";
	public static final String book_id="bookId";
	
	//Comment
	public static final String comment_er = "commenterID";
	public static final String comment_text = "commentText";	
	public static final String comment_edBook = "commentedBookName";
	public static final String comment_edBookOwner ="bookAdderId";
	public static final String comment_create_date ="Create Date";
	
	//BookLike
	public static final String bookLike_id	= "bookLikeID";
	public static final String bookLike_r_id = "bookLikerID";
	public static final String bookLike_bookID = "bookID";
	public static final String bookLike_date= "bookLikeDate";
	
	//Followship
	public static final String followship_user_id ="userId";
	public static final String followship_follows ="follows";
	
	
	
	//NetmeraUser
	public static final String netmera_user_email="email";
	public static final String netmera_user_username="username";
	
	//User
	public static final String user_userProfile ="profilePhoto";
	public static final String user_email ="email";
	public static final String user_username ="username";
	public static final String user_about ="about";
	
	//Hashtag  table
	public static final String hashtable_tag ="tag";
	public static final String hashtable_book_title ="book_title";
	public static final String hashtable_book_adder_id="book_adder_id";
	public static final String hashtable_book_path = "book_path";
	
	//Form Fields Tip Texts
	public static final String comment_write_comment = "Yorum ekle...";
	
	
	//Stored Credential parameters
	public static String sharedPrefName = "let_the_books_begin";
	public static final String username = "bl_user_name";
	public static final String password = "bl_password";
	public static final String unregistered_username = "unregistered_username";
	public static final String unregistered_password = "unregistered_password";
	
	//Timeline Listview Types
	public static final String TYPE="TYPE";
	public static final String TYPE_BOOK="BOOK";
	public static final String TYPE_COMMENT="COMMENT";
	public static final String TYPE_FOLLOW="FOLLOW";
	
	//Timeline Listview comment Types
	public static final String TYPE_COMMENDATOR="COMMENDATOR";
	public static final String TYPE_COMMENTEDBOOKOWNER="BOOKOWNER";
	public static final String TYPE_COMMENTEDBOOKNAME="BOOKNAME";
	public static final String TYPE_COMMENTDATE="COMMENTDATE";
	
	//Timeline Listview follow Types
	public static final String TYPE_FOLLOWER="FOLLOWER";
	public static final String TYPE_FOLLOWED="FOLLOWED";
	public static final String TYPE_FOLLOWSHIP="FOLLOWSHIP";
	public static final String TYPE_FOLLOWDATE="FOLLOWDATE";
	
	//Timeline Listview book Types
	public static final String TYPE_COVER_URL="COVER URL";
	public static final String TYPE_BOOK_NAME="BOOK_NAME";
	public static final String TYPE_BOOK_DESC="BOOK_DESC";
	public static final String TYPE_BOOK_OWNER="BOOK_OWNER";
	public static final String TYPE_BOOK_CREATE_DATE="BOOK_CREATE_DATE";
	
	//Action tablo elemanlari
	public static final String ACTION_TYPE="actionType";
	public static final String ACTION_OWNER="actionOwner";
	//book tablo hareketleri icin
	public static final String action_book_name = "bookName";
	public static final String action_book_desc = "desc";
	public static final String action_book_adderId = "adderId";
	public static final String action_book_writer = "writer";	
	public static final String action_book_coverPhoto = "coverPhoto";
	public static final String action_book_tags = "tags";
	//comment tablo hareketleri için
	public static final String action_comment_er = "commenterID";
	public static final String action_comment_text = "commentText";	
	public static final String action_comment_edBook = "commentedBookName";
	public static final String action_comment_edBookOwner ="bookAdderId";
	//follow tablo hareketleri için
	public static final String action_follower_id="followerID";
	public static final String action_followed_id="followedID";
	
	//action tipleri
	public static final int ACTION_TYPE_BOOK=1;
	public static final int ACTION_TYPE_COMMENT=2;
	public static final int ACTION_TYPE_FOLLOW=3;
	public static final int  ACTION_TYPE_LIKE=4;
	
	
	
	public static final String CREATE_DATE="CREATE_DATE";
	
	//zaman t�nelinde listelenen elemanlarda bulunan tarih b�l�m�n�n format�n� tutar
	public final static SimpleDateFormat dateFormat=new SimpleDateFormat("hh:mm dd-MM-yyyy");
	
	//zaman tunelindeki sonraki dugmesinin degerini tutar
	public static int timelineListStatus=1;
	public static int mainBookListStatus=1;
	//zaman tunelinde gosterilen elemanların en geçmiş tarihli olan değeri tutar
	public static Date timelineLastDate=dateFormat.getCalendar().getTime();

	public static final String WS_ENDPOINT_ADRESS = "http://192.168.2.148:8080/booklet-ws/services";
	public static final String WS_OPERATION_GET_BY_ID		="GET_BY_ID";
	public static final String WS_OPERATION_ADD				="ADD";
	public static final String WS_OPERATION_LIST			="LIST";
	public static final String WS_OPERATION_LIST_LIKES		="LIST_LIKES";
	public static final String WS_OPERATION_LIST_COMMENTS	="LIST_COMMENTS";
	public static final String WS_OPERATION_LIST_BY_TEXT	="LIST_BY_TEXT";
	
	public static final String WS_OPERATION_SAVE			="SAVE";
	public static final String WS_OPERATION_UPDATE			="UPDATE";
	public static final String WS_OPERATION_DELETE			="DELETE";
	
}
