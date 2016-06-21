package com.yunguo.ygmjconfig;



import java.util.HashMap;
import java.util.Map;

import com.yunguo.ygmjconfig.Controller.YGController;
import com.yunguo.ygmjconfig.Entity.Controller;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.SerializableMap;
import com.yunguo.ygmjconfig.Util.SwitchData;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DevControllerActivitySub extends Activity {

	
	private EditText editPsw8;
	private EditText editPsw9;
	private EditText editPsw91;
	private EditText edittimedelay;
	private EditText controlSettingType;
	private EditText opnedoorSettingType;
	private EditText Serverurl;
	String ServerurlStr ;
	
	private Spinner mSpinner;
	private Spinner cSpinner;
	private Spinner oSpinner;
	private Spinner tSpinner;
//	private Spinner rSpinner;
	private Spinner pSpinner;
	
	
	private String dhcp;
	private int DHCP;//ip获取方式
	private String ip = "";//IP地址
	private String Gateway = "";//网关
	private String Mask = "";//子网掩码
	
	private String controlType;
	private String opnedoorTypestr;
	
	/**
	 * 进度框
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * listItem的索引
	 */
	private int postion;
	
	
	/**
	 * data
	 */
	private Controller postmap;
	private Map<String,String> setmap = new HashMap<String, String>();
	
	
	private final int NET_Port = 50000;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_devcontrollersub);
		
		/**
		 * 获取数据
		 */
		
		Intent inten = getIntent();
		SerializableMap serializableMap = SerializableMap.getSerializableMap();
		postmap = serializableMap.getMap();
		
		/**
		 * 初始化控件
		 */
		findView();
		
		/**
		 * 配置Spinner
		 */
		initSpinner();
		
		/**
		 * 填充数据
		 */

		setData();
		
	}
	
	/**
	 * 查找控件
	 */
	private void findView() {
		 mSpinner = (Spinner) findViewById(R.id.ipSettingTypesub);
		 cSpinner = (Spinner) findViewById(R.id.controlSettingType);
		 oSpinner = (Spinner) findViewById(R.id.opnedoorSettingType);
		 tSpinner = (Spinner) findViewById(R.id.ThediveSettingType);
		 //rSpinner = (Spinner) findViewById(R.id.readheadSettingType);
		 pSpinner = (Spinner) findViewById(R.id.readheadProtocolSettingType);
		
		
		editPsw8 = (EditText) findViewById(R.id.editPsw8);
		editPsw9 = (EditText) findViewById(R.id.editPsw9);
		editPsw91 = (EditText) findViewById(R.id.editPsw91);
		Serverurl = (EditText) findViewById(R.id.Serverurl);
		
		
		//设置EditText的显示方式为多行文本输入  
		Serverurl.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE); 
		//文本显示的位置在EditText的最上方  
		Serverurl.setGravity(Gravity.TOP);  
		//改变默认的单行模式  
		Serverurl.setSingleLine(false);  
		//水平滚动设置为False  
		Serverurl.setHorizontallyScrolling(false);
		edittimedelay = (EditText) findViewById(R.id.edittimedelay);
	}
	
	/**
	 * 配置Spinner
	 */
	
	public void initSpinner(){
		
		//设置Spinner值
		String[] mItems = new String[]{"手动","DHCP"};
		String[] cItems = new String[]{"在线","常开","常闭"};
		String[] oItems = new String[]{"开启","关闭"};
		//String[] rItems = new String[]{"三合一","普通"};
		String[] pItems = new String[]{"WG26","WG66","WG34"};
		
		// 设置SpinnerAdapter
		ArrayAdapter<String> m_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		ArrayAdapter<String> c_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, cItems);
		ArrayAdapter<String> o_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, oItems);
		//ArrayAdapter<String> r_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, rItems);
		ArrayAdapter<String> p_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, pItems);
		
		mSpinner.setAdapter(m_Adapter);
		cSpinner.setAdapter(c_Adapter);
		oSpinner.setAdapter(o_Adapter);
		tSpinner.setAdapter(o_Adapter);
		//rSpinner.setAdapter(r_Adapter);
		pSpinner.setAdapter(p_Adapter);
		
		//监听
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String sInfo=arg0.getItemAtPosition(arg2).toString();
				
				
				Toast.makeText(getApplicationContext(), sInfo, Toast.LENGTH_SHORT).show();
				if(sInfo.equals("手动")){
					/**
					 * 设置可点击
					 */
					editPsw8.setEnabled(true);
					editPsw9.setEnabled(true);
					editPsw91.setEnabled(true);
				}else{
					/**
					 * 设置不可点击
					 */
					editPsw8.setEnabled(false);
					editPsw9.setEnabled(false);
					editPsw91.setEnabled(false);
					
					/**
					 * 设置值为空
					 */
					editPsw8.setText("");
					editPsw9.setText("");
					editPsw91.setText("");
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		
		oSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String sInfo=arg0.getItemAtPosition(arg2).toString();
				opnedoorTypestr = sInfo;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				
			}
		});
		
		cSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				String sInfo=arg0.getItemAtPosition(arg2).toString();
				
				controlType = sInfo;
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});

	}
	
	
	
	/**
	 * 填充数据
	 */
	public void setData(){
		setSpinnerItemSelectedByValue(mSpinner,"手动");
		
		//if(postmap.getOpenDelay().toString().equals("手动")){
			editPsw8.setText(postmap.getmDevIP());
			editPsw9.setText(postmap.getGetway());
			editPsw91.setText(postmap.getMask());
			Serverurl.setText(postmap.getServerUrl());
		//}
		
		setSpinnerItemSelectedByValue(cSpinner,postmap.getCtrlType());
		
		edittimedelay.setText(postmap.getOpenDelay());
		
		setSpinnerItemSelectedByValue(oSpinner,postmap.getDoorEnable());
		setSpinnerItemSelectedByValue(tSpinner,postmap.getIsSneak());
		
		
	}
	
	
	/**
	 * 根据值, 设置spinner默认选中:
	 * @param spinner
	 * @param value
	 */
	public static void setSpinnerItemSelectedByValue(Spinner spinner,String value){
	    SpinnerAdapter apsAdapter= spinner.getAdapter(); //得到SpinnerAdapter对象
	    int k= apsAdapter.getCount();
	    for(int i=0;i<k;i++){
	        if(value.equals(apsAdapter.getItem(i).toString())){
	            spinner.setSelection(i,true);// 默认选中项
	            break;
	        }
	    }
	} 

	/**
	 * 提交数据
	 * @param view
	 */
	public void OnClickSure(View view) {
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在上传...", true);
		 
		 	dhcp = (String) mSpinner.getSelectedItem();
		  DHCP = SwitchData.SwitchDHCPint(dhcp);//ip获取方式
		  if(!dhcp.equals("DHCP")){
			  ip = editPsw8.getText()+"";//IP地址
			  Gateway = editPsw9.getText()+"";//网关
			  Mask = editPsw91.getText()+"";//子网掩码
		  }
		  
		 String DoorNO = postmap.getDoorNo();//门号
		 int ControlType = SwitchData.SwitchcontrolTypeInt(controlType);//控制方式
		 String openDoorTime = edittimedelay.getText().toString();//开门延时
		 String butIsOpenDoor = SwitchData.SwitchDoorEnableInt(opnedoorTypestr);//按钮开门
		 String TheDiveIs = SwitchData.SwitchIsSneakInt((String) tSpinner.getSelectedItem());//防潜入
		// String readheadType = (String) rSpinner.getSelectedItem();//读头类型
		 String readheadProtocolType = (String) pSpinner.getSelectedItem();//读头协议
		 InmarsatSerialNumber.getInstance().setDe_SN(readheadProtocolType);
		 setmap.put("DoorNo", DoorNO);
		 setmap.put("CtrlType", ControlType+"");
		 setmap.put("OpenDelay", openDoorTime+"");
		 setmap.put("DoorEnable", butIsOpenDoor+"");
		 setmap.put("IsSneak", TheDiveIs+"");
		 setmap.put("Beeper", postmap.getBeeper());
		 setmap.put("PrivilegeType", postmap.getPrivilegeType());
		 setmap.put("VoiceAlarm", postmap.getVoiceAlarm());
		 setmap.put("IsTag", postmap.getIsTag());
		 setmap.put("CloseToRemindDelay", postmap.getCloseToRemindDelay());
		 setmap.put("UnlockAlarm", postmap.getUnlockAlarm());
		 ServerurlStr = Serverurl.getText().toString(); 
		 System.out.println(setmap);
		/* setmap.put("TheDiveIs", readheadType);
		 setmap.put("TheDiveIs", readheadProtocolType);*/
		 new Thread(mThreadLoadApps).start();
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
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传成功", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 1:
				progressDialog.dismiss();
				Toast.makeText(getApplicationContext(), "上传失败", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	
	/**
	 * 上传数据
	 */
	private Thread mThreadLoadApps = new Thread(){
		@Override
		public void run() {
			new YGController().SetDevCtrlPlatform(ServerurlStr, postmap.getmDevSN(), postmap.getmDevIP());
			postmap.SetControllerIP(DHCP, ip, Mask, Gateway);
			postmap.setmDevIP(ip);
			Boolean tmp = postmap.SetDoorCtrlInfo(setmap);
			if(tmp){
				handler1.sendEmptyMessage(0);
			}else{
				handler1.sendEmptyMessage(1);
			}
		}
	};
	
	
	/**   
	 * 取消操作
	 */
	public void OnClickfinish(View view){
		finish();
	}
}
