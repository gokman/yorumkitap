package com.bookworm.util;

import java.util.List;
import com.netmera.mobile.NetmeraContent;
import com.netmera.mobile.NetmeraException;

public class ApplicationUtil {

    public static String[] convertObjectListToInputList(List <NetmeraContent> netmeraContentList,String inputField ){
    	String [] outputList = new String[netmeraContentList.size()];
    	
    	if(netmeraContentList!=null && netmeraContentList.size()>=0){
    		for(int i = 0 ; i < netmeraContentList.size(); i++){
    			try {
					outputList[i] = (netmeraContentList.get(i).get(inputField).toString());
				} catch (NetmeraException e) {
					e.printStackTrace();
				}
    		
    		}
    		
    	}
    	return outputList;
    }	
}
