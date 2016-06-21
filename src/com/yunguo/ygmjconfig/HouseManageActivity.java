package com.yunguo.ygmjconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yunguo.ygmjconfig.Adapter.AddressAdapter;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.HtmlUtil;
import com.yunguo.ygmjconfig.fragment.NotInstalledActivity;
import com.yunguo.ygmjconfig.fragment.ToBeInstalledActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;

public class HouseManageActivity extends FragmentActivity {
	
	private ArrayList<Fragment> pagerItemList = new ArrayList<Fragment>();
	private ViewPager pager;
	private RadioGroup radioGroup;
	private RadioButton radioButton1,radioButton2;
	private MyAdapter myAdapter;
	private TextView addresstext;
	private ImageView locationimg;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_housemanage);
		initview();
		
		initPageAdapter();
		
		initOnClick();
		
	}
	/**
	 * 初始化组件
	 */
	public void initview() {
		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioButton1 = (RadioButton) findViewById(R.id.radiobtu1);
		radioButton2 = (RadioButton) findViewById(R.id.radiobtu2);
		pager = (ViewPager) findViewById(R.id.Housemanageparkpager);
		locationimg = (ImageView) findViewById(R.id.locationimg);
		addresstext = (TextView) findViewById(R.id.addresstext);
		addresstext.setText(InmarsatSerialNumber.getInstance().getUserAddressId().get(0).get("Name"));//设置初始显示地址
	}
	
	public void initPageAdapter() {
		pagerItemList.add(new ToBeInstalledActivity());
		pagerItemList.add(new ToBeInstalledActivity());
		myAdapter = new MyAdapter(getSupportFragmentManager(), pagerItemList);
		pager.setAdapter(myAdapter);
		
	}
	/**
	 * 添加监听
	 */
	public void initOnClick() {
		/**
		 * RadioButton监听
		 */
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == radioButton1.getId()){
					pager.setCurrentItem(0);
				}else{
					pager.setCurrentItem(1);
				}
			}
		});
		
		locationimg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent3 = new Intent(HouseManageActivity.this, AdressDlogActivity.class);
				startActivityForResult(intent3, 1);
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(data != null){
			Bundle b = data.getExtras();
			addresstext.setText(b.getString("address"));
			InmarsatSerialNumber.getInstance().setAddhouseaddressId(b.getString("addressId"));
			myAdapter.notifyDataSetChanged();
			pager.setAdapter(myAdapter);
			pager.setCurrentItem(0);
		}
	}
	
	public class MyAdapter extends FragmentPagerAdapter {
		private List<Fragment> list;
		public MyAdapter(FragmentManager fm, List<Fragment> list) {
			super(fm);
			this.list = list;
		}
		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}
		@Override
		public int getCount() {
			return list.size();
		}
	}
	
	
	private MyPageChangeListener myPageChangeListener;
	public void setMyPageChangeListener(MyPageChangeListener l) {
		myPageChangeListener = l;
	}
	public interface MyPageChangeListener {
		public void onPageSelected(int position);
	}
}
