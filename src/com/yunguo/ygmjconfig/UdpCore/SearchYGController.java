/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yunguo.ygmjconfig.Controller.YGController;
import com.yunguo.ygmjconfig.Entity.Controller;
import com.yunguo.ygmjconfig.Util.SwitchData;

import android.annotation.SuppressLint;
import android.util.Log;

/**
 *
 * @author jp
 */
public class SearchYGController {
    private final int CMD_PACK_LEN = 73;
    private final String BroadCastIP = "255.255.255.255";
    private final int NET_Port = 50000;
    
    public SearchYGController(){
    }
    
    /**
     * 搜索控制器
     * @return 控制器列表
     */
    @SuppressLint("DefaultLocale")
	public List<Controller> SearchControllers(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        List<Controller> controllers = new ArrayList<Controller>();
        Calendar time = Calendar.getInstance();
        cmd[0] = Protocol.Command.SEARCH.value;
        List<byte[]> recvpacks = new ArrayList<>();
        recvpacks = ExcuteCmdWithMultiAck(cmd, BroadCastIP, NET_Port);
        if(recvpacks==null){
        	return null;
        }else{
        for(byte[] ack : recvpacks){
            if(ack[0] == Protocol.Command.SEARCH.value){
                int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
                
                int sn = BaseConvert.FetchInt(intAck, 1, 4);
                String ip = String.format("%d.%d.%d.%d", intAck[5], intAck[6], intAck[7], intAck[8]);
                
                Controller controller = new Controller(NET_Port, ip, sn);
                
                
                controller.setMask(String.format("%d.%d.%d.%d", intAck[9], intAck[10], intAck[11], intAck[12]));
                controller.setGetway(String.format("%d.%d.%d.%d", intAck[13], intAck[14], intAck[15], intAck[16]));
                controller.setMAC(String.format("%x-%x-%x-%x-%x-%x", intAck[17], intAck[18], intAck[19], intAck[20], intAck[21], intAck[22]));
                controller.setVersion(String.format("%d%d", intAck[23], intAck[24]));
                time.set(intAck[25]*100 + intAck[26], intAck[27] - 1, intAck[28]);
                controller.setDate(time);
                
                Map<String,String> map = new HashMap<String,String>();
                map = controller.GetDoorCtrlInfo(1);
                
                if(map !=null){
                	
                	controller.setDoorNo(map.get("DoorNo")+"");
                	String CtrlType = map.get("CtrlType");
                	if(CtrlType!=null){
                		CtrlType = SwitchData.SwitchCtrlType(CtrlType);
                	}else{
                		CtrlType = SwitchData.SwitchCtrlType("3");
                	}
                	controller.setCtrlType(CtrlType);
                	controller.setOpenDelay(map.get("OpenDelay"));
                	String DoorEnable = map.get("DoorEnable");
                	if(DoorEnable!=null){
                		controller.setDoorEnable( SwitchData.SwitchDoorEnable(DoorEnable));
                	}else{
                		controller.setDoorEnable( SwitchData.SwitchDoorEnable("1"));
                	}
                	String IsSenak = map.get("IsSneak");
                	if(IsSenak!=null){
                		controller.setIsSneak(SwitchData.SwitchIsSneak(IsSenak));
                	}else{
                		controller.setIsSneak(SwitchData.SwitchIsSneak("1"));
                	}
                	controller.setPrivilegeType(map.get("PrivilegeType"));
                	controller.setVoiceAlarm(map.get("VoiceAlarm"));
                	controller.setBeeper(map.get("Beeper"));
                	controller.setIsTag(map.get("IsTag"));
                	
                	controller.setCloseToRemindDelay(map.get("CloseToRemindDelay"));
                	controller.setUnlockAlarm(map.get("UnlockAlarm"));
                	
                	controller.setServerUrl(new YGController().GetDevCtrlPlatform(controller.getmDevSN(), controller.getmDevIP()));
                	System.out.println("mj_map ========"+map);
                	
                	controllers.add(controller);
                }else{
                	return null;
                }
            }else{
            	return null;
        }
        }
      }
        return controllers;
    }
    
    

    
    public synchronized List<byte[]> ExcuteCmdWithMultiAck(byte[] cmd, String ip, int port){
        List<byte[]> recvpacks = new ArrayList<>();
        DatagramSocket mSocket = null;
        try {
            mSocket = new DatagramSocket();
            mSocket.setSoTimeout(1000);
            InetAddress addr = InetAddress.getByName(ip);
            DatagramPacket send = new DatagramPacket(cmd, cmd.length, addr, port);
            mSocket.send(send);
            do{
                byte[] recvbuf = new byte[cmd.length];
                DatagramPacket recv = new DatagramPacket(recvbuf, recvbuf.length);
                mSocket.receive(recv);
                recvpacks.add(recvbuf);
            }while(true);
        } catch (SocketException | UnknownHostException ex) {
            Log.i("error", ex.getMessage(), ex);
        } catch (IOException ex) {
        	ex.printStackTrace();
        }
        if(mSocket != null){
            mSocket.close(); 
        }
        
        if(recvpacks==null){
        	return null;
        }
        return recvpacks;
    }
}
