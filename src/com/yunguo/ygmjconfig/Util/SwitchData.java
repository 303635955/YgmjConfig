package com.yunguo.ygmjconfig.Util;


public class SwitchData {
	
	
	public static String SwitchCtrlType(String CtrlType){
		
		String CtrlTypeStr = "";
		switch (CtrlType) {
		case "1":
			CtrlTypeStr = "常开";
			break;
		case "2":
			CtrlTypeStr = "常闭";
			break;
		case "3":
			CtrlTypeStr = "在线";
			break;
		}
		return CtrlTypeStr;
	}
	
	
	public static String SwitchDoorEnable(String DoorEnable){
		String DoorEnablestr = "";
		
		switch (DoorEnable) {
		case "0":
			DoorEnablestr = "关闭";
			break;
		case "1":
			DoorEnablestr = "开启";
			break;
		}
		
		return DoorEnablestr;
	}
	
	public static String SwitchIsSneak(String IsSneak){
		
		String IsSneakStr = "";
		switch (IsSneak) {
		case "0":
			IsSneakStr = "关闭";
			break;
		case "1":
			IsSneakStr = "开启";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static int SwitchDHCPint(String dhcp){
		int temp = 0;
		switch (dhcp) {
		case "DHCP":
			temp = 1;
			break;
		case "手动":
			temp = 0;
			break;
		}
		return temp;
	}
	
	public static int SwitchcontrolTypeInt(String type){
		int temp = 0;
		switch (type) {
		case "在线":
			temp = 3;
			break;
		case "常开":
			temp = 1;
			break;
		case "常闭":
			temp = 2;
			break;

		}
		return temp;
		
	}
	
	
	public static String SwitchDoorEnableInt(String DoorEnable){
		String DoorEnablestr = "";
		
		switch (DoorEnable) {
		case "关闭":
			DoorEnablestr = "0";
			break;
		case "开启":
			DoorEnablestr = "1";
			break;
		}
		
		return DoorEnablestr;
	}
	
	public static String SwitchIsSneakInt(String IsSneak){
		
		String IsSneakStr = "";
		switch (IsSneak) {
		case "关闭":
			IsSneakStr = "0";
			break;
		case "开启":
			IsSneakStr = "1";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchpostint(String value){
		String IsSneakStr = "";
		switch (value) {
		case "0":
			IsSneakStr = "否";
			break;
		case "1":
			IsSneakStr = "是";
			break;
		case "2":
			IsSneakStr = "null";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchsaveint(String value){
		String IsSneakStr = "";
		switch (value) {
		case "0":
			IsSneakStr = "TF卡";
			break;
		case "1":
			IsSneakStr = "SATA";	
			break;
		case "2":
			IsSneakStr = "未设置";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchpoststr(String value){
		String IsSneakStr = "";
		switch (value) {
		case "否":
			IsSneakStr = "0";
			break;
		case "是":
			IsSneakStr = "1";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchsavestr(String value){
		String IsSneakStr = "";
		switch (value) {
		case "TF卡":
			IsSneakStr = "0";
			break;
		case "SATA":
			IsSneakStr = "1";
			break;
		
		}
		return IsSneakStr;
	}
	
	
	public static String Swithredstr(String value){
		String tmp = "1";
		switch (value) {
		case "普通":
			tmp = "1";
			break;
		case "WG26":
			tmp = "2";
			break;
		case "WG34":
			tmp = "3";
			break;
		}
		return tmp;
	}
	
	public static String Switchpoststr2(String value){
		String IsSneakStr = "";
		switch (value) {
		case "否":
			IsSneakStr = "false";
			break;
		case "是":
			IsSneakStr = "true";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchpostint2(String value){
		String IsSneakStr = "";
		switch (value) {
		case "false":
			IsSneakStr = "否";
			break;
		case "true":
			IsSneakStr = "是";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchposthousestr(String value){
		String IsSneakStr = "";
		switch (value) {
		case "居住":
			IsSneakStr = "1";
			break;
		case "办公":
			IsSneakStr = "2";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String Switchpostlockstr(String value){
		String IsSneakStr = "";
		switch (value) {
		case "未初始化":
			IsSneakStr = "1";
			break;
		case "正常":
			IsSneakStr = "2";
		case "维护":
			IsSneakStr = "3";
			break;
		
		}
		return IsSneakStr;
	}
	
	public static String SwichDoorType(String value){
		String IsDoorTypeStr = "DCTRL_TYPE_YG_NET";
		switch (value) {
		case "1":
			IsDoorTypeStr = "DCTRL_TYPE_YG_LOCKER";
			break;
		case "2":
			IsDoorTypeStr = "DCTRL_TYPE_YG_NET";
			break;
		case "3":
			IsDoorTypeStr = "DCTRL_TYPE_WG_NET";
			break;	
		}
		return IsDoorTypeStr;
	}
	
	public static int SwichDoorType2(String value){
		int IsDoorTypeStr = 2;
		switch (value) {
		case "DCTRL_TYPE_YG_LOCKER":
			IsDoorTypeStr = 1;
			break;
		case "DCTRL_TYPE_YG_NET":
			IsDoorTypeStr = 2;
			break;
		case "DCTRL_TYPE_WG_NET":
			IsDoorTypeStr = 3;
			break;	
		}
		return IsDoorTypeStr;
	}
}
