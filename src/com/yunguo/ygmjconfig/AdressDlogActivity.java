package com.yunguo.ygmjconfig;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunguo.ygmjconfig.Adapter.AddressAdapter;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.HtmlUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class AdressDlogActivity extends Activity{
	
	private Spinner spin_province1,spin_province,spin_city,spin_county,spin_county2,spin_county3;
	private List<Map<String,String>> mHouseaddress = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> mHouseaddress1 = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> mHouseaddress2 = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> mHouseaddress3 = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> mHouseaddress4 = new ArrayList<Map<String,String>>();
	private List<Map<String,String>> mHouseaddress5 = new ArrayList<Map<String,String>>();
	
	private List<Map<String,String>> tempAddress = new ArrayList<Map<String,String>>();
	
	private Map<String,String> SpinnerValue = new HashMap<String, String>();
	
	private AddressAdapter addressAdapter;
	private AddressAdapter addressAdapter1;
	private AddressAdapter addressAdapter2;
	private AddressAdapter addressAdapter3;
	private AddressAdapter addressAdapter4;
	private AddressAdapter addressAdapter5;
	
	private String addressId2;
	private String codestr;
	private StringBuffer addreestr = new StringBuffer(256);
	private TextView addresstext;
	
	private View view;
	
	private static int tmp;
	
	private String str1 = "";
	private String str2 = "";
	private String str3 = "";
	private String str4 = "";
	private String str5 = "";
	private String str6 = "";
	
	private Button btn_confirm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adress);
		SpinnerValue.put("Name", "请选择");
		mHouseaddress1 = InmarsatSerialNumber.getInstance().getUserAddressId();
		initView();
		setAdapter();
		setOnClick();
	}
	
	public void initView() {
		addresstext = (TextView) findViewById(R.id.addresstext);
		
		spin_province1 = (Spinner) findViewById(R.id.spin_province1);
		spin_province = (Spinner) findViewById(R.id.spin_province);
		spin_city = (Spinner) findViewById(R.id.spin_city);
		spin_county = (Spinner) findViewById(R.id.spin_county);
		spin_county2 = (Spinner) findViewById(R.id.spin_county2);
		spin_county3 = (Spinner) findViewById(R.id.spin_county3);
		
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
	}
	
	public void setAdapter(){
		addressAdapter1 = new AddressAdapter(mHouseaddress1, this);
		addressAdapter = new AddressAdapter(mHouseaddress, this);
		addressAdapter2 = new AddressAdapter(mHouseaddress2, this);
		addressAdapter3 = new AddressAdapter(mHouseaddress3, this);
		addressAdapter4 = new AddressAdapter(mHouseaddress4, this);
		addressAdapter5 = new AddressAdapter(mHouseaddress5, this);
		
		spin_province1.setAdapter(addressAdapter1);
		spin_province.setAdapter(addressAdapter);
		spin_city.setAdapter(addressAdapter2);
		spin_county.setAdapter(addressAdapter3);
		spin_county2.setAdapter(addressAdapter4);
		spin_county3.setAdapter(addressAdapter5);
	}

	public void setOnClick(){
		spin_province1.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter1.getItem(arg2);
				String selecdName =  map.get("Name");
				if(!selecdName.equals("请选择")){
					spin_city.setVisibility(View.GONE);
					spin_county.setVisibility(View.GONE);
					spin_county2.setVisibility(View.GONE);
					spin_county3.setVisibility(View.GONE);
					addressId2 = map.get("Id");
					str1 = selecdName;
					addresstext.setText(str1+str2);
					tmp = 0;
					new Thread(mThreadLoadApps).start();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spin_province.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter.getItem(arg2);
				String selecdName =  map.get("Name");
				if(!selecdName.equals("请选择")){
					spin_county.setVisibility(View.GONE);
					spin_county2.setVisibility(View.GONE);
					spin_county3.setVisibility(View.GONE);
					addressId2 = map.get("Id");
					str2 = selecdName;
					addresstext.setText(str1+str2);
					tmp = 1;
					new Thread(mThreadLoadApps).start();
					}
				}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spin_city.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter2.getItem(arg2);
				String selecdName =  map.get("Name");
				spin_county2.setVisibility(View.GONE);
				spin_county3.setVisibility(View.GONE);
				if(!selecdName.equals("请选择")){
					addressId2 = map.get("Id");
					str3 = selecdName;
					addresstext.setText(str1+str2+str3);
					tmp = 2;
					new Thread(mThreadLoadApps).start();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spin_county.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter3.getItem(arg2);
				String selecdName =  map.get("Name");
				if(!selecdName.equals("请选择")){
					spin_county3.setVisibility(View.GONE);
					addressId2 = map.get("Id");
					str4 = selecdName;
					addresstext.setText(str1+str2+str3+str4);
					tmp = 3;
					new Thread(mThreadLoadApps).start();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spin_county2.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter4.getItem(arg2);
				String selecdName =  map.get("Name");
				if(!selecdName.equals("请选择")){
					addressId2 = map.get("Id");
					str5 = selecdName;
					addresstext.setText(str1+str2+str3+str4+str5);
					tmp = 4;
					new Thread(mThreadLoadApps).start();
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		spin_county3.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter5.getItem(arg2);
				String selecdName =  map.get("Name");
				if(!selecdName.equals("请选择")){
					addressId2 = map.get("Id");
					str6 = selecdName;
					addresstext.setText(str1+str2+str3+str4+str5+str6);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		btn_confirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showSelectedResult();
			}
		});
	}
	
	
	/**
	 * handler刷新数据
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler1 = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mHouseaddress.clear();
				mHouseaddress.addAll(tempAddress);
				spin_province.setVisibility(View.VISIBLE);
				addressAdapter.notifyDataSetChanged();
				spin_province.setAdapter(addressAdapter);
				Toast.makeText(getApplicationContext(), codestr, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				mHouseaddress2.clear();
				mHouseaddress2.addAll(tempAddress);
				spin_city.setVisibility(View.VISIBLE);
				addressAdapter2.notifyDataSetChanged();
				spin_city.setAdapter(addressAdapter2);
				break;
			case 2:
				mHouseaddress3.clear();
				mHouseaddress3.addAll(tempAddress);
				spin_county.setVisibility(View.VISIBLE);
				addressAdapter3.notifyDataSetChanged();
				spin_county.setAdapter(addressAdapter3);
				break;
			case 3:
				mHouseaddress4.clear();
				mHouseaddress4.addAll(tempAddress);
				spin_county2.setVisibility(View.VISIBLE);
				addressAdapter4.notifyDataSetChanged();
				spin_county2.setAdapter(addressAdapter4);
				break;
			case 4:
				mHouseaddress5.clear();
				mHouseaddress5.addAll(tempAddress);
				spin_county3.setVisibility(View.VISIBLE);
				addressAdapter5.notifyDataSetChanged();
				spin_county3.setAdapter(addressAdapter5);
				break;
			}
		}
	};
	
	
	/**
	 * 请求网络数据
	 */
	private Thread mThreadLoadApps = new Thread(){
		@SuppressWarnings("unchecked")
		@Override
		public void run() {
			Map<String,String> map = new HashMap<String, String>();
			map.put("AreaID", addressId2);
			JSONObject jsonObject = null;
			try {
				jsonObject = new JSONObject(map.toString());
			} catch (JSONException e) {
				e.printStackTrace();
			}
			String ret = HtmlUtil.PostStringToUrl("http://120.25.65.125:8118/Client1/GetAreaListByid",jsonObject.toString());
			
			tempAddress.clear();
			tempAddress.add(SpinnerValue);
			tempAddress.addAll(getMessgeList(ret));
			handler1.sendEmptyMessage(tmp);
		}
	};
	
	public List getMessgeList(String str){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			JSONObject jsonObject2 =new JSONObject(str);
			codestr = (String) jsonObject2.get("message");
			JSONArray jsonArray = jsonObject2.getJSONArray("areas"); 
			 for (int i=0;i<jsonArray.length();i++){
				  Map<String,String> map = new HashMap<String, String>();
		          JSONObject jsonObjectSon= (JSONObject)jsonArray.opt(i);
		          map.put("Id", jsonObjectSon.getString("Id"));
		          map.put("Name", jsonObjectSon.getString("Name"));
		          map.put("Address", jsonObjectSon.getString("Pid"));
		          list.add(map);
		          }
			}catch (JSONException e) {
				e.printStackTrace();
			}
		return list;
	}
	private void showSelectedResult(){
		Toast.makeText(AdressDlogActivity.this, "当前选中:"+str1+" "+str2+" "+str3+" "+str4+" "+str5+" "+str6, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent();
		String address = str1+" "+str2+" "+str3+" "+str4+" "+str5+" "+str6;
		intent.putExtra("address",address);
		intent.putExtra("addressId",addressId2);
		InmarsatSerialNumber.getInstance().setAddhouseaddressId(addressId2);
		setResult(100,intent);
		finish();
	}

}
