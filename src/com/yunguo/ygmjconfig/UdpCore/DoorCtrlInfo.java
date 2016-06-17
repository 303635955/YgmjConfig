/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

/**
 *
 * @author Administrator
 */
class DoorCtrlInfo {
    /**
     * 门号
     */
    private byte DoorNo;
    /**
     * 控制类型
     * 1―常开[不受刷卡控制]; 
     * 2—常闭[不受刷卡控制]; 
     * 3—在线控制(缺省值:3)
     */
    private byte CtrlType;
    /**
     * 开门延时(秒) (缺省: 3秒)
     * 保持门打开的时长
     */
    private byte OpenDelay = 3;
    /**
     * 权限判断规则
     * 1：黑名单有效；2白名单有效(缺省)
     */
    private byte PrivilegeType = 2;
    /**
     * 非法卡语音报警
     * 1：启用, 0：不启用
     */
    private byte VoiceAlarm;
    /**
     * 出门按钮功能
     * 1：启用, 0：不启用
     */
    private byte DoorEnable;
    /**
     * 警笛功能
     * 1：启用, 0：不启用
     */
    private byte Beeper;
    /**
     * 关闭提醒延时（保持提醒控制时长）
     * 1：启用, 0：不启用
     */
    private int CloseToRemindDelay;
    /**
     * 技术开锁报警选项（保持提醒控制时长）
     * 1：启用, 0：不启用
     */
    private byte UnlockAlarm;
    /**
     * 防潜入选项
     * 1：启用, 0：不启用
     */
    private byte IsSneak;
    /**
     * 防尾随报警选项
     * 1：启用, 0：不启用
     */
    private byte IsTag;

    /**
     *
     * @return
     */
    public byte getDoorNo() {
        return DoorNo;
    }

    /**
     *
     * @param DoorNo
     */
    public void setDoorNo(byte DoorNo) {
        this.DoorNo = DoorNo;
    }

    /**
     *
     * @return
     */
    public byte getCtrlType() {
        return CtrlType;
    }

    /**
     *
     * @param CtrlType
     */
    public void setCtrlType(byte CtrlType) {
        this.CtrlType = CtrlType;
    }

    /**
     *
     * @return
     */
    public byte getOpenDelay() {
        return OpenDelay;
    }

    /**
     *
     * @param OpenDelay
     */
    public void setOpenDelay(byte OpenDelay) {
        this.OpenDelay = OpenDelay;
    }

    /**
     *
     * @return
     */
    public byte getPrivilegeType() {
        return PrivilegeType;
    }

    /**
     *
     * @param PrivilegeType
     */
    public void setPrivilegeType(byte PrivilegeType) {
        this.PrivilegeType = PrivilegeType;
    }

    /**
     *
     * @return
     */
    public byte getVoiceAlarm() {
        return VoiceAlarm;
    }

    /**
     *
     * @param VoiceAlarm
     */
    public void setVoiceAlarm(byte VoiceAlarm) {
        this.VoiceAlarm = VoiceAlarm;
    }

    /**
     *
     * @return
     */
    public byte getDoorEnable() {
        return DoorEnable;
    }

    /**
     *
     * @param DoorEnable
     */
    public void setDoorEnable(byte DoorEnable) {
        this.DoorEnable = DoorEnable;
    }

    /**
     *
     * @return
     */
    public byte getBeeper() {
        return Beeper;
    }

    /**
     *
     * @param Beeper
     */
    public void setBeeper(byte Beeper) {
        this.Beeper = Beeper;
    }

    /**
     *
     * @return
     */
    public int getCloseToRemindDelay() {
        return CloseToRemindDelay;
    }

    /**
     *
     * @param CloseToRemindDelay
     */
    public void setCloseToRemindDelay(int CloseToRemindDelay) {
        this.CloseToRemindDelay = CloseToRemindDelay;
    }

    /**
     *
     * @return
     */
    public byte getUnlockAlarm() {
        return UnlockAlarm;
    }

    /**
     *
     * @param UnlockAlarm
     */
    public void setUnlockAlarm(byte UnlockAlarm) {
        this.UnlockAlarm = UnlockAlarm;
    }

    /**
     *
     * @return
     */
    public byte getIsSneak() {
        return IsSneak;
    }

    /**
     *
     * @param IsSneak
     */
    public void setIsSneak(byte IsSneak) {
        this.IsSneak = IsSneak;
    }

    /**
     *
     * @return
     */
    public byte getIsTag() {
        return IsTag;
    }

    /**
     *
     * @param IsTag
     */
    public void setIsTag(byte IsTag) {
        this.IsTag = IsTag;
    }
}
