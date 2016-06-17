package com.yunguo.ygmjconfig.LocationApplication;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;

import android.app.Application;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class LocationApplication extends Application{
	
	public LocationClient mLocationClient;//定位SDK的核心类
	public MyLocationListener mMyLocationListener;//定义监听类
	public TextView mLocationAddress;
	public EditText mLocationGnote;

	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
	}
	
	
	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			StringBuffer address = new StringBuffer(256);
			StringBuffer  Gnote = new StringBuffer(256);
			Gnote.append(location.getLatitude());//获得纬度
			Gnote.append(",");
			Gnote.append(location.getLongitude());//获得经度
			
			
			if (location.getLocType() == BDLocation.TypeGpsLocation){//通过GPS定位
				//address.append(location.getProvince());//获得当前地址
				//address.append(location.getCity());//获得当前地址
				//address.append(location.getDistrict());//获得当前地址
				address.append(location.getStreet());//获得当前地址
			} else if (location.getLocType() == BDLocation.TypeNetWorkLocation){//通过网络连接定位
				//address.append(location.getProvince());//获得当前地址
				//address.append(location.getCity());//获得当前地址
				//address.append(location.getDistrict());//获得当前地址
				address.append(location.getStreet());//获得当前地址
				
			} else {
				address.append("获取失败，请检查网络设置");
			}
			logMsgAddress(address.toString());
			logMsgGnote(Gnote.toString());
			Log.i("BaiduLocationApiDem", address.toString()+"  --- "+Gnote.toString());
		}
	}
	
	/**
	 * 显示请求字符串
	 * @param str
	 */
	public void logMsgAddress(String str) {
		
		if(str.equals("")){
			str = "获取失败，请检查网络设置";
		}
		try {
			if (mLocationAddress != null)
				mLocationAddress.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logMsgGnote(String str) {
		
		if(str.equals(",")){
			str = "获取失败，请检查网络";
		}
		try {
			if (mLocationGnote != null)
				mLocationGnote.setText(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}

}
