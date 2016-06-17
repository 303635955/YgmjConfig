/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.VideoController;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Administrator
 */
public class H3ConfigClient {
    private static final int NET_PORT = 51000;
    private static final String NET_IP = "255.255.255.255";
    
    public static ArrayList<ConfigData> searchDevice() 
    {
        DatagramSocket socket = null;
        ArrayList<ConfigData> devices = new ArrayList();
        try {
            byte[] recvbuff = new byte[2048];
            DatagramPacket recv = new DatagramPacket(recvbuff, recvbuff.length);
            socket = new DatagramSocket(0, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            //1. 生成命令包
            CfgProtocol p = new CfgProtocol();
            p.Operator = CfgProtocol.OP_GET;
            p.Data = null;
            p.SerialNo=0;
            //2. 发送
            DatagramPacket sendcmd = new DatagramPacket(p.toJson().getBytes(), p.toJson().getBytes().length, InetAddress.getByName(NET_IP), NET_PORT);
            socket.send(sendcmd);
            socket.setSoTimeout(2000);
            //3. 接收多个回复
            do{
                socket.receive(recv);
                if(recv.getLength() > 0) {
                    String json = new String(recv.getData(), 0, recv.getLength());
                    CfgProtocol ack = CfgProtocol.fromJson(json);
                    if(ack != null && ack.Operator == CfgProtocol.OP_GET_ACK) {
                        String dataJson = new GsonBuilder().create().toJson(ack.Data);
                        System.out.println("H3  JSON数据  ====="+dataJson);
                        ConfigData devData = null;
                        try {
                        	devData = ConfigData.fromJson(dataJson);
						} catch (Exception e) {
							Log.i("异常", e.toString());
						}
                        if(devData != null){
                            devices.add(devData);
                        }else{
                        	break;
                        }
                    }
                }
            }while(recv.getLength() > 0);
            
        } catch (SocketException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
        return devices;
    }
    
    public static boolean configDevice(ConfigData data, String desIp) 
    {
        boolean bret = false;
        DatagramSocket socket = null;
        
        if(data == null) {
            return false;
        }        
        
        try {
            CfgProtocol pcmd = new CfgProtocol();
            pcmd.Operator = CfgProtocol.OP_SET;
            pcmd.Data = data;
            pcmd.SerialNo = data.SerialNo;
            
            byte[] recvbuff = new byte[2048];
            DatagramPacket recvpack = new DatagramPacket(recvbuff, recvbuff.length);
            socket = new DatagramSocket(0, InetAddress.getByName("0.0.0.0"));
            socket.setBroadcast(true);
            DatagramPacket sendpack = new DatagramPacket(pcmd.toJson().getBytes(), pcmd.toJson().getBytes().length, InetAddress.getByName(desIp), NET_PORT);
            socket.send(sendpack);
            //接收
            socket.receive(recvpack);
            if(recvpack.getLength() > 0) {
                String json = new String(recvpack.getData(), 0, recvpack.getLength());
                CfgProtocol ack = CfgProtocol.fromJson(json);
                Gson gson = new GsonBuilder().create();
                String dataJson = gson.toJson(ack.Data);
                OperRet opRet = gson.fromJson(dataJson, OperRet.class);
                if(ack.Operator == CfgProtocol.OP_SET_ACK && opRet != null &&
                        opRet.RetCode == OperRet.RET_OK){
                    bret = true;
                }
            }
        } catch (SocketException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnknownHostException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(H3ConfigClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally {
            if(socket != null) {
                socket.close();
            }
        }
        return bret;
    }
}
