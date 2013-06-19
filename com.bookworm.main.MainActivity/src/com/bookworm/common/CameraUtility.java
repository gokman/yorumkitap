package com.bookworm.common;

import java.util.ArrayList;
import java.util.List;

import android.hardware.Camera;
import android.hardware.Camera.Size;

public final class CameraUtility {

	private static android.hardware.Camera.Parameters camParameters;
    private static List<android.hardware.Camera.Size> pictureSizes;
    private static ArrayList<Integer> supportedMegaPixels;
	
    private static void refreshCameraData() {
    	Camera cam = Camera.open();
		camParameters = cam.getParameters();
		pictureSizes = camParameters.getSupportedPictureSizes();
		
		if (pictureSizes.size() > 0) {
			supportedMegaPixels = new ArrayList<Integer>();
			for (int i = 0; i < pictureSizes.size(); i++) {
				supportedMegaPixels.add(((pictureSizes.get(i).height * pictureSizes.get(i).width) + 1000000 / 2) / 1000000);
			}
		}
		
		cam.release();
    }
    
    
    
    public static List<Integer> getSupportedMegapixelRatings() {
    	if (camParameters == null) {
    		refreshCameraData();
    	}
    	
    	return supportedMegaPixels;
    }
    	
    public static boolean doesSupport2MP() {
    	return (supportedMegaPixels.contains(2));    	
    }
    
    public static boolean doesSupport3MP() {
    	return (supportedMegaPixels.contains(3));    	
    }
    
    public static boolean doesSupport5MP() {
    	return (supportedMegaPixels.contains(5));    	
    }
    
    public static boolean doesSupport8MP() {
    	return (supportedMegaPixels.contains(8));    	
    }
    
    public static boolean doesSupport12MP() {
    	return (supportedMegaPixels.contains(12));    	
    }
    
    public static boolean doesSupport15MP() {
    	return (supportedMegaPixels.contains(15));    	
    }
    	
    public static boolean doesSupportMP(int megapixels) {
    	return (supportedMegaPixels.contains(megapixels));
    }
       
    
}
