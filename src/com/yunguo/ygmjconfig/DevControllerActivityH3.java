package com.yunguo.ygmjconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.yunguo.ygmjconfig.Adapter.RtspListAdapter;
import com.yunguo.ygmjconfig.UdpCore.ConfigTransfer;
import com.yunguo.ygmjconfig.Util.DateTimePickDialogUtil;
import com.yunguo.ygmjconfig.Util.SerializableMap;
import com.yunguo.ygmjconfig.Util.SplitString;
import com.yunguo.ygmjconfig.Util.SwitchData;
import com.yunguo.ygmjconfig.VideoController.ConfigData;
import com.yunguo.ygmjconfig.VideoController.H3ConfigClient;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableRow;
import android.widget.Toast;

public class DevControllerActivityH3 extends Activity {
	
	private Form form;
	private EditText Ip,GetWay,MAC,Mask,editsn,videotime,VideoServer;
	private Spinner mSpinner;
	private Spinner mjtype;
	private Spinner VideoStorePath;
	private ProgressDialog progressDialog;
	private ListView rtsplist;
	private RtspListAdapter rtspListAdapter;
	private ImageButton addrtspbut;
	private ConfigData configData;
	
	private List<Map<Object,Object>> rtspmap = new ArrayList<Map<Object,Object>>();
	
	private Map<String,String> postmap = new HashMap<String,String>();
	private Map<Object,Object> getmap = new HashMap<Object,Object>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_devcontrollerh3);
		/**
		 * 初始化控件
		 */
		findView();
		/**
		 * 配置Spinner
		 */
		initlistSinner();
		/**
		 * 控件监听
		 */
		setOnClick();
		/**
		 * 填充数据
		 */
		setData();
		/**
		 * 初始化listview
		 */
		setAdapter();
		/**
		 * 校验数据
		 */
		InputValidate();
	}
	/**
	 * 查找控件
	 */
	private void findView() {
		 mSpinner = (Spinner) findViewById(R.id.ipSettingType);
		 //mjtype = (Spinner) findViewById(R.id.mjtype);
		 
		 Ip = (EditText) findViewById(R.id.Ip);
		 GetWay = (EditText) findViewById(R.id.GetWay);
		 MAC = (EditText) findViewById(R.id.mac);
		 Mask = (EditText) findViewById(R.id.Mask);
		 editsn = (EditText) findViewById(R.id.editsn);
		 videotime = (EditText) findViewById(R.id.videotime);
		 VideoServer = (EditText) findViewById(R.id.VideoServer);
		 
		 //设置EditText的显示方式为多行文本输入  
		 VideoServer.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE); 
		 //文本显示的位置在EditText的最上方  
		 VideoServer.setGravity(Gravity.TOP);  
		 //改变默认的单行模式  
		 VideoServer.setSingleLine(false);  
		 //水平滚动设置为False  
		 VideoServer.setHorizontallyScrolling(false);
		 
		 VideoStorePath = (Spinner) findViewById(R.id.VideoStorePath);
		 
		 rtsplist = (ListView) findViewById(R.id.rtsplist);
		 
		 addrtspbut = (ImageButton) findViewById(R.id.addrtspbut);
	}
	
	
	/**
	 * 填充数据
	 */
	public void setData(){
		
		SerializableMap myMap=SerializableMap.getSerializableMap();
		
		getmap = myMap.getH3Map();
		
		String dhcp = "";
		if(getmap.get("Dhcp").equals(true)){
			dhcp = "DHCP";
		}else{
			dhcp = "手动";
		}
		 setSpinnerItemSelectedByValue(mSpinner,dhcp);//ip获取方式
		 Ip.setText(getmap.get("Ip")+"");//IP地址
		 MAC.setText(getmap.get("Mac")+"");
		 Mask.setText(getmap.get("Mask")+"");//子网掩码
		 
		 if(!(getmap.get("Getway")+"").equals("null")){
			 GetWay.setText(getmap.get("Getway")+"");//网关
		 }
		 editsn.setText(getmap.get("DoorCtrlSN")+"");//序列号
		 //setSpinnerItemSelectedByValue(mjtype,getmap.get("DoorCtrlType")+"");//门禁类型
		 videotime.setText(getmap.get("VideoCapDuration")+"");	//录像时长
		 
		 if(!(getmap.get("DeviceCtrlServer")+"").equals("")){
			 
			 VideoServer.setText(getmap.get("DeviceCtrlServer")+"");	//平台地址
		 }
		 
		 setSpinnerItemSelectedByValue(VideoStorePath,getmap.get("VideoStorePath")+"");//缓存路径
		
		@SuppressWarnings("unchecked")
		ArrayList<String> rtspUrl =  (ArrayList<String>) getmap.get("VideoUrl");
		
		
			for (int i = 0; i < rtspUrl.size(); i++) {
				
				if(rtspUrl.get(i)!=null){
					
					Map<Object,Object> map = new HashMap<Object,Object>();
					map.put("RtspStr", rtspUrl.get(i));
					
						if(rtspUrl.get(i).equals(getmap.get("OutDoorRtspUrl")+"")){
							map.put("RtspType", "门外抓拍");
						}else if(rtspUrl.get(i).equals(getmap.get("InDoorRtspUrl")+"")){
							map.put("RtspType", "门内抓拍");
						}else{
							map.put("RtspType", "无抓拍");
						}
					
					rtspmap.add(map);
				}
			}
	}
	
	
	/**
	 * 设置输入校验
	 */
	
	/**
	 * 配置Sinner
	 */
	public void initlistSinner(){
		String[] mItems = new String[]{"手动","DHCP"};
		String[] sItems = new String[]{"TF","SATA"};
		String[] jItems = new String[]{"DCTRL_TYPE_YG_NET","DCTRL_TYPE_YG_LOCKER","DCTRL_TYPE_WG_NET"};
		ArrayAdapter<String> m_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		ArrayAdapter<String> j_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, jItems);
		ArrayAdapter<String> s_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, sItems);
		mSpinner.setAdapter(m_Adapter);
		//mjtype.setAdapter(j_Adapter);
		VideoStorePath.setAdapter(s_Adapter);
	}
	
	public void setAdapter(){
		rtspListAdapter = new RtspListAdapter(rtspmap, getApplicationContext());
		rtsplist.setAdapter(rtspListAdapter);
	}
	
	public void InputValidate(){
		form = new Form();
		/**
		 * 非空验证
		 */
		NotEmptyValidator notNull= new NotEmptyValidator(this);
		
		String ipType = (String) mSpinner.getSelectedItem();
		Validate telValidate = null;
		Validate telValidate2 = null;
		Validate telValidate3 = null;
		/*if(ipType.equals("手动")){
			 telValidate = new Validate(editPsw8);
			telValidate.addValidator(notNull);//非空验证
			 telValidate2 = new Validate(editPsw9);
			telValidate2.addValidator(notNull);//非空验证
			
			 telValidate3 = new Validate(editPsw91);
			telValidate3.addValidator(notNull);//非空验证
			
			form.addValidates(telValidate);
			form.addValidates(telValidate2);
			form.addValidates(telValidate3);
		}
		
		
		form.addValidates(telValidate4);
		form.addValidates(telValidate5);
		form.addValidates(telValidate6);
		form.addValidates(telValidate7);
		form.addValidates(telValidate8);
		form.addValidates(telValidate9);
		form.addValidates(telValidate10);
		form.addValidates(telValidate11);*/
	}
	
	/**
	 * 控件监听
	 */
	public void setOnClick(){
		
		/**
		 * 监听Spinner选中状态刷新界面
		 */
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
					 Ip.setEnabled(true);
					 GetWay.setEnabled(true);
					 Mask.setEnabled(true);
					 MAC.setEnabled(true);
				}else{
					/**
					 * 设置不可点击
					 */
					Ip.setEnabled(false);
					GetWay.setEnabled(false);
					Mask.setEnabled(false);
					MAC.setEnabled(false);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		
		rtsplist.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				@SuppressWarnings("unchecked")
				HashMap<Object,Object> map = (HashMap<Object,Object>) rtsplist.getItemAtPosition(arg2);
				Intent intent = new Intent(DevControllerActivityH3.this,AddRtspUrlDiog.class);
				intent.putExtra("postion", arg2);
				intent.putExtra("rtsp", map.get("RtspStr")+"");
				startActivityForResult(intent, 2);
				return false;
			}
		});
		
		addrtspbut.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent3 = new Intent(DevControllerActivityH3.this, AddRtspUrlDiog.class);
				intent3.putExtra("postion", 666);
				startActivityForResult(intent3, 2);
			}
		});
	}

	/**
	 * 点击返回结果
	 * @param view
	 */
	public void OnClickSure(View view) {
		configData = new ConfigData();
		
		boolean IsOK = form.validate();
		if(IsOK){                    
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在上传...", true);
		 	String dhcp = (String) mSpinner.getSelectedItem();//DHCP
		 	String IPstr = "";//IP
			String Maskstr = "";//网关
			String Getwaystr = "";//掩码
			String Mac = "";
		 	Boolean dhcpstr;//请求方式
		 	
		 	if(dhcp.equals("DHCP")){
		 		dhcpstr = true;
		 	}else {
		 		dhcpstr = false;
			}
		 	IPstr = Ip.getText().toString();//IP
		 	Getwaystr = GetWay.getText().toString();//网关
		 	if(Getwaystr.equals("")){
		 		String tmp =  getmap.get("Ip")+"";
		 		int path_1 = tmp.indexOf(".");// 第一个位置
		 		int path_2 = path_1 + tmp.substring(path_1 + 1).indexOf(".") + 1;// 第二个位置
		 		int path_3 = path_2 + tmp.substring(path_2 + 1).indexOf(".") + 1;// 第三个位置
		 		
		 		Getwaystr = tmp.substring(0, path_3+1)+"1";
		 	}
		 	Maskstr = Mask.getText().toString();//掩码
		 	Mac = MAC.getText().toString();
		 	
		 	
			String DoorCtrlSN = editsn.getText().toString();//DoorCtrlSN
			//int DoorCtrlType = SwitchData.SwichDoorType2((String) mjtype.getSelectedItem());
			int Videotime = Integer.parseInt(videotime.getText().toString());
			String VideoServerurl = VideoServer.getText().toString();
			
			List<String> VideoUrllist = new ArrayList<String>();
			int tmp = 0;
			int tmp2 = 0;
			for (int i = 0; i < rtspmap.size(); i++) {
				if(rtspmap.get(i).get("RtspType").equals("门内抓拍")){
					configData.InDoorRtspUrl = rtspmap.get(i).get("RtspStr")+"";
					tmp++;
				}else if(rtspmap.get(i).get("RtspType").equals("门外抓拍")){
					configData.OutDoorRtspUrl = rtspmap.get(i).get("RtspStr")+"";
					tmp2++;
				}
				String aa = rtspmap.get(i).get("RtspStr")+"";
				VideoUrllist.add(rtspmap.get(i).get("RtspStr")+"");
			}
			
			String VideoStorePathstr = (String) VideoStorePath.getSelectedItem();
			
			
			if(tmp > 1 || tmp2 > 1){
				if(progressDialog != null){
					progressDialog.dismiss();
				}
				Toast.makeText(getApplicationContext(), "输入有误,进门抓拍或出门抓拍不能", Toast.LENGTH_SHORT).show();
				return;
			}
			configData.SerialNo = Integer.parseInt(getmap.get("SerialNo")+"");
			configData.Dhcp = dhcpstr;
			configData.IP = IPstr;
			configData.Mac = Mac;
			configData.Getway = Getwaystr;
			configData.Mask = Maskstr;
			configData.DoorCtrlSN = DoorCtrlSN;
			//configData.DoorCtrlType = DoorCtrlType;
			configData.VideoCapDuration = Videotime;
			configData.DeviceCtrlServer = VideoServerurl;
			configData.VideoUrl = VideoUrllist;
			configData.VideoStorePath = VideoStorePathstr;
			
			/**
			 * 开启线程上传
			 */
				new Thread(mThreadLoadApps).start();
			}else{
				Toast.makeText(getApplicationContext(), "输入有误", Toast.LENGTH_SHORT).show();
			}
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
				Toast.makeText(getApplicationContext(), "设置成功！", Toast.LENGTH_SHORT).show();
				finish();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "╮(╯▽╰)╭设置失败！", Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	/**
	 * 异步上传数据
	 */
	private Thread mThreadLoadApps = new Thread(){
		@Override
		public void run() {
			if(H3ConfigClient.configDevice(configData, getmap.get("Ip")+"")){
				handler1.sendEmptyMessage(0);
			}else{
				handler1.sendEmptyMessage(1);
			}
			
			progressDialog.dismiss();
		}
	};
	
	
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
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			Bundle b = data.getExtras();
			if(requestCode == 2 || requestCode == 3){
				Map<Object,Object> tmpmap = new HashMap<Object,Object>();
				tmpmap.put("RtspStr", b.getString("rtsp"));
				tmpmap.put("RtspType", b.getString("rtspType"));
				int postion = b.getInt("postion");
				if(postion != 666){
					rtspmap.remove(postion);
				}
				rtspmap.add(tmpmap);
				rtspListAdapter.notifyDataSetChanged();
				rtsplist.setAdapter(rtspListAdapter);
			}
		}
	}
	
	/**
	 * 取消操作
	 */
	public void OnClickfinish(View view){
		finish();
	}
}
