package com.yunguo.ygmjconfig.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.yunguo.ygmjconfig.DevH3Activity;
import com.yunguo.ygmjconfig.R;
import com.yunguo.ygmjconfig.Adapter.MessgeListAdapter;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.HtmlUtil;
import com.yunguo.ygmjconfig.Util.SwitchData;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;


public class ToBeInstalledActivity extends Fragment{
	
	private View view;
	private PullToRefreshListView HouseMessge;
	private MessgeListAdapter messgeListAdapter;
	private ProgressDialog progressDialog;
	private String codestr;
	private static int page = 1;
	private static boolean flas = true;
	private String Id;
	private List<Map<String,String>> mHouseMessge = new ArrayList<Map<String,String>>();
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.housemanage_page1,null);
		
		/**
		 * 初始化组件
		 */
		initView();
		/**
		 * 初始化适配器
		 */
		setAdapter();
		
		//显示等待框
		 progressDialog = ProgressDialog.show(getActivity(), "请稍等", "正在获取...", true);
		
		//开启加载数据线程
		new Thread(mThreadLoadApps).start();
		setOnClick();
		
		return view;
	}

	public void initView() {
		HouseMessge = (PullToRefreshListView) view.findViewById(R.id.HouseMessge);
		// 设置PullToRefresh  
		HouseMessge.setMode(Mode.BOTH); 
	}
	
	
	public void setAdapter(){
		messgeListAdapter = new MessgeListAdapter(mHouseMessge, getActivity());
		HouseMessge.setAdapter(messgeListAdapter);
	}
	
	
	
	public void setOnClick(){
		HouseMessge.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				@SuppressWarnings("unchecked")
				HashMap<String,String> map=(HashMap<String,String>)messgeListAdapter.getItem(arg2-1);
				Id = map.get("Id");
				//显示等待框
				progressDialog = ProgressDialog.show(getActivity(), "", "请稍后...", true);
				new Thread(mThreadLoadApps3).start();;
			}
		});
		
		/**
		 * 下拉刷新加载
		 */
		HouseMessge.setOnRefreshListener(new OnRefreshListener2<ListView>(){

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉的时候数据重置  
					flas = true;
					page = 1;
					new Thread(mThreadLoadApps).start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				  // 上拉的时候添加选项  
				flas = false;
				++page;
				System.out.println(page);
				
				new Thread(mThreadLoadApps2).start();
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
				messgeListAdapter.notifyDataSetChanged();
				HouseMessge.setAdapter(messgeListAdapter);
				HouseMessge.onRefreshComplete();
				Toast.makeText(getActivity(), codestr, Toast.LENGTH_SHORT).show();
				break;
			case 1:
				messgeListAdapter.notifyDataSetChanged();
				HouseMessge.onRefreshComplete();
				Toast.makeText(getActivity(), codestr, Toast.LENGTH_SHORT).show();
				break;
			case 2:
				Intent intent = new Intent(getActivity(),DevH3Activity.class);
				intent.putExtra("values", "5");
				intent.putExtra("houseId", Id);
				startActivity(intent);
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
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			Map<String,String> map = new HashMap<String, String>();
			map.put("page", "1");
			map.put("rows", "10");
			map.put("AreaId",InmarsatSerialNumber.getInstance().getAddressId());
			try {
				JSONObject jsonObject = new JSONObject(map.toString());
				String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlQueryhouse"),jsonObject.toString());
				System.out.println("房屋信息 ================"+ret);
				getMessgeList(ret);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			handler1.sendEmptyMessage(0);
			
		}
	};
	
	/**
	 * 请求网络数据
	 */
	private Thread mThreadLoadApps2 = new Thread(){
		@Override
		public void run() {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			
			Map<String,String> map = new HashMap<String, String>();
			map.put("page", page+"");
			map.put("rows", "10");
			map.put("AreaId", InmarsatSerialNumber.getInstance().getAddressId());
			try {
				JSONObject jsonObject = new JSONObject(map.toString());
				String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlQueryhouse"),jsonObject.toString());
				getMessgeList(ret);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			handler1.sendEmptyMessage(1);
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			
		}
	};
	
	/**
	 * 请求网络数据
	 */
	private Thread mThreadLoadApps3 = new Thread(){
		@Override
		public void run() {
			Map<String,String> map = new HashMap<String, String>();
			map.put("page","1");
			map.put("rows", "10");
			map.put("HouseId", Id);
			try {
				JSONObject jsonObject = new JSONObject(map.toString());
				String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlQueryLockinfo"),jsonObject.toString());
				getMessgeList2(ret);
			} catch (JSONException e) {
				e.printStackTrace();
			}
			handler1.sendEmptyMessage(2);
			if(progressDialog != null){
				progressDialog.dismiss();
			}
			
		}
	};
	
	
	public void getMessgeList(String str){
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		try {
			JSONObject jsonObject2 =new JSONObject(str);
			codestr = (String) jsonObject2.get("message");
			JSONArray jsonArray = jsonObject2.getJSONArray("houses"); 
			 for (int i=0;i<jsonArray.length();i++){
				  Map<String,String> map = new HashMap<String, String>();
		          JSONObject jsonObjectSon= (JSONObject)jsonArray.opt(i);
		          map.put("Id", jsonObjectSon.getString("Id"));
		          map.put("AreaId", jsonObjectSon.getString("AreaId"));
		          map.put("Name", jsonObjectSon.getString("Name"));
		          map.put("Address", jsonObjectSon.getString("Address"));
		          map.put("HouseOwnerTel", jsonObjectSon.getString("HouseOwnerTel"));
		          map.put("OwnerId", jsonObjectSon.getString("OwnerId"));
		          map.put("HouseNum", jsonObjectSon.getString("HouseNum"));
		          map.put("HouseOwnerICcard", jsonObjectSon.getString("HouseOwnerICcard"));
		          map.put("Gps", jsonObjectSon.getString("Gps"));
		          map.put("UseageStr", jsonObjectSon.getString("UseageStr"));
		          map.put("RoomCount", jsonObjectSon.getString("RoomCount"));
		          map.put("FloorCount", jsonObjectSon.getString("FloorCount"));
		          map.put("AreaName", jsonObjectSon.getString("AreaName"));
		          map.put("HouseOwner", jsonObjectSon.getString("Owner"));
		          list.add(map);
		          }
				 if(flas){
					 mHouseMessge.clear();
				 }
				 
				 mHouseMessge.addAll(list);
			}catch (JSONException e) {
				e.printStackTrace();
			}
				
	}
	
	
	public void getMessgeList2(String str){
		List<Map<String,String>> SerialNo = new ArrayList<Map<String,String>>();
		try {
			JSONObject jsonObject2 =new JSONObject(str);
			codestr = (String) jsonObject2.get("message");
			JSONArray jsonArray = jsonObject2.getJSONArray("lockers"); 
			SerialNo.clear();
			 for (int i=0;i<jsonArray.length();i++){
				  Map<String,String> map = new HashMap<String, String>();
		          JSONObject jsonObjectSon= (JSONObject)jsonArray.opt(i);
		          map.put("SerialNo", jsonObjectSon.getString("SerialNo"));
		          SerialNo.add(map);
		          }
			 
			 InmarsatSerialNumber.getInstance().setHouseSn(SerialNo);
			}catch (JSONException e) {
				e.printStackTrace();
			}
				
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	
}
