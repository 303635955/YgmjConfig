/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class protocolPacket
{
    final int PACK_LEN = 80;
    final int CMD_DATA_LEN = 64;
    public byte _cmd;
    public long _sn;
    public byte[] data;
    public int serialIndex;
    public int timeout;
    public protocolPacket(){ 
        data = new byte[CMD_DATA_LEN];
        timeout = 2000;
    }
    public protocolPacket(long sn, byte cmd){
        _sn = sn;
        _cmd = cmd;
        data = new byte[CMD_DATA_LEN];
        timeout = 2000;
    }
    
    public synchronized protocolPacket ExcuteCmd(String ip, int port){
        byte[] buf = new byte[PACK_LEN];
        byte[] recvbuf = new byte[PACK_LEN];
        protocolPacket recvPtlpack=null;
        DatagramPacket recvPacket = new DatagramPacket(recvbuf, recvbuf.length);
        DatagramSocket sock = null;
        try{
            sock = new DatagramSocket();
            sock.setSoTimeout(timeout);
            InetAddress addr = InetAddress.getByName(ip);
            /* 组包 */
            int offset = 0;
            offset += UtilFunction.StoreInt(buf, offset, 1, this._cmd);
            offset += UtilFunction.StortLong(buf, offset, 4, this._sn);
            System.arraycopy(data, 0, buf, offset, data.length);
            offset += data.length;
            offset += UtilFunction.StoreInt(buf, offset, 4, serialIndex);
            /* 发包 */
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addr, port);
            sock.send(sendPacket);
            sock.receive(recvPacket);
            recvPtlpack = new protocolPacket();
            offset = 0;
            recvPtlpack._cmd = (byte)UtilFunction.FetchInt(recvbuf, offset, 1);
            offset+=1;
            recvPtlpack._sn = UtilFunction.FetchInt(recvbuf, offset, 4);
            offset+=4;
            recvPtlpack.data = new byte[CMD_DATA_LEN];
            System.arraycopy(recvbuf, offset, recvPtlpack.data, 0, CMD_DATA_LEN);
            offset += CMD_DATA_LEN;
            recvPtlpack.serialIndex = UtilFunction.FetchInt(recvbuf, offset, 4);
            sock.close();
        }catch(Exception e){
        	e.printStackTrace();
            if(null != sock){
                sock.close();
            }
        }
        return recvPtlpack;
    }
    
    public synchronized List<protocolPacket> ExcuteBroadcastCmd(String ip, int port){
        byte[] buf = new byte[PACK_LEN];
        List<protocolPacket> recvPtlpacks= new ArrayList();
        
        DatagramSocket sock = null;
        try{
            sock = new DatagramSocket();
            sock.setSoTimeout(timeout);
            sock.setBroadcast(true);
            InetAddress addr = InetAddress.getByName(ip);
            /* 组包 */
            int offset = 0;
            offset += UtilFunction.StoreInt(buf, offset, 1, this._cmd);
            offset += UtilFunction.StortLong(buf, offset, 4, this._sn);
            System.arraycopy(data, 0, buf, offset, data.length);
            offset += data.length;
            offset += UtilFunction.StoreInt(buf, offset, 4, serialIndex);
            /* 发包 */
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addr, port);
            sock.send(sendPacket);
            for(int i=0; i <100; i++)
            {
                try
                {
                    byte[] recvbuf = new byte[PACK_LEN];
                    protocolPacket recvPtlpack=null;
                    DatagramPacket recvPacket = new DatagramPacket(recvbuf, recvbuf.length);
                    sock.receive(recvPacket);
                    recvPtlpack = new protocolPacket();
                    offset = 0;
                    recvPtlpack._cmd = (byte)UtilFunction.FetchInt(recvbuf, offset, 1);
                    offset+=1;
                    recvPtlpack._sn = UtilFunction.FetchLong(recvbuf, offset, 4);
                    offset+=4;
                    recvPtlpack.data = new byte[CMD_DATA_LEN];
                    System.arraycopy(recvbuf, offset, recvPtlpack.data, 0, CMD_DATA_LEN);
                    offset += CMD_DATA_LEN;
                    recvPtlpack.serialIndex = UtilFunction.FetchInt(recvbuf, offset, 4);
                    recvPtlpacks.add(recvPtlpack);
                }catch(SocketTimeoutException e){
                    break;
                }                
            }
            sock.close();
        }catch(Exception e){
        	e.printStackTrace();
            if(null != sock){
                sock.close();
            }
        }
        return recvPtlpacks;
    }
    
    public synchronized void SendCmd(String ip, int port){
        int ret = 0;
        byte[] buf = new byte[PACK_LEN];
        DatagramSocket sock = null;
        try{
            sock = new DatagramSocket();
            sock.setSoTimeout(2000);
            InetAddress addr = InetAddress.getByName(ip);
            /* 组包 */
            int offset = 0;
            offset += UtilFunction.StoreInt(buf, offset, 1, this._cmd);
            offset += UtilFunction.StortLong(buf, offset, 4, this._sn);
            System.arraycopy(data, 0, buf, offset, data.length);
            offset += data.length;
            offset += UtilFunction.StoreInt(buf, offset, 4, serialIndex);
            /* 发包 */
            DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, addr, port);
            sock.send(sendPacket);
            sock.close();
        }catch(Exception e){
        	e.printStackTrace();
            if(null != sock){
                sock.close();
            }
        }
    }
}
/**
 *
 * @author Administrator
 */
public class YGController extends IController {
    private final int YG_CTRL_PORT = 50000;
    private final int CMD_CHK_FLAG = 0x55AAAA55;
    private int mDevPort;
    private String mDevIP;
    private String mMask;
    private String mGetway;
    private String mMac;
    private String mVer;
    private long mDevSN;
    private int PackIndex = 0;

    
  
    
    enum Command{
        SEARCH((byte)0x10),
        SET_IP((byte)0x11),        
        SET_SERVER_INFO((byte)0x12),
        GET_SERVER_INFO((byte)0x13),        
        GET_DOOR_PARA((byte)0x15),
        SET_DOOR_PARA((byte)0x16),
        GET_STATUS((byte)0x17),
        GET_DATETIME((byte)0x19),
        SET_DATETIME((byte)0x18),
        GET_REC_BY_IDX((byte)0x1B),
        GET_SUM_REC((byte)0x1A),
        SET_REC_READED((byte)0x1C),
        GET_SUM_UNREAD((byte)0x1D),
        Delete_First_REC((byte)0x1E),
        CLEAR_REC((byte)0x1F),
        ADD_PRIVILIGE((byte)0x20),
        DEL_PRIVILIGE((byte)0x21),
        CLR_PRIVILIGE((byte)0x22),
        QUERY_PRIVILIGE((byte)0x23),
        GET_CNT_PRIVILIGE((byte)0x24),
        GET_PRI_BY_IDX((byte)0x25),
        OPEN_DOOR((byte)0x26),
        SIM_SWIPE((byte)0x2A),
        SET_PLATFORM((byte)0x2B),
        GET_PLATFORM((byte)0x2C);
        public byte value;
        private Command(byte value){
            this.value = value;
        }
    }
    
    public static List<IController> Search(){
        List<IController> ctrls = new ArrayList();
        protocolPacket pack = new protocolPacket(0, Command.SEARCH.value);
        List<protocolPacket> acks =  pack.ExcuteBroadcastCmd("255.255.255.255", 50000);
        for(protocolPacket p : acks){
            String ip = String.format("%d.%d.%d.%d", UtilFunction.FetchInt(p.data, 0, 1),
                    UtilFunction.FetchInt(p.data, 1, 1),
                    UtilFunction.FetchInt(p.data, 2, 1),
                    UtilFunction.FetchInt(p.data, 3, 1));
            String mask = String.format("%d.%d.%d.%d", UtilFunction.FetchInt(p.data, 4, 1),
                    UtilFunction.FetchInt(p.data, 5, 1),
                    UtilFunction.FetchInt(p.data, 6, 1),
                    UtilFunction.FetchInt(p.data, 7, 1));
            String getway = String.format("%d.%d.%d.%d", UtilFunction.FetchInt(p.data, 8, 1),
                    UtilFunction.FetchInt(p.data, 9, 1),
                    UtilFunction.FetchInt(p.data, 10, 1),
                    UtilFunction.FetchInt(p.data, 11, 1));
            String mac = String.format("%02x:%02x:%02x:%02x:%02x:%02x", UtilFunction.FetchInt(p.data, 12, 1),
                    UtilFunction.FetchInt(p.data, 13, 1),
                    UtilFunction.FetchInt(p.data, 14, 1),
                    UtilFunction.FetchInt(p.data, 15, 1),
                    UtilFunction.FetchInt(p.data, 16, 1),
                    UtilFunction.FetchInt(p.data, 17, 1));
            String ver = String.format("%d.%02d", UtilFunction.FetchInt(p.data, 18, 1),
                    UtilFunction.FetchInt(p.data, 19, 1));
            IController c = new YGController(p._sn, ver, 50000, ip, mask, getway, mac);
            ctrls.add(c);
        }
        return ctrls;
    }
    
    public YGController(long sn, String ver, int port, String ip, String mask, String getway, String mac){
        this.mDevPort = port;
        this.mDevIP = ip;
        this.mDevSN = sn;
        this.mVer = ver;
        this.mMask = mask;
        this.mGetway = getway;
        this.mMac = mac;
    }
    
    public YGController(){
    	
    }

    @Override
    public long GetSerialNo() {
        return mDevSN; 
    }
    @Override
    public String GetIP() {
        return mDevIP;
    }
    @Override
    public String GetMask() {
        return mMask;
    }    
    @Override
    public String GetGetway() {
        return mGetway;
    }    
    @Override
    public String GetMAC() {
        return mMac;
    }
    @Override
    public String GetVersion() {
        return mVer;
    }
    
    @Override
    public void StartListen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void StopListen() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Record GetStatus() {
        Record status = null;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_STATUS.value);
        pack.serialIndex = ++PackIndex;
        
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            status = new Record();
            status.RecIndex = UtilFunction.FetchInt (ack.data,  0, 4);
            status.Type     = UtilFunction.FetchInt (ack.data,  4, 1);
            status.Passed   = (byte)UtilFunction.FetchInt (ack.data, 5, 1);
            status.DoorNo   = UtilFunction.FetchInt (ack.data, 6, 1);
            status.InOut    = UtilFunction.FetchInt (ack.data, 7, 1);
            status.CardNo   = UtilFunction.FetchLong (ack.data, 8, 8);
            status.Time     = UtilFunction.FetchTime(ack.data, 16, 7);
            status.DoorStatus = UtilFunction.FetchInt(ack.data, 23, 4);
            status.ReasonCode = UtilFunction.FetchInt(ack.data, 27, 1); 
        }
        return status;
    }

    @Override
    public void SetIP(String ip, String mask, String getway, Boolean dhcp) {
        int idx = 0;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.SET_IP.value);
        //DHCP
        idx += UtilFunction.StoreInt(pack.data, idx, 1, dhcp?1:0);
        if(dhcp){
            idx += UtilFunction.StoreInt(pack.data, idx, 4, UtilFunction.NetAddrToInt(ip));
            idx += UtilFunction.StoreInt(pack.data, idx, 4, UtilFunction.NetAddrToInt(mask));
            idx += UtilFunction.StoreInt(pack.data, idx, 4, UtilFunction.NetAddrToInt(getway));
        }
        idx += UtilFunction.StoreInt(pack.data, 13, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        pack.SendCmd(mDevIP, mDevPort);
    }

    @Override
    public Boolean SetTime(Calendar time) {
        Boolean bret = false;
        int idx = 0;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.SET_DATETIME.value);
        pack.serialIndex = ++PackIndex;
        
        idx += UtilFunction.StoreTime(pack.data, idx, 7, time);
        UtilFunction.StoreInt(pack.data, idx, 4, CMD_CHK_FLAG);
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
         if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public Calendar GetTime() {
        Calendar time = null;
        int idx = 0;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_DATETIME.value);
        pack.serialIndex = ++PackIndex;
        
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
         if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            time = UtilFunction.FetchTime(ack.data, 0, 7);
        }
        return time;
    }

    @Override
    public int GetPriviligeCnt() {
        int cnt = 0;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_CNT_PRIVILIGE.value);
        pack.serialIndex = ++PackIndex;
        
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
         if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            cnt = UtilFunction.FetchInt(ack.data, 0, 4);
        }
        return cnt;
    }

    @Override
    public Privilige GetPriviligeAt(int index) {
        int idx = 0;
        int cnt = 0;
        Privilige prv = null;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_PRI_BY_IDX.value);
        UtilFunction.StoreInt(pack.data, 0, 4, index);
        pack.serialIndex = ++PackIndex;
        
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            prv = new Privilige();
            prv.CardNo = UtilFunction.FetchInt(ack.data, 0, 8);
            prv.BeginTime = UtilFunction.FetchTime(ack.data, 8, 6);
            prv.EndTime = UtilFunction.FetchTime(ack.data, 14, 6);
            prv.DoorEnabled = UtilFunction.FetchInt(ack.data, 20, 4);
            System.arraycopy(ack.data, 24, prv.Password, 0, 6);
        }
        return prv;
    }

    @Override
    public Boolean AddPrivilige(Privilige p) {
        int idx = 0;
        int pws = 0;
        Boolean bret = false;
        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.ADD_PRIVILIGE.value);
        idx += UtilFunction.StortLong(pack.data, idx, 8, p.CardNo);
        idx += UtilFunction.StoreTime(pack.data, idx, 6, p.BeginTime);
        idx += UtilFunction.StoreTime(pack.data, idx, 6, p.EndTime);
        idx += UtilFunction.StoreInt(pack.data, idx, 4, p.DoorEnabled);        
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[0]);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[1]);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[2]);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[3]);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[4]);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, p.Password[5]);
        idx += UtilFunction.StoreInt(pack.data, idx, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public Boolean EditPrivilige(Privilige p) {
        return AddPrivilige(p);
    }

    @Override
    public Boolean DeletePrivilige(long cardno) {
        int idx = 0;
        int pws = 0;
        Boolean bret = false;
        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.DEL_PRIVILIGE.value);
        idx += UtilFunction.StortLong(pack.data, idx, 8, cardno);
        idx += UtilFunction.StoreInt(pack.data, idx, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public Privilige GetPrivilige(long cardno) {
        int idx = 0;
        int cnt = 0;
        Privilige prv = null;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.QUERY_PRIVILIGE.value);
        UtilFunction.StortLong(pack.data, 0, 8, cardno);
        pack.serialIndex = ++PackIndex;
        
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            prv = new Privilige();
            prv.CardNo = UtilFunction.FetchLong(ack.data, 0, 8);
            prv.BeginTime = UtilFunction.FetchTime(ack.data, 8, 6);
            prv.EndTime = UtilFunction.FetchTime(ack.data, 14, 6);
            prv.DoorEnabled = UtilFunction.FetchInt(ack.data, 20, 4);
            System.arraycopy(ack.data, 24, prv.Password, 0, 6);
        }
        return prv;
    }

    @Override
    public int CountPrivilige() {
        int cnt = 0;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_CNT_PRIVILIGE.value);        
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            cnt = UtilFunction.FetchInt(ack.data, 0, 4);
        }
        return cnt;
    }

    @Override
    public Boolean ClearPrivilige() {
        Boolean bret = false;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.CLR_PRIVILIGE.value);
        UtilFunction.StoreInt(pack.data, 0, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public Record GetRecordAt(int index) {
        int idx = 0;   
        Record rec = null;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_REC_BY_IDX.value);
        idx += UtilFunction.StoreInt(pack.data, idx, 4, index);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            rec = new Record();
            rec.RecIndex = UtilFunction.FetchInt(ack.data, 0, 4);
            rec.Type = UtilFunction.FetchInt(ack.data, 4, 1);
            rec.Passed = (byte)UtilFunction.FetchInt(ack.data, 5, 1);
            rec.DoorNo = UtilFunction.FetchInt(ack.data, 6, 1);
            rec.InOut = UtilFunction.FetchInt(ack.data, 7, 1);
            rec.CardNo = UtilFunction.FetchLong(ack.data, 8, 8);
            rec.Time = UtilFunction.FetchTime(ack.data, 16, 7);
            rec.ReasonCode = UtilFunction.FetchInt(ack.data, 23, 1);
        }
        return rec;
    }

    @Override
    public int CountUnreadRecord() {
        int cnt = 0;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_SUM_UNREAD.value);        
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            cnt = UtilFunction.FetchInt(ack.data, 0, 4);
        }
        return cnt;
    }

    @Override
    public int CountRecordTotal() {
        int cnt = 0;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_SUM_REC.value);        
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            cnt = UtilFunction.FetchInt(ack.data, 0, 4);
        }
        return cnt;
    }

    @Override
    public Boolean DeleteRecord(int index) {
        Boolean bret = false;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.Delete_First_REC.value);
        UtilFunction.StoreInt(pack.data, 0, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }
    
    @Override
    public Boolean SetRecordReaded(int index) {
        Boolean bret = false;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.SET_REC_READED.value);
        UtilFunction.StoreInt(pack.data, 0, 4, index);
        UtilFunction.StoreInt(pack.data, 4, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public Boolean OpenDoor() {
        Boolean bret = false;        
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.OPEN_DOOR.value);
        UtilFunction.StoreInt(pack.data, 0, 4, 1);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }

    @Override
    public ControlParam GetControlParam(long doorno) {
        ControlParam param = null;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.GET_DOOR_PARA.value);
        UtilFunction.StoreInt(pack.data, 0, 4, 1);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            param = new ControlParam();
            param.DoorNo = UtilFunction.FetchInt(ack.data, 0, 1);
            param.CtrlType = UtilFunction.FetchInt(ack.data, 1, 1);
            param.OpenDuration = UtilFunction.FetchInt(ack.data, 2, 1);
            param.PriviligeType = UtilFunction.FetchInt(ack.data, 3, 1);
            //param.AlarmEnable = UtilFunction.FetchInt(ack.data, 4, 1);
            param.PressEnable = (UtilFunction.FetchInt(ack.data, 5, 1)==1)?true:false;
            param.AlarmEnable = (UtilFunction.FetchInt(ack.data, 6, 1)==1)?true:false;
            param.CloseAttenDuration = UtilFunction.FetchInt(ack.data, 7, 2);
            param.DestroyAlarm = (UtilFunction.FetchInt(ack.data, 9, 1)==1)?true:false;
            param.DefendSlip = (UtilFunction.FetchInt(ack.data, 10, 1)==1)?true:false;
            param.DefendShadow = (UtilFunction.FetchInt(ack.data, 11, 1)==1)?true:false;
        }
        return param;
    }

    @Override
    public Boolean SetControlParam(ControlParam param) {
        int idx = 0;
        Boolean bret = false;
        protocolPacket pack = new protocolPacket(this.mDevSN, Command.SET_DOOR_PARA.value);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.DoorNo);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.CtrlType);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.OpenDuration);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.PriviligeType);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, 0);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.PressEnable?1:0);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.AlarmEnable?1:0);
        idx += UtilFunction.StoreInt(pack.data, idx, 2, param.CloseAttenDuration);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.DestroyAlarm?1:0);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.DefendSlip?1:0);
        idx += UtilFunction.StoreInt(pack.data, idx, 1, param.DefendShadow?1:0);
        UtilFunction.StoreInt(pack.data, idx, 4, CMD_CHK_FLAG);
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(mDevIP, mDevPort);
        if((ack != null) && (ack._sn == this.mDevSN) && (ack._cmd == pack._cmd)){
            bret = ack.data[1] == 1;
        }
        return bret;
    }

    @Override
    public Boolean SimuSwipe(long cardno, int readerno) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String GetDevCtrlPlatform(long sn,String IP){
        String url = "";
        protocolPacket pack = new protocolPacket(sn, Command.GET_PLATFORM.value);
        
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(IP, 50000);
        if((ack != null) && (ack._sn == sn) && (ack._cmd == pack._cmd)){
            url = new String(ack.data);
        }
        url = url.replaceAll(" ","");
        url = url.trim();
        return url;
    }

    @Override
    public boolean SetDevCtrlPlatform(String url,long sn,String IP) {
        Boolean bret = false;        
        protocolPacket pack = new protocolPacket(sn, Command.SET_PLATFORM.value);
        //平台地址
        System.arraycopy(url.getBytes(), 0, pack.data, 0, url.length());
        
        pack.serialIndex = ++PackIndex;
        protocolPacket ack = pack.ExcuteCmd(IP, 50000);
        if((ack != null) && (ack._sn == sn) && (ack._cmd == pack._cmd)){
            bret = ack.data[0] == 1;
        }
        return bret;
    }
}
