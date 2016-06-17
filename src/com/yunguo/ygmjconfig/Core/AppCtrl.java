package com.yunguo.ygmjconfig.Core;


public class AppCtrl {
	private AppConfig mAppConfig = null;
	
	
	public AppConfig GetAppConfig(){
		if(mAppConfig == null){
			mAppConfig = new AppConfig();
		}
		return mAppConfig;
	}
	
	
	static private AppCtrl mObj = null;

	static public AppCtrl GetObj() {
		if (mObj == null) {
			mObj = new AppCtrl();
		}
		return mObj;
	}
	
	private AppCtrl(){
		
	}

}
