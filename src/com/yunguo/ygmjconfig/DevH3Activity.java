package com.yunguo.ygmjconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yunguo.ygmjconfig.Adapter.DevListAdapter;
import com.yunguo.ygmjconfig.Adapter.H3ListAdapter;
import com.yunguo.ygmjconfig.Adapter.ODVListAdapter;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.Controller;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.UdpCore.ConfigTransfer;
import com.yunguo.ygmjconfig.UdpCore.SearchYGController;
import com.yunguo.ygmjconfig.Util.HtmlUtil;
import com.yunguo.ygmjconfig.Util.SerializableMap;
import com.yunguo.ygmjconfig.Util.SwitchData;
import com.yunguo.ygmjconfig.VideoController.ConfigData;
import com.yunguo.ygmjconfig.VideoController.H3ConfigClient;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DevH3Activity extends FragmentActivity {
    private TextView notdatetext;
    private TextView h3notdatetext;
    private SearchYGController mSearchController;
    private ProgressDialog progressDialog,progressDialog2;
	private ListView mListView;
	private ListView h3ListView;
	private ListView videoListView;
	private DevListAdapter mAdapter;
	private H3ListAdapter dAdapter;
	private ODVListAdapter oAdapter;
	private LinearLayout title;
	private Button subBtn,upsuo;
	private List<Controller> mWGList = new ArrayList<Controller>();
	private List<Map<Object,Object>> mH3list = new ArrayList<Map<Object,Object>>();
	private List<Map<String,String>> mODVlist = new ArrayList<Map<String,String>>();
	private String mesg;
	private String tmpvalue;
	private SharedPreferences sharedPreferences;
	private String HouseId;
	private boolean flas= false;
	private Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_editdev);
		
		intent = getIntent();
		title = (LinearLayout) findViewById(R.id.title);
		subBtn = (Button) findViewById(R.id.subBtn);
		upsuo = (Button) findViewById(R.id.upsuo);
		
		if(intent != null){
			tmpvalue = intent.getExtras().get("values").toString();
			if(tmpvalue.equals("0") || tmpvalue.equals("3")){
				title.setVisibility(View.GONE);
			}else if(tmpvalue.equals("1")){
				title.setVisibility(View.VISIBLE);
				subBtn.setVisibility(View.VISIBLE);
				upsuo.setVisibility(View.GONE);
			}else if(tmpvalue.equals("4")){	
				title.setVisibility(View.GONE);
			}else if(tmpvalue.equals("5")){
				subBtn.setVisibility(View.GONE);
				upsuo.setVisibility(View.VISIBLE);
				HouseId = intent.getExtras().get("houseId").toString();
			}
		}
		
		//初始化控件
		initview();
				
		//初始化list
		initListView();
		
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在获取...", true);
		 
	      // 开始动态加载线程
	     mThreadLoadApps.start();
	}
	
	/**
	 * 初始化控件
	 */
	public void initview(){
		
		h3ListView = (ListView) findViewById(R.id.H3list);
		mListView = (ListView) findViewById(R.id.RKElist);
		videoListView = (ListView) findViewById(R.id.VODlist);
		notdatetext = (TextView) findViewById(R.id.notdatetext);
		h3notdatetext = (TextView) findViewById(R.id.h3notdatetext);
	}
	
	/**
	 * 初始化ListView
	 */
	private void initListView() {
		
		mAdapter = new DevListAdapter(mWGList,this);
		mListView.setAdapter(mAdapter);
		
		dAdapter = new H3ListAdapter(mH3list,this);
		h3ListView.setAdapter(dAdapter);
		
		/*oAdapter = new ODVListAdapter(mODVlist,this);
		videoListView.setAdapter(oAdapter);*/
		/**
		 * listview监听
		 */
		setlistOnClick();
	}
	
	/**
	 * 设置listview监听
	 */
	public void setlistOnClick(){

		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				Controller map=(Controller) mListView.getItemAtPosition(arg2);
				if(!map.getState().equals("离线")){
					Intent intent = new Intent(DevH3Activity.this, DevControllerActivitySub.class);
					SerializableMap myMap=SerializableMap.getSerializableMap();
					myMap.setMap(map);//将map数据添加到封装的myMap中
					startActivityForResult(intent, 0);
				}
				return false;
			}
		});
		
		h3ListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Intent intent2 = new Intent(DevH3Activity.this, DevControllerActivityH3.class);
				
				@SuppressWarnings("unchecked")
				HashMap<Object,Object> map=(HashMap<Object,Object>)h3ListView.getItemAtPosition(arg2);
				SerializableMap myMap=SerializableMap.getSerializableMap();
                myMap.setH3Map(map);//将map数据添加到封装的myMap中
				
				startActivityForResult(intent2, 0);
				return false;
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
				mAdapter.notifyDataSetChanged();
				mListView.setAdapter(mAdapter);
				notdatetext.setVisibility(View.GONE);
				break;
			case 1:
				notdatetext.setVisibility(View.VISIBLE);
				break;
			case 2:
				dAdapter.notifyDataSetChanged();
				h3ListView.setAdapter(dAdapter);
				h3notdatetext.setVisibility(View.GONE);
				break;
			case 3:
				h3notdatetext.setVisibility(View.VISIBLE);
				break;
			case 4:
				progressDialog2.dismiss();
				
				String message = "设备绑定成功";
				if(!flas){
					message = "设备绑定失败";
				}
				
				if (!mesg.equals("操作失败")) {
					InmarsatSerialNumber.getInstance().setDev_SN("0");
					Intent intent = new Intent(DevH3Activity.this,MainActivity.class);
					startActivity(intent);
				}else{
					Toast.makeText(getApplicationContext(), mesg+"\r\n"+message, Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	};
	
	
	/**
	 * 请求网络数据
	 */
	private Thread mThreadLoadApps = new Thread(){
		@Override
		public void run() {
			getSubData();
			getH3Data();
			progressDialog.dismiss();
		}
	};
	
	/**
	 * 手动刷新
	 */
	@SuppressWarnings("deprecation")
	public void RefreshOnclick(View view){
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在获取...", true);
	      // 开始动态加载线程
		new Thread(mThreadLoadApps).start();
	}
	
	@SuppressWarnings("unused")
	public void getSubData(){
		mSearchController = new SearchYGController();
		List<Controller> list = mSearchController.SearchControllers();
		List<Map<String,String>> listSn =  InmarsatSerialNumber.getInstance().getHouseSn();
		mWGList.clear();
		if(tmpvalue.equals("5")){
			if(listSn.size() > 0){
				if(list != null){
					List<Controller> listcontroller = IsSn(list,listSn);
					if(listcontroller.size()>0){
						mWGList.addAll(listcontroller);
						handler1.sendEmptyMessage(0);
					}else{
						List<Controller> list2 = new ArrayList<Controller>();
						for (int j = 0; j < listSn.size(); j++) {
							String sn = listSn.get(j).get("SerialNo");
							Controller controller = new Controller(1, "", Long.parseLong(sn));
							controller.setVersion("");
							controller.setState("离线");
							controller.setOpenDelay("");
							controller.setBeeper("");
							controller.setCloseToRemindDelay("");
							controller.setCtrlType("");
							list2.add(controller);
						}
						mWGList.addAll(list2);
						handler1.sendEmptyMessage(0);
					}
				}else{
					List<Controller> list2 = new ArrayList<Controller>();
					for (int j = 0; j < listSn.size(); j++) {
						String sn = listSn.get(j).get("SerialNo");
						Controller controller = new Controller();
						controller.setmDevSN(Long.parseLong(sn));
						controller.setVersion("");
						controller.setState("离线");
						controller.setOpenDelay("");
						controller.setBeeper("");
						controller.setCloseToRemindDelay("");
						controller.setCtrlType("");
						
						list2.add(controller);
					}
					mWGList.addAll(list2);
					handler1.sendEmptyMessage(0);
				}
			}else{
				handler1.sendEmptyMessage(1);
				System.out.println("没有门禁设备");
			}
		}else if(list.size() > 0){
			mWGList.addAll(list);
			handler1.sendEmptyMessage(0);
		}else{
			handler1.sendEmptyMessage(1);
			System.out.println("没有门禁设备");
		}
		
		
	}
	
	
	public List<Controller> IsSn(List<Controller> list,List<Map<String,String>> listSn){
		
		List<Controller> listcontroller = new ArrayList<Controller>();
		boolean tmp = true;
		for(int i = 0; i < listSn.size(); i++){
			tmp = false;
			for (int j = 0; j < list.size(); j++) {
				Controller controller = list.get(j);
				String str = controller.getmDevSN()+"";
				
				if(str.equals(listSn.get(i).get("SerialNo"))){
					
					listcontroller.add(controller);
					tmp = true;
				}
			}
			if(!tmp){
				Controller controller = new Controller();
				String sn = listSn.get(i).get("SerialNo");
				System.out.println("sn   =======   " + sn);
				controller.setmDevSN(Long.parseLong(sn));
				controller.setVersion("");
				controller.setmDevIP("");
				controller.setState("离线");
				controller.setOpenDelay("");
				listcontroller.add(controller);
			}
		}
		return listcontroller;
	}
	
	@SuppressWarnings("unused")
	public void getH3Data(){
		ArrayList<ConfigData> pdatas = H3ConfigClient.searchDevice();
		mH3list.clear(); 
		if(pdatas.size() <= 0){
			handler1.sendEmptyMessage(3);
			System.out.println("没有H3设备");
			return;
		}
		for (int i = 0; i < pdatas.size(); i++) {
			ConfigData p = pdatas.get(i);
			Map<Object,Object> map = new HashMap<Object,Object>();
			map.put("SerialNo", p.SerialNo);
			map.put("Dhcp", p.Dhcp);
			map.put("Ip", p.IP);
			map.put("Mac", p.Mac);
			map.put("Mask", p.Mask);
			map.put("Getway", p.Getway);
			map.put("DoorCtrlSN", p.DoorCtrlSN);
			//map.put("DoorCtrlType", SwitchData.SwichDoorType(p.DoorCtrlType+""));
			map.put("VideoCapDuration", p.VideoCapDuration);
			map.put("VideoServer", p.CtrlServer);
			map.put("VideoUrl", p.VideoUrl);
			map.put("VideoStorePath", p.VideoStorePath);
			map.put("OutDoorRtspUrl", p.OutDoorRtspUrl);
			map.put("InDoorRtspUrl", p.InDoorRtspUrl);
			
			mH3list.add(map);
		}
		handler1.sendEmptyMessage(2);
	}
	
	public void endButtonOnClick(View view){
		InmarsatSerialNumber.getInstance().setDev_SN("0");
		finish();
	}
	/**
	 * 上传数据
	 * @param view
	 */
	public void postDataOnClick(View view){
			//显示等待框
		if(!InmarsatSerialNumber.getInstance().getDev_SN().equals("0")){
			progressDialog2 = ProgressDialog.show(DevH3Activity.this, "请稍等", "正在提交...", true);
			new Thread(mThreadLoadApps2).start();
		}else{
			Toast.makeText(getApplicationContext(), "请选定一个设备", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 更换锁
	 * @param view
	 */
	public void AddDataOnClick(View view){
		upsuo.setVisibility(View.GONE);
		subBtn.setVisibility(View.VISIBLE);
		tmpvalue = "66666";
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在获取...", true);
		 // 开始动态加载线程
	     new Thread(mThreadLoadApps).start();
	}
	
	/**
	 * 请求网络数据
	 */
	private Thread mThreadLoadApps2 = new Thread(){
		@Override
		public void run() {
			
			String ret = "";
			String intentvalues = intent.getExtras().getString("values");
			
			if(intentvalues.equals("6")){
				
					Map<String,String> map4 = new HashMap<String, String>();
					map4.put("SN",InmarsatSerialNumber.getInstance().getDev_SN());//序列号
					map4.put("readprotocol",SwitchData.Swithredstr(InmarsatSerialNumber.getInstance().getDe_SN()));//读头协议
					map4.put("UserName", sharedPreferences.getString("userName", ""));//用户名
					map4.put("HouseId", intent.getExtras().getString("houseId"));//房屋Id
					JSONObject jsonObject = new JSONObject(map4);
					ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("AppAddLock"),jsonObject.toString());
				
				}else if(tmpvalue.equals("66666")){
					
					Map<String,String> map3 = new HashMap<String, String>();
					sharedPreferences = getSharedPreferences("login_info",MODE_PRIVATE);
					map3.put("SN",InmarsatSerialNumber.getInstance().getDev_SN());//序列号
					map3.put("readprotocol",SwitchData.Swithredstr(InmarsatSerialNumber.getInstance().getDe_SN()));//读头协议
					map3.put("UserName", sharedPreferences.getString("userName", ""));//用户名
					map3.put("HouseId", HouseId);//房屋Id
					JSONObject jsonObject = new JSONObject(map3);
					ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("AppAddLock"),jsonObject.toString());
				}else{
					Map<String,String> map = InmarsatSerialNumber.getInstance().getMap();
					
					
					map.put("SN",InmarsatSerialNumber.getInstance().getDev_SN());//序列号
					map.put("readprotocol",SwitchData.Swithredstr(InmarsatSerialNumber.getInstance().getDe_SN()));//读头协议
					
					JSONObject jsonObject = new JSONObject(map);
						System.out.println("添加房屋数据"+jsonObject.toString());
					ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlAddhouse"),jsonObject.toString());
				}
			try {
				JSONObject json = new JSONObject(ret);
				mesg = (String) json.get("message");
			} catch (JSONException e) {
				e.printStackTrace();
			}
			
			handler1.sendEmptyMessage(4);
		}
	};
	public Map<String,String> controllerdata(Controller controller){
		    
		    Map<String,String> controller1 = new HashMap<String, String>();
		    controller1.put("Mask", controller.getMask());
		    controller1.put("Getway", controller.getGetway());
		    controller1.put("MAC", controller.getMAC());
		    controller1.put("Version", controller.getVersion());
		    controller1.put("DoorNo", controller.getDoorNo());
		    controller1.put("CtrlType", controller.getCtrlType());
		    controller1.put("OpenDelay", controller.getOpenDelay());
		    controller1.put("DoorEnable", controller.getDoorEnable());
		    controller1.put("IsSneak", controller.getIsSneak());
		    controller1.put("PrivilegeType", controller.getPrivilegeType());
		    controller1.put("Beeper", controller.getBeeper());
		    controller1.put("IsTag", controller.getIsTag());
		    controller1.put("CloseToRemindDelay", controller.getCloseToRemindDelay());
		    controller1.put("UnlockAlarm", controller.getUnlockAlarm());
		    return controller1;
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
		if(progressDialog2!=null){
			progressDialog2.dismiss();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//显示等待框
		 progressDialog = ProgressDialog.show(this, "请稍等", "正在获取...", true);
	      // 开始动态加载线程
		new Thread(mThreadLoadApps).start();
	}
	  
}
