package com.yunguo.ygmjconfig.Util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MyIp {

    /**
     * 获取手机设备的IP
     * @return IP
     */
    public static String getIP(Context context) {
        //获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        return (ipAddress & 0xFF) + "." +
                ((ipAddress >> 8 ) & 0xFF) + "." +
                ((ipAddress >> 16 ) & 0xFF) + "." +
                ( ipAddress >> 24 & 0xFF) ;
    }
}
