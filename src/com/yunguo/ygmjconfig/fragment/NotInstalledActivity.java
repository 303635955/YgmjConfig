package com.yunguo.ygmjconfig.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.yunguo.ygmjconfig.DevH3Activity;
import com.yunguo.ygmjconfig.HouseAddActivity;
import com.yunguo.ygmjconfig.HouseManageActivity;
import com.yunguo.ygmjconfig.R;
import com.yunguo.ygmjconfig.Adapter.HouseInstallListAdapter;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.HtmlUtil;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


public class NotInstalledActivity extends Fragment{
	
	private View view;
	
	private HouseInstallListAdapter houseInstallListAdapter;
	private PullToRefreshListView Houseinstall;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.housemanage_page2,null);
		
		initView();
		setAdapter();
		new Thread(mThreadLoadApps).start();
		setOnClick();
		return view;
	}

	public void initView() {
		Houseinstall = (PullToRefreshListView) view.findViewById(R.id.Houseinstall);
		// 设置PullToRefresh  
		Houseinstall.setMode(Mode.BOTH); 
	}
	
	public void setAdapter(){
		houseInstallListAdapter = new HouseInstallListAdapter(getlist(), getActivity());
		Houseinstall.setAdapter(houseInstallListAdapter);
	}
	
	
	public void setOnClick(){
		Houseinstall.setOnItemClickListener(new OnItemClickListener() {

			@SuppressWarnings("unchecked")
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				
				HashMap<String,String> map=(HashMap<String,String>)houseInstallListAdapter.getItem(arg2);
				Intent intent = new Intent(getActivity(),DevH3Activity.class);
				intent.putExtra("values", "6");
				intent.putExtra("houseId", map.get("Id"));
				startActivity(intent);
			}
		});
		
		
		/**
		 * 下拉刷新加载
		 */
		Houseinstall.setOnRefreshListener(new OnRefreshListener2<ListView>(){

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				// 下拉的时候数据重置  
				new Thread(mThreadLoadApps).start();
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				  // 上拉的时候添加选项  
				new Thread(mThreadLoadApps).start();
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
				Houseinstall.onRefreshComplete();
				break;
			case 1:
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
			handler1.sendEmptyMessage(0);
		}
	};
	
	public List getlist(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = new HashMap<String, String>();
		
		map.put("housename", "出租屋");
		map.put("username", "张三");
		map.put("usertel", "17780602344");
		map.put("houseaddress", "白云区Xxx路25号");
		map.put("housetime", "2016/05/17/17:30");
		
		list.add(map);
		list.add(map);
		list.add(map);
		list.add(map);
		list.add(map);
		list.add(map);
		list.add(map);
		list.add(map);
		return list;
	}
}
