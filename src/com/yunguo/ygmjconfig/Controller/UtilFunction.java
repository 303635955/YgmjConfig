/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;

import java.util.Calendar;

/**
 *
 * @author Administrator
 */
class UtilFunction {
    
    public static int[] ByteToInt(byte[] ack, int len){
        int[] ret = new int[len];
        for(int i = 0; i < len; i++){
            ret[i] = (int)ack[i] & 0xff;
        }
        return ret;
    }
    
    public static int StortLong(byte[] cmd, int offset, int len, long value){
        for(int i = 0; i < Math.min(len, 8); i++){
            cmd[offset + i] = (byte)(value >> (8*i) & 0x0FF);
        }
        return Math.min(len, 8);
    }
    
    public static long FetchLong(byte[] cmd, int offset, int len){
        long value = 0;
        for (int i = 0; i < Math.min(len, 8); i++)
        {
            value |= (((long)cmd[offset + i] & 0x0FF) << (8 * i));
        }
        return value;
    }
    
    public static int StoreInt(byte[] cmd, int offset,int len, int value){
        for (int i = 0; i < Math.min(len, 4); i++)
        { 
            cmd[offset+i] = (byte)(value >> (8*i) & 0x0FF); 
        }
        return Math.min(len, 4);
    }
    
    
    
    public static int FetchInt(byte[] cmd, int offset, int len){
        int value = 0;
        for(int i = 0; i < Math.min(len, 4); i++){
            value |= ((int)cmd[offset + i] & 0x0FF) << (8 * i);
        }
        return value;
    }
    
    public static Calendar FetchTime(byte[] cmd, int offset, int len){
        Calendar time = null;
        int value = 0;
        //年月日
        if(len >=  4){
            time = Calendar.getInstance();
            time.set(Calendar.YEAR, FetchInt(cmd, offset, 2));
            time.set(Calendar.MONTH, FetchInt(cmd, offset+2, 1));
            time.set(Calendar.DATE, FetchInt(cmd, offset+3, 1));
            if(len >= 6){
                //年月日时分
                time.set(Calendar.HOUR, FetchInt(cmd, offset+4, 1));
                time.set(Calendar.MINUTE, FetchInt(cmd, offset+5, 1));
            }
            else if(len >= 7){
                //年月日时分秒
                time.set(Calendar.SECOND, FetchInt(cmd, offset+6, 1));
            }
        }
        return time;
    }
    
    public static int StoreTime(byte[] cmd, int offset, int len, Calendar time){
        int idx = 0;
        if((time != null) && (len >= 4)){
            idx += StoreInt(cmd, offset+idx, 2, time.get(Calendar.YEAR));
            idx += StoreInt(cmd, offset+idx, 1, time.get(Calendar.MONTH));
            idx += StoreInt(cmd, offset+idx, 1, time.get(Calendar.DATE));
            if(len >= 6){
                idx += StoreInt(cmd, offset+idx, 1, time.get(Calendar.HOUR));
                idx += StoreInt(cmd, offset+idx, 1, time.get(Calendar.MINUTE));
            }
            else if(len >= 7){
                idx += StoreInt(cmd, offset+idx, 1, time.get(Calendar.SECOND));
            }
        }
        return idx;
    }
    
    public static int NetAddrToInt(String addr){
        int value = 0;
        if(!addr.equals("")){
            String[] strs = addr.split("\\.");
            for(int i = 0; i < strs.length; i++){
                value |= Integer.valueOf(strs[i]) << 8*i;
            }
        }
        return value & 0xffffffff;
    }

}