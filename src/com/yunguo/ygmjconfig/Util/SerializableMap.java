package com.yunguo.ygmjconfig.Util;

import java.io.Serializable;
import java.util.Map;

import com.yunguo.ygmjconfig.Entity.Controller;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;

/**
 * 序列化map供Bundle传递map使用
 * Created  on 13-12-9.
 */
@SuppressWarnings("serial")
public class SerializableMap{
	
	public static SerializableMap serializableMap;
    private Controller map;
    private Map<Object,Object> H3Map;
 
    
   
	private SerializableMap(){
    	
    }
    public static SerializableMap getSerializableMap() {
		  
        if (serializableMap == null) {
  
        	serializableMap = new SerializableMap();
  
        }
  
        return serializableMap;
		  
    }
    
    public void setH3Map(Map<Object, Object> h3Map) {
		H3Map = h3Map;
	}
    public Map<Object,Object> getH3Map() {
		return H3Map;
	}
    
    public Controller getMap() {
        return map;
    }
 
    public void setMap(Controller map) {
        this.map = map;
    }
}
