package com.bookworm.util;

import java.util.regex.Pattern;

import android.widget.EditText;

public class Validation {
	
	private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	public static boolean isUserNameValid(final EditText text){
		final String str=text.getText().toString();
		if(str.length()>40){
			text.setError("40 karakterden fazla olamaz");
			return false;
		}
		if(str.length()<5){
			text.setError("5 karakterden az olamaz");
			return false;
		}
		if(str.equals("")){
			text.setError("zorunlu alan");
			return false;
		}
		return true;
	}

	public static boolean isEmailValid(final EditText text){
		final String str=text.getText().toString();
		if(!Pattern.matches(EMAIL_REGEX, str)){
			text.setError("yanlış email adresi");
			return false;
		}
		if(str.equals("")){
			text.setError("zorunlu alan");
			return false;
		}
		return true;
	}
	public static boolean isPasswordValid(final EditText text){
		final String str=text.getText().toString();
		if(str.length()<8){
			text.setError("en az 8 karakter olmalı");
			return false;
		}
		if(str.equals("")){
			text.setError("zorunlu alan");
			return false;
		}
		return true;
	}
	public static boolean isTwoFieldsSame(final EditText text,final EditText text2){
		final String str=text.getText().toString();
		final String str2=text2.getText().toString();
		if(str.equals(str2)){
			return true;
		}else{
			text2.setError("Şifre aynı olmalı");
			return false;
		}
	}
	public static boolean isEmpty(final EditText text){
		final int textLength=text.getText().toString().trim().length();
		if(textLength==0){
			text.setError("Zorunlu alan");
			return true;
		}else{
			return false;
		}
	}
}