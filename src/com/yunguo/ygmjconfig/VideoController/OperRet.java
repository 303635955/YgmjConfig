/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.VideoController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 *
 * @author qxh
 */
public class OperRet
{
    public static final int RET_OK = 1;
    public int RetCode;
    public String Description;
    public OperRet()
    {
        RetCode=0;
        Description="操作失败";
    }
    public OperRet(int retCode, String desc)
    {
        RetCode = retCode;
        Description = desc;
    }
    public String toJson()
    {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(this, OperRet.class);
    }
}
