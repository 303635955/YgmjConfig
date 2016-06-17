/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

import android.util.Log;

/**
 *
 * @author jp
 */
class Udp {
    //地址设置
    //private final SocketAddress mLocalAddress;
    //接收socket
    private final DatagramSocket mLocalSocket;
   // private final Logger log = Logger.getLogger(Udp.class);
    private final int RecvTimeOut = 500;
    //private final String myLocalIp;
    //private final int localPort = 61114;
    //构造函数：port:接收监听端口 recvTimeOut:接收超时
    
    public Udp() throws SocketException {
        //myLocalIp = BaseConvert.GetHostLocalIp();
        //mLocalAddress = new InetSocketAddress(myLocalIp, localPort);
        
        //mLocalSocket = new DatagramSocket(mLocalAddress);
        mLocalSocket = new DatagramSocket();
        mLocalSocket.setSoTimeout(RecvTimeOut);
    }

    @Override
    protected void finalize() throws Throwable {
        if (mLocalSocket != null) {
            mLocalSocket.close();
        }
        super.finalize();
    }

    //接收数据（类构造函数里设置端口、超时）
    public synchronized int Recv(byte[] recvBuf) {
        int len = 0;
        try {
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            mLocalSocket.receive(recvPacket);
            InetAddress addr = recvPacket.getAddress();
            len = recvPacket.getLength();
        } catch (Exception e) {
            Log.i("erreo", e.getMessage(), e);
            len = -1;
        }
        return len;
    }
    
        //发送数据到指定IP和端口，返回1：成功 -1：失败
    public synchronized int Send(String ip, int port, byte[] data, int len) {
        int ret = -1;
        try {
            InetAddress addr = InetAddress.getByName(ip);
            DatagramPacket sendPacket = new DatagramPacket(data, len, addr, port);
            mLocalSocket.send(sendPacket);
            ret = 1;
        } catch (Exception e) {
        	Log.i("erreo", e.getMessage(), e);
            ret = -1;
        }
        return ret;
    }
}
