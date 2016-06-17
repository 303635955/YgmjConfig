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
class PrivilegeInfo {
    private Calendar StartDate;                          //起始日期(年月日) 20100101
    private Calendar EndDate;                            //截止日期(年月日) 20291231
    private long CardNo;                              //卡号
    private int DoorNoValid;                            //门权限标识(Bit0-Bit31表示1-31号门的权限) 1:有权限, 0:无权限
    private int PassWord;                            //用户密码[启用了密码键盘才有效]

    public Calendar getStartDate() {
        return StartDate;
    }

    public void setStartDate(Calendar StartDate) {
        this.StartDate = StartDate;
    }

    public Calendar getEndDate() {
        return EndDate;
    }

    public void setEndDate(Calendar EndDate) {
        this.EndDate = EndDate;
    }

    public long getCardNo() {
        return CardNo;
    }

    public void setCardNo(long CardNo) {
        this.CardNo = CardNo;
    }

    public int getDoorNoValid() {
        return DoorNoValid;
    }

    public void setDoorNoValid(int DoorNoValid) {
        this.DoorNoValid = DoorNoValid;
    }

    public int getPassWord() {
        return PassWord;
    }

    public void setPassWord(int PassWord) {
        this.PassWord = PassWord;
    }

}
