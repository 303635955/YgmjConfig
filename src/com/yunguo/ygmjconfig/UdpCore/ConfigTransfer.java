/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.yunguo.ygmjconfig.Util.SwitchData;

/**
 *
 * @author Administrator
 */
public class ConfigTransfer {
    private final int NET_Port = 51000;
    private final String NET_IP = "255.255.255.255";
    private final int CMD_LEN = 2048;
    private DatagramPacket recv;
    
    public Map<String,String> GetConfig(){
    	Map<String,String> map = new HashMap<String,String>();
        JSONObject recvJson = null;
        map.put("FLAG", "H3");
        map.put("Oper", "GET");
        JSONObject json = new JSONObject(map);
        byte[] ack = ExcuteCmd(json.toString().getBytes(), NET_Port, NET_IP);
        if(ack[0] != 0){
            String s = "";
            try {
                s = new String(ack, "UTF-8");
            } catch (UnsupportedEncodingException ex) {
            }
            try {
                recvJson = new JSONObject(s);
                System.out.println("获取到的数据有=========="+recvJson);
                map = parseJSON(recvJson);
            } catch (JSONException ex) {
            }
        }else{
        	return null;
        }
        return map;
    }
    
    
    public Map<String, String> parseJSON(JSONObject recvJson){
    	Map<String,String> mapjson = new HashMap<String,String>();
    	try {
    		
    		mapjson.put("RtspUrl", recvJson.getString("RtspUrl"));
			mapjson.put("HostAddress", recvJson.getString("HostAddress"));
			mapjson.put("HostGateway", recvJson.getString("HostGateway"));
	    	mapjson.put("HostNetmask", recvJson.getString("HostNetmask"));
	    	mapjson.put("OpenDhcp", recvJson.getString("OpenDhcp"));
	    	mapjson.put("PrvDuration", recvJson.getString("PrvDuration"));
	    	mapjson.put("RecDuration", recvJson.getString("RecDuration"));
	    	mapjson.put("ServerAddr", recvJson.getString("ServerAddr"));
	    	mapjson.put("ServerPort", recvJson.getString("ServerPort"));
	    	mapjson.put("Sn", recvJson.getString("Sn"));
	    	mapjson.put("startVideoPostIdleTime", recvJson.getString("StartTime"));
	    	mapjson.put("endVideoPostIdleTime", recvJson.getString("EndTime"));
    		mapjson.put("VideoIspost", SwitchData.Switchpostint2(recvJson.getString("VideoIsPost")));
    		mapjson.put("VideoSaveWay",SwitchData.Switchsaveint(recvJson.getString("VideoSaveWay")));
	    	
		} catch (JSONException e) {
			e.printStackTrace();
		}
    	
    	System.out.println("++++++++++++++++++++++++++++++++++"+mapjson);
		return mapjson;
    }
    public boolean SetConfig(Map<String,String> map){
        Map<String,Object> tMap = null;
        boolean retVal = false;
        if(map != null){
            map.put("FLAG", "H3");
            map.put("Oper", "SET");
            JSONObject json = new JSONObject(map);
            byte[] ack = null;
            try{
                if(recv != null){
                    ack = ExcuteCmd(json.toString().getBytes("UTF-8"), NET_Port, recv.getAddress().getHostAddress());
                }else{
                    ack = ExcuteCmd(json.toString().getBytes("UTF-8"), NET_Port, NET_IP);
                }
            } catch (UnsupportedEncodingException ex) {
            }
            
            if(ack[0] != 0){
                try {
                    JSONObject recvJson = new JSONObject(new String(ack, "UTF-8"));
                    if(recvJson.getString("IsOk").equals("true")){
                        retVal = true;
                    }
                } catch (JSONException ex) {
                } catch (UnsupportedEncodingException ex) {
                }
            }
        }
        return retVal;
    }
    
    private byte[] ExcuteCmd(byte[] cmd, int port, String ip){
        DatagramSocket socket = null;
        byte[] recvbuff = new byte[CMD_LEN];
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(2000);
            InetAddress addr = InetAddress.getByName(ip);
            DatagramPacket send = new DatagramPacket(cmd, cmd.length, addr, port);
            socket.send(send);
            
            recv = new DatagramPacket(recvbuff, recvbuff.length);
            socket.receive(recv);
        } catch (SocketException ex) {
        } catch (UnknownHostException ex) {
        } catch (IOException ex) {
        }
        return recvbuff;
    }
}
