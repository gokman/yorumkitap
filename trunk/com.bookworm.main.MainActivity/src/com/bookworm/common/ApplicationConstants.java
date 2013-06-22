package com.bookworm.common;

public class ApplicationConstants {

	public static final String EMPTY_STRING ="";
	public static final String DIESIS_SIGN = "#";
	public static final String userEmailParam = "useremail";
	public static final String object_path ="Path";
	
	public static final int item_count_per_page = 12;
	public static final int item_count_per_page_for_comments = 100;
	public static final int item_count_per_page_for_explore_page = 15;
	public static final int item_count_per_page_for_main_page = 1;
	
	//Tables
	public static final String user	= "User";
	public static final String book	= "Book";
	public static final String comment = "Comment";
	public static final String followship = "Followship";
	public static final String netmera_user = "NetmeraUser";
	public static final String hashtable ="HashTable";
	
	
	
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
	
	//Comment
	public static final String comment_er = "commenterID";
	public static final String comment_text = "commentText";	
	public static final String comment_edBook = "commentedBookName";
	public static final String comment_edBookOwner ="bookAdderId";
	public static final String comment_create_date ="Create Date";
	
	
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
	public static final String username = "bl_user_name";
	public static final String password = "bl_password";
	public static final String unregistered_username = "unregistered_username";
	public static final String unregistered_password = "unregistered_password";
	
}
