/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.Entity;

import java.util.Calendar;

import com.yunguo.ygmjconfig.UdpCore.Protocol;


/**
 *
 * @author jp
 */
public class Controller extends Protocol{
    private String Mask;                    //子网掩码
    private String Getway;                  //网关
    private String MAC;                     //MAC地址
    private String Version;                  //版本号
    private Calendar Date;                      //版本时期
    private String DoorNo;						//门号
	private String CtrlType;					//控制类型
    private String OpenDelay;					//开门延时
    private String DoorEnable;					//出门按钮
    private String IsSneak;						//防潜入
    private String PrivilegeType;
	private String VoiceAlarm;
    private String Beeper;
    private String IsTag;
	private String CloseToRemindDelay;
    private String UnlockAlarm;
    private String  state = "在线";//状态
    private String ServerUrl;
    
    
    public String getServerUrl() {
		return ServerUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.ServerUrl = serverUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCloseToRemindDelay() {
  		return CloseToRemindDelay;
  	}

  	public void setCloseToRemindDelay(String closeToRemindDelay) {
  		CloseToRemindDelay = closeToRemindDelay;
  	}

  	public String getUnlockAlarm() {
  		return UnlockAlarm;
  	}

  	public void setUnlockAlarm(String unlockAlarm) {
  		UnlockAlarm = unlockAlarm;
  	}
    
    public String getPrivilegeType() {
		return PrivilegeType;
	}

	public void setPrivilegeType(String privilegeType) {
		PrivilegeType = privilegeType;
	}

	public String getVoiceAlarm() {
		return VoiceAlarm;
	}

	public void setVoiceAlarm(String voiceAlarm) {
		VoiceAlarm = voiceAlarm;
	}

	public String getBeeper() {
		return Beeper;
	}

	public void setBeeper(String beeper) {
		Beeper = beeper;
	}

	public String getIsTag() {
		return IsTag;
	}

	public void setIsTag(String isTag) {
		IsTag = isTag;
	}
    public Controller(int port, String ip, long sn) {
        super(port, ip, sn);
    }
    
    public Controller(){
    	
    }

    public String getMask() {
        return Mask;
    }

    public void setMask(String Mask) {
        this.Mask = Mask;
    }

    public String getGetway() {
        return Getway;
    }

    public void setGetway(String Getway) {
        this.Getway = Getway;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String Version) {
        this.Version = Version;
    }

    public Calendar getDate() {
        return Date;
    }

    public void setDate(Calendar Date) {
        this.Date = Date;
    }
    
    public String getDoorNo() {
		return DoorNo;
	}
    
    public void setDoorNo(String doorNo) {
		DoorNo = doorNo;
	}

	public String getCtrlType() {
		return CtrlType;
	}

	public void setCtrlType(String ctrlType) {
		CtrlType = ctrlType;
	}

	public String getOpenDelay() {
		return OpenDelay;
	}

	public void setOpenDelay(String openDelay) {
		OpenDelay = openDelay;
	}

	public String getDoorEnable() {
		return DoorEnable;
	}

	public void setDoorEnable(String doorEnable) {
		DoorEnable = doorEnable;
	}

	public String getIsSneak() {
		return IsSneak;
	}

	public void setIsSneak(String isSneak) {
		IsSneak = isSneak;
	}

}
