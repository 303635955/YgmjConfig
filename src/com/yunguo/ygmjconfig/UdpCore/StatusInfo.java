/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.util.Calendar;

/**
 *
 * @author jp
 */
public class StatusInfo {
    private int Index;           //最后一条记录的索引号 (=0表示没有记录)
    private byte Type;               //记录类型 0=无记录 1=刷卡记录 2=门磁,按钮, 设备启动, 远程开门记录 3=报警记录
    private boolean Valid;              //有效性(0 表示不通过, 1表示通过)
    private byte DoorNo;             //门号(1,2,3,4)
    private byte InOut;              //进门/出门(1表示进门, 2表示出门)
    private long CardNo;              //卡号
    
    private Calendar SwipeTime;          //刷卡时间年月日时分秒 (采用BCD码)
    private int DoorContact;                     //门磁状态(Bit0-Bit31分别表示1-32号门)的状态
    private byte ExcptionNo;             //故障号
    private Calendar CurTime;                //控制器当前时间

    public int getIndex() {
        return Index;
    }

    public void setIndex(int Index) {
        this.Index = Index;
    }

    public byte getType() {
        return Type;
    }

    public void setType(byte Type) {
        this.Type = Type;
    }

    public boolean isValid() {
        return Valid;
    }

    public void setValid(boolean Valid) {
        this.Valid = Valid;
    }

    public byte getDoorNo() {
        return DoorNo;
    }

    public void setDoorNo(byte DoorNo) {
        this.DoorNo = DoorNo;
    }

    public byte getInOut() {
        return InOut;
    }

    public void setInOut(byte InOut) {
        this.InOut = InOut;
    }

    public long getCardNo() {
        return CardNo;
    }

    public void setCardNo(long CardNo) {
        this.CardNo = CardNo;
    }

    public Calendar getSwipeTime() {
        return SwipeTime;
    }

    public void setSwipeTime(Calendar SwipeTime) {
        this.SwipeTime = SwipeTime;
    }

    public int getDoorContact() {
        return DoorContact;
    }

    public void setDoorContact(int DoorContact) {
        this.DoorContact = DoorContact;
    }

    public byte getExcptionNo() {
        return ExcptionNo;
    }

    public void setExcptionNo(byte ExcptionNo) {
        this.ExcptionNo = ExcptionNo;
    }

    public Calendar getCurTime() {
        return CurTime;
    }

    public void setCurTime(Calendar CurTime) {
        this.CurTime = CurTime;
    }

}
