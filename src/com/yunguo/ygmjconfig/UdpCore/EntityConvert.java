/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.yunguo.ygmjconfig.UdpCore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jp
 */
class EntityConvert {
    
    public static Map<String,String> RecordToMap(SwipeRecordInfo record){
        Map<String,String> map = null;
        if(record != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map = new HashMap<String,String>();
            map.put("Index", String.valueOf(record.getIndex()));
            map.put("Type", String.valueOf(record.getType()));
            map.put("Valid", record.isValid() == true ? "1" : "0");
            map.put("DoorNo", String.valueOf(record.getDoorNo()));
            map.put("InOut", String.valueOf(record.getInOut()));
            map.put("CardNo", String.valueOf(record.getCardNo()));
            map.put("SwipeTime", sdf.format(record.getSwipeTime().getTime()));
        }
        return map;
    }
    
    public static PrivilegeInfo MapToPrivilege(Map<String,String> map){
        PrivilegeInfo privilege = null;
        if(map != null){
            Calendar Stime = Calendar.getInstance();
            Calendar Etime = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            privilege = new PrivilegeInfo();
            privilege.setCardNo(Integer.valueOf(map.get("CardNo")));
            try {
                Stime.setTime(sdf.parse(map.get("StartDate")));
            } catch (ParseException ex) {
            }
            privilege.setStartDate(Stime);
            
            try {
                Etime.setTime(sdf.parse(map.get("EndDate")));
            } catch (ParseException ex) {
            }
            privilege.setEndDate(Etime);
            if(map.get("DoorNoValid") != null ){
                privilege.setDoorNoValid(Integer.valueOf(map.get("DoorNoValid")));             
            }
            if(map.get("PassWord") != null){
                privilege.setPassWord(Integer.valueOf(map.get("PassWord")));
            }
        }
        return privilege;
    }
    
    public static Map PrivilegeToMap(PrivilegeInfo privilege){
        Map<String,String> map = null;
        if(privilege != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map = new HashMap<String,String>();
            map.put("CardNo", String.valueOf(privilege.getCardNo()));
            map.put("StartDate", sdf.format(privilege.getStartDate().getTime()));
            map.put("EndDate", sdf.format(privilege.getEndDate().getTime()));
            map.put("DoorNoValid", String.valueOf(privilege.getDoorNoValid()));
            map.put("PassWord", String.valueOf(privilege.getPassWord()));
        }
        return map;
    }
    
    public static Map DoorCtrlInfoToMap(DoorCtrlInfo door){
        Map<String,String> map = null;
        if(door != null){
            map = new HashMap<String,String>();
            map.put("DoorNo",String.valueOf(door.getDoorNo()));//序列号
            map.put("CtrlType", String.valueOf(door.getCtrlType()));//控制类型
            map.put("OpenDelay", String.valueOf(door.getOpenDelay()));//开门延时
            map.put("PrivilegeType", String.valueOf(door.getPrivilegeType()));//权限判断规则
            map.put("VoiceAlarm", String.valueOf(door.getVoiceAlarm()));//非法卡语音报警
            map.put("DoorEnable", String.valueOf(door.getDoorEnable()));//出门按钮功能
            map.put("Beeper", String.valueOf(door.getBeeper()));//警笛功能
            map.put("CloseToRemindDelay", String.valueOf(door.getCloseToRemindDelay()));//关闭提醒延时（保持提醒控制时长）
            map.put("UnlockAlarm", String.valueOf(door.getUnlockAlarm()));//技术开锁报警选项（保持提醒控制时长）
            map.put("IsSneak", String.valueOf(door.getIsSneak()));//防潜入选项
            map.put("IsTag", String.valueOf(door.getIsTag()));//防尾随报警选项
        }
        return map;
    }
   
    public static DoorCtrlInfo MapToDoorCtrlInfo(Map<String,String> map){
        DoorCtrlInfo door = null;
        if(map != null ){
            door = new DoorCtrlInfo();
            door.setDoorNo(Byte.valueOf(map.get("DoorNo")));
            door.setCtrlType(Byte.valueOf(map.get("CtrlType")));
            door.setOpenDelay(Byte.valueOf(map.get("OpenDelay")));
            door.setPrivilegeType(Byte.valueOf(map.get("PrivilegeType")));
            door.setVoiceAlarm(Byte.valueOf(map.get("VoiceAlarm")));
            door.setDoorEnable(Byte.valueOf(map.get("DoorEnable")));
            door.setBeeper(Byte.valueOf(map.get("Beeper")));
            door.setCloseToRemindDelay(Integer.valueOf(map.get("CloseToRemindDelay")));
            door.setUnlockAlarm(Byte.valueOf(map.get("UnlockAlarm")));
            door.setIsSneak(Byte.valueOf(map.get("IsSneak")));
            door.setIsTag(Byte.valueOf(map.get("IsTag"))); 
        }
        return door;
    }
    
    public static Map StatusToMap(StatusInfo status){
        Map<String,String> map = null;
        if(status != null){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            map = new HashMap<String,String>();
            map.put("Index", String.valueOf(status.getIndex()));
            map.put("Type", String.valueOf(status.getType()));
            map.put("Valid", status.isValid() == true ? "1": "0");
            map.put("DoorNo", String.valueOf(status.getDoorNo()));
            map.put("InOut", String.valueOf(status.getInOut()));
            map.put("CardNo", String.valueOf(status.getCardNo()));
            map.put("SwipeTime", sdf.format(status.getSwipeTime().getTime()));
            map.put("DoorContact", String.valueOf(status.getDoorContact()));
            map.put("ExcptionNo", String.valueOf(status.getExcptionNo()));
            map.put("CurTime", sdf.format(status.getCurTime().getTime()));
        }
        return map;
    }
    
}
