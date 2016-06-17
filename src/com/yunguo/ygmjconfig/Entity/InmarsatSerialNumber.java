package com.yunguo.ygmjconfig.Entity;

import java.util.List;
import java.util.Map;

public class InmarsatSerialNumber {
	
	
	public static InmarsatSerialNumber inmarsatSerialNumber;
	
	private Map<String,String> H3_SN;//H3序列号
	private String Dev_SN  = "0";//门禁序列号
	private String ODV_SN;//视频rps地址
	private Map<String,String> map;
	private String De_SN = "2";//读头协议
	private List<Map<String,String>> houseSn;//房屋门禁号
	private List<Map<String,String>> UserAddressId;//用户管辖区域
	private String addhouseaddressId;//添加房屋时的区域ID
	private String addressId;//加载数据是的区域ID
	
	private InmarsatSerialNumber(){
		
	}
	
	public static InmarsatSerialNumber getInstance() {
		  
        if (inmarsatSerialNumber == null) {
  
        	inmarsatSerialNumber = new InmarsatSerialNumber();
  
        }
  
        return inmarsatSerialNumber;
		  
    }
	
	public String getAddhouseaddressId() {
		return addhouseaddressId;
	}

	public void setAddhouseaddressId(String addhouseaddressId) {
		this.addhouseaddressId = addhouseaddressId;
	}

	public String getAddressId() {
		return addressId;
	}

	public void setAddressId(String addressId) {
		this.addressId = addressId;
	}
	public List<Map<String, String>> getUserAddressId() {
		return UserAddressId;
	}

	public void setUserAddressId(List<Map<String, String>> userAddressId) {
		UserAddressId = userAddressId;
	}
	public List<Map<String,String>> getHouseSn() {
		return houseSn;
	}

	public void setHouseSn(List<Map<String,String>> houseSn) {
		this.houseSn = houseSn;
	}

	public String getDe_SN() {
		return De_SN;
	}

	public void setDe_SN(String de_SN) {
		De_SN = de_SN;
	}
	
	public String getDev_SN() {
		return Dev_SN;
	}

	public void setDev_SN(String dev_SN) {
		Dev_SN = dev_SN;
	}
	public Map<String, String> getH3_SN() {
		return H3_SN;
	}

	public void setH3_SN(Map<String, String> h3_SN) {
		H3_SN = h3_SN;
	}


	public String getODV_SN() {
		return ODV_SN;
	}

	public void setODV_SN(String oDV_SN) {
		ODV_SN = oDV_SN;
	}
	
	public Map<String, String> getMap() {
		return map;
	}

	public void setMap(Map<String, String> map) {
		this.map = map;
	}
}
