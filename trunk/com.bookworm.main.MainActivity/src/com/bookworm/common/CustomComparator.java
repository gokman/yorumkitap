package com.bookworm.common;

import java.util.Comparator;
import java.util.HashMap;

public class CustomComparator implements Comparator<HashMap<String,String>> {
    public int compare(HashMap<String,String> o1, HashMap<String,String> o2) {
        return o1.get(ApplicationConstants.CREATE_DATE).compareTo(o2.get(ApplicationConstants.CREATE_DATE));
    }
}
