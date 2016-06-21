package com.yunguo.ygmjconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.PhoneValidator;
import com.throrinstudio.android.common.libs.validator.validator.RegExpValidator;
import com.yunguo.ygmjconfig.Adapter.AddressAdapter;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.LocationApplication.LocationApplication;
import com.yunguo.ygmjconfig.Util.SwitchData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

public class HouseAddActivity extends Activity implements OnClickListener{
	private EditText stdAddr;
	private Button subBtn;
	private ImageView searchloction;
	private LocationClient locationClient;
	private Form form;
	private EditText edit_ex1,houseNum,edit_ex3,edit_ex4,edit_ex5,edit_ex6,edit_ex7,edit_ex8,edit_ex9,edit_ex10,edit_ex11,edit_ex12;
	private Spinner houseSpinner;
	private AddressAdapter addressAdapter1,addressAdapter2;
	private EditText Gnote;
	private RadioGroup myRadioGroupSex;
	private RadioButton myRadioButtonSex1,myRadioButtonSex2;
	private String housevalue,lockStatus;
	private String sexStr="1";
	private LocationClient mLocationClient;//定位SDK的核心类

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_houseadd);
		
		 initView();
		InitLocation();//定位初始化
		validateform();
	}
	
	public void validateform(){
		form = new Form();
		
		/**
		 * 非空验证
		 */
		NotEmptyValidator notNull= new NotEmptyValidator(this);
		/**
		 * 身份证验证
		 */
		RegExpValidator IdCarIs = new RegExpValidator(this);
		IdCarIs.setPattern("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
		Validate telValidate = new Validate(edit_ex1);
		telValidate.addValidator(notNull);//非空验证
		Validate telValidate2 = new Validate(houseNum);
		telValidate2.addValidator(notNull);//非空验证
		Validate telValidate3 = new Validate(edit_ex3);
		telValidate3.addValidator(notNull);//非空验证
		Validate telValidate4 = new Validate(edit_ex4);
		telValidate4.addValidator(notNull);//非空验证
		Validate telValidate5 = new Validate(edit_ex5);
		telValidate5.addValidator(notNull);//非空验证
		Validate telValidate6 = new Validate(edit_ex6);
		telValidate6.addValidator(notNull);//非空验证
		Validate telValidate7 = new Validate(edit_ex7);
		telValidate7.addValidator(notNull);//非空验证
		Validate telValidate8 = new Validate(edit_ex8);
		telValidate8.addValidator(notNull);//非空验证
		Validate telValidate9 = new Validate(edit_ex9);
		telValidate9.addValidator(notNull);//非空验证
		telValidate9.addValidator(IdCarIs);
		Validate telValidate10 = new Validate(edit_ex10);
		telValidate10.addValidator(new PhoneValidator(this));// 手机号格式验证
		telValidate10.addValidator(notNull);// 手机号格式验证
		Validate telValidate11 = new Validate(edit_ex11);
		telValidate11.addValidator(notNull);//非空验证
		Validate telValidate12 = new Validate(edit_ex12);
		telValidate12.addValidator(notNull);//非空验证
		
		
		form.addValidates(telValidate);
		form.addValidates(telValidate2);
		form.addValidates(telValidate3);
		form.addValidates(telValidate4);
		form.addValidates(telValidate5);
		form.addValidates(telValidate6);
		form.addValidates(telValidate7);
		form.addValidates(telValidate8);
		form.addValidates(telValidate9);
		form.addValidates(telValidate10);
		form.addValidates(telValidate11);   
		form.addValidates(telValidate12);
	}
				

	@SuppressLint("CutPasteId")
	public void initView() {
		
		 Gnote = (EditText) findViewById(R.id.gps);
		 houseNum = (EditText) findViewById(R.id.houseNum);
		 edit_ex1 = (EditText)findViewById(R.id.houseName);
		 
		 
		 houseSpinner = (Spinner)findViewById(R.id.houseSpinner);
		 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		 Map<String,String> map = new HashMap<String, String>(); 
		 map.put("Name", "居住");
		 Map<String,String> map2 = new HashMap<String, String>(); 
		 map2.put("Name", "办公");
		 list.add(map);
		 list.add(map2);
		 addressAdapter1 = new AddressAdapter(list, this);
		 houseSpinner.setAdapter(addressAdapter1);
		 
		 //LockStatusSpinner = (Spinner) findViewById(R.id.LockStatusSpinner);
//		 List<Map<String,String>> list2 = new ArrayList<Map<String,String>>();
//		 Map<String,String> map3 = new HashMap<String, String>(); 
//		 map3.put("Name", "未初始化");
//		 Map<String,String> map4 = new HashMap<String, String>(); 
//		 map4.put("Name", "正常");
//		 Map<String,String> map5 = new HashMap<String, String>(); 
//		 map5.put("Name", "维护");
//		 list2.add(map3);
//		 list2.add(map4);
//		 list2.add(map5);
//		 addressAdapter2 = new AddressAdapter(list2, this);
		 //LockStatusSpinner.setAdapter(addressAdapter2);
		 
		 
		 edit_ex3 = (EditText)findViewById(R.id.roomNum);
		 edit_ex4 = (EditText)findViewById(R.id.floor);
		 
		 myRadioGroupSex = (RadioGroup) findViewById(R.id.myRadioGroupSex);
		 myRadioButtonSex1 = (RadioButton) findViewById(R.id.myRadioButtonSex1);
		 myRadioButtonSex2 = (RadioButton) findViewById(R.id.myRadioButtonSex2);
		 
		 edit_ex5 = (EditText)findViewById(R.id.stdAddr);
		 edit_ex6 = (EditText)findViewById(R.id.houseAddr);
		 edit_ex7 = (EditText)findViewById(R.id.gps);
		 edit_ex8 = (EditText)findViewById(R.id.ownerName);
		 edit_ex9 = (EditText)findViewById(R.id.ownerIdCardNo);
		 edit_ex10 = (EditText)findViewById(R.id.ownerTel);
		 edit_ex11= (EditText)findViewById(R.id.ownerIdCardAddr);
		 edit_ex12= (EditText)findViewById(R.id.ownerIdCardAuth);
		
		 stdAddr = (EditText) findViewById(R.id.stdAddr);
		 subBtn = (Button) findViewById(R.id.subBtn);
		 searchloction = (ImageView) findViewById(R.id.searchloction);
		 
		 houseSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				 @SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter1.getItem(arg2);
				housevalue = map.get("Name");
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		 
		/* LockStatusSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				Map<String,String> map = (Map<String, String>) addressAdapter2.getItem(arg2);
				lockStatus = map.get("Name");
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});*/
		 
		 myRadioGroupSex.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					if(checkedId == myRadioButtonSex1.getId()){
						sexStr = "1";
					}else{
						sexStr = "2";
					}
				}
			});
		 
		 stdAddr.setOnClickListener(this);
		 subBtn.setOnClickListener(this);
		 searchloction.setOnClickListener(this);
		 
	}



	public void SaveUserDataOnClick(){  
		boolean isOk = form.validate();
		if(isOk){
			stuUserData();
		}else{
			Toast.makeText(getApplicationContext(), "输入有误",
					Toast.LENGTH_SHORT).show();
		}
	}

	public void stuUserData(){
		
		 Map<String,String> dataMap = new HashMap<String, String>();
		 dataMap.put("HouseNum",  houseNum.getText().toString());
		 dataMap.put("houseName",  edit_ex1.getText().toString());
		 dataMap.put("houseUseage",SwitchData.Switchposthousestr(housevalue));
		 dataMap.put("roomNum",edit_ex3.getText().toString());
		 dataMap.put("floor", edit_ex4.getText().toString());
		 dataMap.put("stdAddr",edit_ex5.getText().toString());
		 dataMap.put("AreaId",InmarsatSerialNumber.getInstance().getAddhouseaddressId());
		 dataMap.put("houseAddr",edit_ex6.getText().toString());
		 dataMap.put("gps",edit_ex7.getText().toString());
		 dataMap.put("LockStatus","1");
		 dataMap.put("ownerName",edit_ex8.getText().toString());
		 dataMap.put("Sex",sexStr);
		 dataMap.put("ownerIdCardNo",edit_ex9.getText().toString());
		 dataMap.put("ownerTel",edit_ex10.getText().toString());
		 dataMap.put("ownerIdCardAddr",edit_ex11.getText().toString());
		 dataMap.put("ownerIdCardAuth",edit_ex12.getText().toString());
		 
		 
		 InmarsatSerialNumber.getInstance().setMap(dataMap);//临时保存数据
		 InmarsatSerialNumber.getInstance().setHouseSn(null);//清空临时数据
		 Intent intent = new Intent(this,DevH3Activity.class);
		 intent.putExtra("values","1");
		 startActivity(intent);
	}



	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.stdAddr:
			Intent intent3 = new Intent(HouseAddActivity.this, AdressDlogActivity.class);
			startActivityForResult(intent3, 0);
			break;
		case R.id.subBtn:
			SaveUserDataOnClick();
			break;
		case R.id.searchloction:
			edit_ex6.setText("获取中……");
            if (locationClient == null) {
				return;
			}else{
				locationClient.start();
			}
			break;
		}
	}
	
	/**
	 * 定位初始化
	 */
	private void InitLocation(){
		mLocationClient = ((LocationApplication)getApplication()).mLocationClient;
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(LocationMode.Hight_Accuracy);//设置高精度定位定位模式
		option.setCoorType("bd09ll");//设置百度经纬度坐标系格式
		option.setIsNeedAddress(true);//反编译获得具体位置，只有网络定位才可以
		mLocationClient.setLocOption(option);
		((LocationApplication)getApplication()).mLocationGnote = Gnote;
		((LocationApplication)getApplication()).mLocationAddress = edit_ex6;
		mLocationClient.start();
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			Bundle b = data.getExtras();
			edit_ex5.setText(b.getString("address"));
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (locationClient != null && locationClient.isStarted()) {
			locationClient.stop();
			locationClient = null;
		}
	}
}
