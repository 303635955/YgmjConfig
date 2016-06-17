/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jp
 */
public class Protocol {
    private final int CMD_PACK_LEN = 73;
    private final int CMD_FLAG = 0x55AAAA55;
    private int mDevPort;
    private String mDevIP;
    private  long mDevSN;
    private int PackIndex = 0;
    protected StatusInfo LastStatus;
    
    public Protocol(){
    	
    }
    
    public Protocol(int port, String ip, long sn){
        this.mDevPort = port;
        this.mDevIP = ip;
        this.mDevSN = sn;
    }

    public int getmDevPort() {
        return mDevPort;
    }

    public String getmDevIP() {
        return mDevIP;
    }
    public void setmDevIP(String Ip) {
        this.mDevIP =Ip;
    }

    public long getmDevSN() {
        return mDevSN;
    }
    
    public  void setmDevSN(long sn) {
        mDevSN = sn;
    }
    
    enum Command{
        SEARCH((byte)0x10),
        SET_IP((byte)0x11),
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
        GET_DOOR_INFO((byte)0x15),
        SET_DOOR_INFO((byte)0x16),
        SET_SERVER_INFO((byte)0x12),
        GET_SERVER_INFO((byte)0x13);
        public byte value;
        private Command(byte value){
            this.value = value;
        }
    }
    
    /**
     * 设置控制器ip
     * @param dhcp 是否启用dhcp 1启用0不启用
     * @param ip
     * @param mask 掩码
     * @param getway 网关
     */
    
    public void SetControllerIP(int dhcp, String ip, String mask, String getway){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.SET_IP.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 1, dhcp);
        if(dhcp == 0){
            BaseConvert.StoreInt(cmd, 6, 4, BaseConvert.NetAddrToInt(ip));
            BaseConvert.StoreInt(cmd, 10, 4, BaseConvert.NetAddrToInt(mask));
            BaseConvert.StoreInt(cmd, 14, 4, BaseConvert.NetAddrToInt(getway));
        }
        BaseConvert.StoreInt(cmd, 18, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        ExcuteCmd(cmd, mDevIP, mDevPort);
    }
    /**
     * 获取门磁状态
     * @return 
     */
    public int GetDoorContact(){
        StatusInfo CurrentStatus = null;
        int ret = 0;
        CurrentStatus = GetControllerStatus();
        if(CurrentStatus != null){
            ret = CurrentStatus.getDoorContact();
        }
        return ret;
    }
    
    /**
     * 获取控制器状态（最后一条刷卡记录）
     * @return 返回控制器的状态
     */
    public synchronized StatusInfo GetControllerStatus(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        StatusInfo status = null;
        Calendar Stime = Calendar.getInstance();
        Calendar Ctime = Calendar.getInstance();
        cmd[0] = Command.GET_STATUS.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_STATUS.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            status = new StatusInfo();
            status.setIndex(BaseConvert.FetchInt(intAck, 5, 4));
            status.setType((byte)intAck[9]);
            status.setValid((intAck[10] == 1));
            status.setDoorNo((byte)intAck[11]);
            status.setInOut((byte)intAck[12]);
            status.setCardNo(BaseConvert.FetchLong(intAck, 13, 8));
            int Syear = BaseConvert.FetchInt(intAck, 21, 2);
            Stime.set(Syear, intAck[23] - 1, intAck[24], 
                    intAck[25], intAck[26], intAck[27]);
            status.setSwipeTime(Stime);
            status.setDoorContact(BaseConvert.FetchInt(intAck, 28, 4));
            status.setExcptionNo((byte)intAck[32]);
            int Cyear = BaseConvert.FetchInt(intAck, 33, 2);
            Ctime.set(Cyear, intAck[35] - 1, intAck[36], 
                    intAck[37], intAck[38], intAck[39]);
            status.setCurTime(Ctime);
            return status;
        }
        return status;
    }
    
    public Map<String, String> GetCurrentRecord(){
        StatusInfo CurrentStatus = null;
        Map<String, String> map = null;
        CurrentStatus = GetControllerStatus();
        if(CurrentStatus != null){
            if(LastStatus == null){
                LastStatus = CurrentStatus;
                CurrentStatus = null;
                return null;
            }else if((CurrentStatus.getIndex() == LastStatus.getIndex())){
                CurrentStatus = null;
                System.gc();
                return null;
            }
            //更新状态
            LastStatus = CurrentStatus;
            map = EntityConvert.StatusToMap(CurrentStatus);
        }
        CurrentStatus = null;
        return map;
    }
    
    /**
     * 查询控制器时间日期
     * @return 返回控制器时间 
     */
    public Calendar GetDateTime(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        Calendar time = Calendar.getInstance();
        cmd[0] = Command.GET_DATETIME.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_DATETIME.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            int year = BaseConvert.FetchInt(intAck, 5, 2);
            time.set(year , intAck[7] - 1, intAck[8], 
                    intAck[9], intAck[10], intAck[11]);
            return time;
        }
        return null;
    }
    
    /**
     * 设置控制器时间日期
     * @param time 控制器时间日期
     * @return 成功返回true 失败返回false
     */
    public boolean SetDateTime(Calendar time){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.SET_DATETIME.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 2, time.get(Calendar.YEAR));
        BaseConvert.StoreInt(cmd, 7, 1, time.get(Calendar.MONTH) + 1);
        BaseConvert.StoreInt(cmd, 8, 1, time.get(Calendar.DAY_OF_MONTH));
        BaseConvert.StoreInt(cmd, 9, 1, time.get(Calendar.HOUR_OF_DAY));
        BaseConvert.StoreInt(cmd, 10, 1, time.get(Calendar.MINUTE));
        BaseConvert.StoreInt(cmd, 11, 1, time.get(Calendar.SECOND));
        BaseConvert.StoreInt(cmd, 12, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.SET_DATETIME.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            return ret = true;
        }
        return ret;
    }
    
    /**
     * 获取记录总数
     * @return 
     */
    public int GetRecordSum(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        int sum = 0;
        cmd[0] = Command.GET_SUM_REC.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_SUM_REC.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            sum = BaseConvert.FetchInt(intAck, 5, 4);
        }
        return sum;
    }
    
    /**
     * 删除一条记录
     * 记录Index 从0 开始。 改命令删除的是第一条记录，而不是第一条未读记录
     * @return 
     */
    public boolean DeleteFirstRecord(){
        boolean ret = false;
        byte[] cmd = new byte[CMD_PACK_LEN];
        cmd[0] = Command.Delete_First_REC.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.Delete_First_REC.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                ret = true;
            }
        }
        return ret;
    }
    /**
     * 根据指定索引号查询刷卡记录
     * @param index 指定的索引号
     * @return 返回刷卡记录
     * @throws Exception 索引号小于0抛出异常
     */
    public Map<String,String> GetRecordByIndex(int index) throws Exception{
        byte[] cmd = new byte[CMD_PACK_LEN];
        SwipeRecordInfo record = null;
        Calendar time = Calendar.getInstance();
        Map<String,String> map = null;
        if(index < 0){
            throw new Exception("不是正确的索引号" + index);
        }
        cmd[0] = Command.GET_REC_BY_IDX.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 4, index);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_REC_BY_IDX.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            record = new SwipeRecordInfo();
            record.setIndex(BaseConvert.FetchInt(intAck, 5, 8));
            record.setType((byte)intAck[9]);
            record.setValid((intAck[10] == 1));
            record.setDoorNo((byte)intAck[11]);
            record.setInOut((byte)intAck[12]);
            record.setCardNo(BaseConvert.FetchLong(intAck, 13, 8));
            int year = BaseConvert.FetchInt(intAck, 21, 2);
            time.set(year, intAck[23] - 1, intAck[24], 
                    intAck[25], intAck[26], intAck[27]);
            record.setSwipeTime(time);
            map = EntityConvert.RecordToMap(record);
        }
        return map;
    }
    
    /**
     * 设置记录为已读
     * @param index 要设置的索引号
     * @return 成功返回true 失败返回false
     */
    public boolean SetRecordReaded(int index){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.SET_REC_READED.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 4, index);
        BaseConvert.StoreInt(cmd, 9, 12, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.SET_REC_READED.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                return ret = true;
            }
        }
        return false;
    }
    
    /**
     * 查询最后一条已读记录索引
     * @return 返回查询到的索引
     */
    public int GetUnreadSum(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        int value = 0;
        cmd[0] = Command.GET_SUM_UNREAD.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_SUM_UNREAD.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            value = BaseConvert.FetchInt(intAck, 5, 4);
        }
        return value;
    }
    
    /**
     * 清空控制器记录
     * @return 成功返回true 失败返回false
     */
    public boolean ClearRecord(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.CLEAR_REC.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 8, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.CLEAR_REC.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                return ret = true;
            }
        }
        return ret;
    }
    
    /**
     * 添加或者修改权限
     * @param map 要添加或者修改的权限数据
     * @return 成功返回true 失败返回false
     */
    public boolean AddOrModifyPrivilege(Map<String,String> map){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        Calendar Stime = Calendar.getInstance();
        Calendar Etime = Calendar.getInstance();
        if(map == null){
            return ret;
        }
        PrivilegeInfo privilege = EntityConvert.MapToPrivilege(map);
        cmd[0] = Command.ADD_PRIVILIGE.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StortLong(cmd, 5, 8, privilege.getCardNo());
        Stime = privilege.getStartDate();
        BaseConvert.StoreInt(cmd, 13, 2, Stime.get(Calendar.YEAR));
        BaseConvert.StoreInt(cmd, 15, 1, Stime.get(Calendar.MONTH) + 1);
        BaseConvert.StoreInt(cmd, 16, 1, Stime.get(Calendar.DAY_OF_MONTH));
        BaseConvert.StoreInt(cmd, 17, 1, Stime.get(Calendar.HOUR_OF_DAY));
        BaseConvert.StoreInt(cmd, 18, 1, Stime.get(Calendar.MINUTE));
        
        Etime = privilege.getEndDate();
        BaseConvert.StoreInt(cmd, 19, 2, Etime.get(Calendar.YEAR));
        BaseConvert.StoreInt(cmd, 21, 1, Etime.get(Calendar.MONTH) + 1);
        BaseConvert.StoreInt(cmd, 22, 1, Etime.get(Calendar.DAY_OF_MONTH));
        BaseConvert.StoreInt(cmd, 23, 1, Etime.get(Calendar.HOUR_OF_DAY));
        BaseConvert.StoreInt(cmd, 24, 1, Etime.get(Calendar.MINUTE));
        
        BaseConvert.StoreInt(cmd, 25, 4, privilege.getDoorNoValid());
        int pass = privilege.getPassWord();
        for(int i = 0; i < 6; i++){
            cmd[34 - i] = (byte)(pass % Math.pow(10, i + 1) / Math.pow(10, i));
        }
        BaseConvert.StoreInt(cmd, 35, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.ADD_PRIVILIGE.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                return ret = true;
            }
        }
        return ret;
    }
    
    /**
     * 根据卡号删除权限
     * @param CardNo    要删除权限的卡号
     * @return 
     */
    public boolean DeletePrivilege(long CardNo){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.DEL_PRIVILIGE.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StortLong(cmd, 5, 8, CardNo);
        BaseConvert.StoreInt(cmd, 13, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.DEL_PRIVILIGE.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                return ret = true;
            }
        }
        return ret;
    }
    
    /**
     * 清空权限
     * @return 
     */
    public boolean ClearPrivilege(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.CLR_PRIVILIGE.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.CLR_PRIVILIGE.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                return ret = true;
            }
        }
        return ret;
    }
    
    /**
     * 查询权限
     * @param CardNo 卡号
     * @return 成功返回权限  失败返回null;
     */
    public Map<String,String> GetPrivilegeToCard(long CardNo){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        Map<String,String> map = null;
        Calendar Stime = Calendar.getInstance();
        Calendar Etime = Calendar.getInstance();
        cmd[0] = Command.QUERY_PRIVILIGE.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StortLong(cmd, 5, 8, CardNo);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.QUERY_PRIVILIGE.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            map = new HashMap<String,String>();
            PrivilegeInfo privilege = new PrivilegeInfo();
            privilege.setCardNo(BaseConvert.FetchLong(intAck, 5, 8));
            int Syear = BaseConvert.FetchInt(intAck, 13, 2);
            Stime.set(Syear, intAck[15] - 1, intAck[16], 
                    intAck[17], intAck[18]);
            privilege.setStartDate(Stime);
            int Eyear = BaseConvert.FetchInt(intAck, 19, 2);
            Etime.set(Eyear, intAck[21] - 1, intAck[22], 
                    intAck[23], intAck[24]);
            privilege.setEndDate(Etime);
            privilege.setDoorNoValid(BaseConvert.FetchInt(intAck, 25, 4));
            privilege.setPassWord(mDevPort);
            int pass = 0;
            for(int i = 0; i < 6; i++){
                pass += (int)Math.pow(intAck[29 + i], 5 - i);
            }
            privilege.setPassWord(pass);
            map = EntityConvert.PrivilegeToMap(privilege);
        }
        return map;
    }
    
    /**
     * 返回权限总数
     * @return 查询失败返回-1
     */
    public int GetPrivilegeSum(){
        byte[] cmd = new byte[CMD_PACK_LEN];
        int sum = 0;
        cmd[0] = Command.GET_CNT_PRIVILIGE.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_CNT_PRIVILIGE.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            sum = BaseConvert.FetchInt(intAck, 5, 4);
        }
        else{
            sum = -1;
        }
        return sum;
    }
    
    /**
     * 根据索引号查询权限
     * @param index
     * @return 失败返回null
     */
    public Map GetPrivilegeToIndex(int index){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        Map<String,String> map = null;
        Calendar Stime = Calendar.getInstance();
        Calendar Etime = Calendar.getInstance();
        cmd[0] = Command.GET_PRI_BY_IDX.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StortLong(cmd, 5, 8, index);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_PRI_BY_IDX.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            map = new HashMap<String,String>();
            PrivilegeInfo privilege = new PrivilegeInfo();
            privilege.setCardNo(BaseConvert.FetchLong(intAck, 5, 8));
            int Syear = BaseConvert.FetchInt(intAck, 13, 2);
            Stime.set(Syear, intAck[15] - 1, intAck[16], 
                    intAck[17], intAck[18]);
            privilege.setStartDate(Stime);
            int Eyear = BaseConvert.FetchInt(intAck, 19, 2);
            Etime.set(Eyear, intAck[21] - 1, intAck[22], 
                    intAck[23], intAck[24]);
            privilege.setEndDate(Etime);
            privilege.setDoorNoValid(BaseConvert.FetchInt(intAck, 25, 4));
            privilege.setPassWord(mDevPort);
            int pass = 0;
            for(int i = 0; i < 6; i++){
                pass += (int)Math.pow(intAck[29 + i], 5 - i);
            }
            privilege.setPassWord(pass);
            map = EntityConvert.PrivilegeToMap(privilege);
        }
        return map;
    }
    
    /**
     * 远程开门
     * @param DoorNo
     * @return 
     */
    public boolean OpenDoor(int DoorNo){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        cmd[0] = Command.OPEN_DOOR.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 4, DoorNo);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.OPEN_DOOR.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(DoorNo == BaseConvert.FetchInt(intAck, 5, 4)){
                ret = true;
            }
        }
        return ret;
    }
    /**
     * 获取门控制参数
     * @param DoorNo
     * @return 失败返回null
     */
    public Map GetDoorCtrlInfo(int DoorNo){
        byte[] cmd = new byte[CMD_PACK_LEN];
        Map<String,String> map = null;
        cmd[0] = Command.GET_DOOR_INFO.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 1, DoorNo);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.GET_DOOR_INFO.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            DoorCtrlInfo door = new DoorCtrlInfo();
            map = new HashMap<String,String>();
            door.setDoorNo((byte)intAck[5]);
            door.setCtrlType((byte)intAck[6]);
            door.setOpenDelay((byte)intAck[7]);
            door.setPrivilegeType((byte)intAck[8]);
            door.setVoiceAlarm((byte)intAck[9]);
            door.setDoorEnable((byte)intAck[10]);
            door.setBeeper((byte)intAck[11]);
            door.setCloseToRemindDelay(BaseConvert.FetchInt(intAck, 12, 2));
            door.setUnlockAlarm((byte)intAck[14]);
            door.setIsSneak((byte)intAck[15]);
            door.setIsTag((byte)intAck[16]); 
            map = EntityConvert.DoorCtrlInfoToMap(door);
        }
        return map;
    }
    
    /**
     * 设置门控制参数
     * @param map
     * @return 
     */
    public boolean SetDoorCtrlInfo(Map<String,String> map){
        byte[] cmd = new byte[CMD_PACK_LEN];
        boolean ret = false;
        if(map == null){
            return ret;
        }
        DoorCtrlInfo door = EntityConvert.MapToDoorCtrlInfo(map);
        cmd[0] = Command.SET_DOOR_INFO.value;
        BaseConvert.StortLong(cmd, 1, 4, mDevSN);
        BaseConvert.StoreInt(cmd, 5, 1, door.getDoorNo());
        BaseConvert.StoreInt(cmd, 6, 1, door.getCtrlType());
        BaseConvert.StoreInt(cmd, 7, 1, door.getOpenDelay());
        BaseConvert.StoreInt(cmd, 8, 1, door.getPrivilegeType());
        BaseConvert.StoreInt(cmd, 9, 1, door.getVoiceAlarm());
        BaseConvert.StoreInt(cmd, 10, 1, door.getDoorEnable());
        BaseConvert.StoreInt(cmd, 11, 1, door.getBeeper());
        BaseConvert.StoreInt(cmd, 12, 2, door.getCloseToRemindDelay());
        BaseConvert.StoreInt(cmd, 14, 1, door.getUnlockAlarm());
        BaseConvert.StoreInt(cmd, 15, 1, door.getIsSneak());
        BaseConvert.StoreInt(cmd, 16, 1, door.getIsTag());
        BaseConvert.StoreInt(cmd, 17, 4, CMD_FLAG);
        BaseConvert.StoreInt(cmd, 69, 4, ++PackIndex);
        byte[] ack = ExcuteCmd	(cmd, mDevIP, mDevPort);
        int[] intAck = BaseConvert.ByteToInt(ack, ack.length);
        if(ack[0] == Command.SET_DOOR_INFO.value && BaseConvert.FetchInt(intAck, 69, 4) == PackIndex){
            if(intAck[5] == 1){
                ret = true;
            }
        }
    return ret;
    }
    

    
    private synchronized byte[] ExcuteCmd(byte[] cmd, String ip, int port){
        int ret = 0;
        byte[] recvbuf = new byte[cmd.length];
        ret = AppCtrl.GetAppCtrl().GetUdp().Send(ip, port, cmd, cmd.length);
        if((ret == 1) && (cmd[1] != Command.SET_IP.value)){
            ret = AppCtrl.GetAppCtrl().GetUdp().Recv(recvbuf);
        }
        return recvbuf;
    }
    
}
