package com.yunguo.ygmjconfig.Core;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
	private  Map<String,String> mCfgs;
	public AppConfig() {
		mCfgs =  new HashMap<String, String>();
		mCfgs.put("UrlAddhouse", "http://120.25.65.125:8118/Client1/AppAddhouse");
		mCfgs.put("UrlQueryhouse", "http://120.25.65.125:8118/Client1/QueryHousesapp");
		mCfgs.put("UrlQueryaddress", "http://120.25.65.125:8118/Client1/GetAreaListByid");
		mCfgs.put("UrlQueryLockinfo", "http://120.25.65.125:8118/Client1/QueryLockers");
		mCfgs.put("UrlQueryUserinfo", "http://120.25.65.125:8118/Client1/Applogin");
		mCfgs.put("AppDetailPawwsd", "http://120.25.65.125:8118/Client1/AppDetailPawwsd");
		mCfgs.put("AppAddLock", "http://120.25.65.125:8118/Client1/AppAddLock");
		mCfgs.put("UrlUpAppVersion", "http://120.25.65.125:8118/Client1/AppUpdate");
	}
	
	public String GetConfig(String key){
		if(mCfgs.containsKey(key)){
			return mCfgs.get(key);
		}
		return "";
	}
	public boolean SaveConfig(String key,String value){
		return true;
	}
	
	public String GetIp(){
		return "192.168.1.155";
	}
}