package com.bookworm.common;

import java.text.ParseException;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;

public class CustomComparator implements Comparator<HashMap<String,String>> {
	Date date1,date2;
    public int compare(HashMap<String,String> o1, HashMap<String,String> o2) {
    	try {
    	date1=ApplicationConstants.dateFormat.parse(o1.get(ApplicationConstants.CREATE_DATE));
    	date2=ApplicationConstants.dateFormat.parse(o2.get(ApplicationConstants.CREATE_DATE));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return date1.compareTo(date2);
    }
}
