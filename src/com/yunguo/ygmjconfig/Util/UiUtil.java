package com.yunguo.ygmjconfig.Util;


import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class UiUtil {
	
	
	static public void MessageBox(String str,Context c){
		AlertDialog.Builder builder  = new AlertDialog.Builder(c);
		 builder.setMessage(str) ;
		 builder.setPositiveButton("确认" ,  null );
		 builder.show(); 
	}
	
	/** 
	    * 获取版本名称 
	    * @param context 
	    * @return 
	    */  
	    public static String getVerName(Context context) {  
	        String verName = "";  
	        try {  
	            PackageManager pm = context.getPackageManager();  
	            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	            verName = pi.versionName;    
	        } catch (NameNotFoundException e) {  
	            Log.e("msg",e.getMessage());  
	        }  
	        return verName;     
	}
}
