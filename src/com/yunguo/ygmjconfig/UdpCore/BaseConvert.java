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
/**
 *
 * @author jp
 */
class BaseConvert {
    
    public static int[] ByteToInt(byte[] ack, int len){
        int[] ret = new int[len];
        for(int i = 0; i < len; i++){
            ret[i] = (int)ack[i] & 0xff;
        }
        return ret;
    }
    
    public static void StortLong(byte[] cmd, int offset, int len, long value){
        for(int i = 0; i < Math.min(len, 8); i++){
            cmd[offset + i] = (byte)(value >> (8*i) & 0xff);
        }
    }
    
    public static long FetchLong(int[] cmd, int offset, int len){
        long value = 0;
        for (int i = 0; i < Math.min(len, 8); i++)
        {
            value |= (long)cmd[offset + i] << (8 * i);
        }
        return value;
    }
    
    public static void StoreInt(byte[] cmd, int offset,int len, int value){
        for (int i = 0; i < Math.min(len, 4); i++)
            { 
                cmd[offset+i] = (byte)(value >> (8*i) & 0xFF); 
            }
    }
    
    
    
    public static int FetchInt(int[] cmd, int offset, int len){
        int value = 0;
        for(int i = 0; i < Math.min(len, 4); i++){
            value |= cmd[offset + i] << (8 * i);
        }
        return value;
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
