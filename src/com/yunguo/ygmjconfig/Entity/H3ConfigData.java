package com.yunguo.ygmjconfig.Entity;

import java.util.List;

public class H3ConfigData {
	
	public int SerialNo;     //序列号
	public boolean Dhcp;     //是否启用DHCP
	public String IP;        
	public String Mac;
	public String Mask;
	public String Getway;
	public String DoorCtrlSN;   //门禁板序列号 【可选】
	public int DoorCtrlType;    //门禁板类型 【可选】, 如果配置了门禁板序列号，则默认选择DCTRL_TYPE_YG_NET
	public int VideoCapDuration;      //视频录像时长
	public String VideoServer;        //视频平台地址  http://xxxx:port 形式 如http://120.25.65.125:8118 http://ygwit.com
	public List<String> VideoUrl;          //视频URL, 最大可选四路视频
	public String VideoStorePath;      //视频存储路径
	public String InDoorRtspUrl;  //门内视频地址, 作为出门抓拍视频地址, 【可选】，如果配置了门禁板序列号，则可配置
	public String OutDoorRtspUrl;
	
    public int getSerialNo() {
		return SerialNo;
	}
	public void setSerialNo(int serialNo) {
		SerialNo = serialNo;
	}
	public boolean isDhcp() {
		return Dhcp;
	}
	public void setDhcp(boolean dhcp) {
		Dhcp = dhcp;
	}
	public String getIP() {
		return IP;
	}
	public void setIP(String iP) {
		IP = iP;
	}
	public String getMac() {
		return Mac;
	}
	public void setMac(String mac) {
		Mac = mac;
	}
	public String getMask() {
		return Mask;
	}
	public void setMask(String mask) {
		Mask = mask;
	}
	public String getGetway() {
		return Getway;
	}
	public void setGetway(String getway) {
		Getway = getway;
	}
	public String getDoorCtrlSN() {
		return DoorCtrlSN;
	}
	public void setDoorCtrlSN(String doorCtrlSN) {
		DoorCtrlSN = doorCtrlSN;
	}
	public int getDoorCtrlType() {
		return DoorCtrlType;
	}
	public void setDoorCtrlType(int doorCtrlType) {
		DoorCtrlType = doorCtrlType;
	}
	public int getVideoCapDuration() {
		return VideoCapDuration;
	}
	public void setVideoCapDuration(int videoCapDuration) {
		VideoCapDuration = videoCapDuration;
	}
	public String getVideoServer() {
		return VideoServer;
	}
	public void setVideoServer(String videoServer) {
		VideoServer = videoServer;
	}
	public List<String> getVideoUrl() {
		return VideoUrl;
	}
	public void setVideoUrl(List<String> videoUrl) {
		VideoUrl = videoUrl;
	}
	public String getVideoStorePath() {
		return VideoStorePath;
	}
	public void setVideoStorePath(String videoStorePath) {
		VideoStorePath = videoStorePath;
	}
	public String getInDoorRtspUrl() {
		return InDoorRtspUrl;
	}
	public void setInDoorRtspUrl(String inDoorRtspUrl) {
		InDoorRtspUrl = inDoorRtspUrl;
	}
	public String getOutDoorRtspUrl() {
		return OutDoorRtspUrl;
	}
	public void setOutDoorRtspUrl(String outDoorRtspUrl) {
		OutDoorRtspUrl = outDoorRtspUrl;
	}
}
