package com.bookworm.common;

public enum LoginPlatform {
	
	BookLet{
		public int getPlatformNumber(){
			return 0;
		}
	},
     FaceBook{
    	 public int getPlatformNumber(){
    		 return 1;
    	 }
     };
	
	public abstract int getPlatformNumber();
}
