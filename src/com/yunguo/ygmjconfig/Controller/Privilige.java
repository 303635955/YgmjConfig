/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Controller;

import java.util.Calendar;

/**
 * 门禁权限类
 * @author Administrator
 */
public class Privilige {
    /// <summary>
    /// 卡号
    /// </summary>
    public long CardNo;
    /// <summary>
    /// 权限起始时间
    /// </summary>
    public Calendar BeginTime;
    /// <summary>
    /// 权限结束时间
    /// </summary>
    public Calendar EndTime;
    /// <summary>
    /// 门权限0-31Bit 分别表示门1--门32的权限, 1：启用, 0：禁止 
    /// </summary>
    public int DoorEnabled;

    /// <summary>
    /// 用户密码
    /// </summary>
    public byte[] Password;

    public Privilige()
    {
        Password = new byte[6];
    }
    
    public Boolean Equals(Object obj)
    {
        Boolean bret = false;
        Privilige p = (Privilige)obj;
        if(p != null)
        {
            if ((CardNo == p.CardNo)
                && (BeginTime.get(Calendar.YEAR) == p.BeginTime.get(Calendar.YEAR))
                && (BeginTime.get(Calendar.MONTH) == p.BeginTime.get(Calendar.MONTH))
                && (BeginTime.get(Calendar.DATE)== p.BeginTime.get(Calendar.DATE))
                && (EndTime.get(Calendar.YEAR) == p.EndTime.get(Calendar.YEAR))
                && (EndTime.get(Calendar.MONTH) == p.EndTime.get(Calendar.MONTH))
                && (EndTime.get(Calendar.DATE) == p.EndTime.get(Calendar.DATE))
                && (DoorEnabled == p.DoorEnabled))
            {
                bret = true;
                for(int i = 0; i < this.Password.length; i++)
                {
                    if (Password[i] != p.Password[i])
                    {
                        bret = false;
                        break;
                    }
                }
            }
        }
        return bret;
    }
}
