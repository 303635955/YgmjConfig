package com.yunguo.ygmjconfig.Util;

import java.util.HashMap;
import java.util.Map;

public class SplitString {
	
	private String rtsp;
	
	public SplitString(String rtsp){
		this.rtsp = rtsp;
	}
	
	public String getAdmin(){
		String[] s = rtsp.split("//");
		String[] a = s[1].split("@");
		String[] b = a[0].split(":");
		return b[0];
	}
	public String getPass(){
		String[] s = rtsp.split("//");
		String[] a = s[1].split("@");
		String[] b = a[0].split(":");
		return b[1];
	}
	public String getIp(){
		
		String[] s = rtsp.split("@");
		String[] a = s[1].split("/");
		String[] b = a[0].split(":");
		return b[0];
	}
	public String getDuankou(){
		String[] s = rtsp.split("@");
		String[] a = s[1].split("/");
		String[] b = a[0].split(":");
		return b[1];
	}
	
	public Map<String,String> getPaement(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("username", getAdmin());
		map.put("userpasw", getPass());
		map.put("Ipstr", getIp());
		map.put("port", getDuankou());
		
		return map;
	}
	
}
