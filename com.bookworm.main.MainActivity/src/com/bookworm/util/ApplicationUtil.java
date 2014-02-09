package com.bookworm.util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.bookworm.model.Hashtag;

public class ApplicationUtil {

    public static List<Long> getIdList(List <Hashtag> list,String inputField ){
    	List<Long> outputList = new ArrayList<Long>();
    	
    	if(list!=null && list.size()>=0){
    		for(int i = 0 ; i < list.size(); i++){
					outputList.add(list.get(i).getBookId());
    		}
    		
    	}
    	return outputList;
    }	
    
}
