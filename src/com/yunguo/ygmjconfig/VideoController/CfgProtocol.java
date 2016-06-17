/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.VideoController;

import com.google.gson.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author qxh
 */

public class CfgProtocol {
    public static final int OP_GET=0x10;
    public static final int OP_GET_ACK=0x11;
    public static final int OP_SET=0x20;
    public static final int OP_SET_ACK=0x21;
    public final int Flag = 0x8134;  //标识
    public int Operator;     //操作标志
    public int SerialNo;     //序列号
    public Object Data;
    
    
    public String toJson()
    {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, CfgProtocol.class);
    }
    
    public static CfgProtocol fromJson(String json)
    {
        CfgProtocol p = null;
        Gson gson = new GsonBuilder().create();
        try{
            p = gson.fromJson(json, CfgProtocol.class);
        }catch(Exception ex){
            Logger.getLogger(CfgProtocol.class.getName()).log(Level.WARNING, "convert json to object error");
        }
        return p;
    }
    
}
