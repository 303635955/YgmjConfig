package com.yunguo.ygmjconfig.VideoController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigData {
    public static final int DCTRL_TYPE_YG_LOCKER = 1;  //云果一体化锁
    public static final int DCTRL_TYPE_YG_NET = 2;     //云果网络门禁控制器
    public static final int DCTRL_TYPE_WG_NET = 3;     //微耕网络门禁控制器
    public int SerialNo;     //序列号
    public boolean Dhcp;     //是否启用DHCP
    public String IP;        
    public String Mac;
    public String Mask;
    public String Getway;
    public String DoorCtrlSN;   //门禁板序列号 【可选】
    public int DoorCtrlType;    //门禁板类型 【可选】, 如果配置了门禁板序列号，则默认选择DCTRL_TYPE_YG_NET
    public int VideoCapDuration;      //视频录像时长
    public String CtrlServer;    //平台服务器地址 http://xxxx:port 形式 如http://120.25.65.125:8118 http://ygwit.com
    public String DeviceCtrlServer;        //视频平台地址  http://xxxx:port 形式 如http://120.25.65.125:8118 http://ygwit.com
    public String SwipeCapServer;     //刷卡抓拍服务地址
    public List<String> VideoUrl;          //视频URL, 最大可选四路视频
    public String VideoStorePath;      //视频存储路径
    public String InDoorRtspUrl;  //门内视频地址, 作为出门抓拍视频地址, 【可选】，如果配置了门禁板序列号，则可配置
    public String OutDoorRtspUrl;   //门外视频地址，作为进门抓拍视频地址 【可选】，如果配置了门禁板序列号，则必须配置
    
    public ConfigData()
    {
        VideoCapDuration = 2;
        VideoStorePath = "TF";
        DoorCtrlType = DCTRL_TYPE_YG_NET;
        DoorCtrlSN = "";
        VideoUrl = new ArrayList();
    }
    public String toJson()
    {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, ConfigData.class);
    }
    
    public static  ConfigData fromJson(String json)
    {
        ConfigData p = null;
        Gson gson = new GsonBuilder().create();
        try{
            p = gson.fromJson(json, ConfigData.class);
            //如果只配置了一个视频地址，则默认作为进门抓拍
            if(p.VideoUrl != null && p.VideoUrl.size() == 1 && p.OutDoorRtspUrl == null) {
                p.OutDoorRtspUrl = p.VideoUrl.get(0);
            }
        }catch(Exception ex){
            Logger.getLogger(CfgProtocol.class.getName()).log(Level.WARNING, "convert json to object error");
        }
        return p;
    }
    
}
