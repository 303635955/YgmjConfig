package com.yunguo.ygmjconfig.Entity;

public class AdressItemIndex {
	private int ProvinceItem;
	private int CityItem;
	private int DistrictItem;
	
	public static AdressItemIndex adressItemIndex;
	
	private AdressItemIndex(){
		
	}
	
	public static AdressItemIndex getAdressItemIndex(){
		if(adressItemIndex == null){
			adressItemIndex = new AdressItemIndex();
		}
		return adressItemIndex;
	}
	
	public int getProvinceItem() {
		return ProvinceItem;
	}
	public void setProvinceItem(int provinceItem) {
		ProvinceItem = provinceItem;
	}
	public int getCityItem() {
		return CityItem;
	}
	public void setCityItem(int cityItem) {
		CityItem = cityItem;
	}
	public int getDistrictItem() {
		return DistrictItem;
	}
	public void setDistrictItem(int districtItem) {
		DistrictItem = districtItem;
	}
}
