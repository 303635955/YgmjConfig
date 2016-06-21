package com.yunguo.ygmjconfig;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.HtmlUtil;
import com.yunguo.ygmjconfig.Util.UiUtil;
import com.yunguo.ygmjconfig.Util.UpAPPVersion;

import android.annotation.SuppressLint;
import android.app.ActivityGroup;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

@SuppressWarnings("deprecation")
public class MainActivity extends ActivityGroup implements OnClickListener{
	private View todayView;
	private View lastListView;
	private RadioButton discussView;
	private View favoritesView;
	private View commentsView;
	private View endsyatem;
	private ImageView topButton;
	private TextView topTv;
	private SlidingMenu sm;
	private LinearLayout centerlinear;
	private ProgressDialog m_progressDlg;
	private String upAppUrl;
	private String AppVersion;
	int mCurrentViewId;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mCurrentViewId = 0;
		
		m_progressDlg =  new ProgressDialog(this);
		m_progressDlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		 // 设置ProgressDialog 的进度条是否不明确 false 就是不设置为不明确     
		m_progressDlg.setIndeterminate(false);
		
		initSlidingMenu();
		/**
		 * 检查是否有新版本
		 */
		UpVerion();
		
		findViews();
	}
	
	public void initSlidingMenu(){
		// 实例化滑动菜单对象
	 	sm = new SlidingMenu(this);  
		// 设置可以左右滑动的菜单
		sm.setMode(SlidingMenu.LEFT);
		// 设置滑动阴影的宽度
		sm.setShadowWidthRes(R.dimen.shadow_width);
		// 设置滑动菜单阴影的图像资源
		sm.setShadowDrawable(null);
		// 设置滑动菜单视图的宽度
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		// 设置渐入渐出效果的值
		sm.setFadeDegree(0.35f);
		// 设置触摸屏幕的模式,这里设置为全屏
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// 设置下方视图的在滚动时的缩放比例
		sm.setBehindScrollScale(0.0f);
		// 附加在Activity上
		sm.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
	        //为侧滑菜单设置布局  
		sm.setMenu(R.layout.layout_menu);
		
	}
	
	
	public void findViews() {
		todayView = findViewById(R.id.tvToday);
		lastListView = findViewById(R.id.tvLastlist);
		discussView = (RadioButton) findViewById(R.id.tvDiscussMeeting);
		favoritesView = findViewById(R.id.tvMyFavorites);
		commentsView = findViewById(R.id.tvMyComments);
		
		centerlinear = (LinearLayout) findViewById(R.id.centerlinear);
		topButton =  (ImageView) findViewById(R.id.topButton);
		topTv = (TextView) findViewById(R.id.topTv);
		endsyatem = findViewById(R.id.endsyatem);
		
		todayView.setOnClickListener(this);
		lastListView.setOnClickListener(this);
		discussView.setOnClickListener(this);
		favoritesView.setOnClickListener(this);
		
		
		discussView.setChecked(true);
		commentsView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);

				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("注销登录？");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								SharedPreferences sharedPreferences = getSharedPreferences("login_info",MODE_PRIVATE);
								sharedPreferences.edit().clear().commit();
								Intent intent = new Intent(MainActivity.this,LoginActivity.class);
								startActivity(intent);
								finish();
							}
						});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});
		
		endsyatem.setOnClickListener(new  OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						MainActivity.this);
				builder.setIcon(R.drawable.ic_launcher);
				builder.setTitle("退出软件");
				builder.setPositiveButton("确认",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								System.exit(0);
							}
						});
				builder.setNegativeButton("取消", null);
				builder.create().show();
			}
		});
		
		topButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				sm.toggle();
				if(sm.isSlidingEnabled()){
					sm.setSlidingEnabled(false);
				}else{
					sm.setSlidingEnabled(true);
				}
			}
		});
		
		
		//设置初始界面
		centerlinear.addView(getLocalActivityManager().startActivity(
				"v1",
				new Intent(MainActivity.this, HouseManageActivity.class).putExtra("values", "0")
						.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
				.getDecorView());
		sm.showContent();
	}
	
	
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		 if(event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			 
			 if(sm.isSlidingEnabled()){
				 sm.toggle();
				 sm.setSlidingEnabled(false);
			 }else{
				 AlertDialog.Builder builder = new AlertDialog.Builder(
						 MainActivity.this);
				 builder.setIcon(R.drawable.ic_launcher);
				 builder.setTitle("退出软件");
				 builder.setPositiveButton("确认",
						 new DialogInterface.OnClickListener() {
					 public void onClick(DialogInterface dialog,
							 int whichButton) {
						 System.exit(0);
					 }
				 });
				 builder.setNegativeButton("取消", null);
				 builder.create().show();
			 }

				return true;
	         }else{
	        	 return super.dispatchKeyEvent(event);
	         }
	}
	

	@Override
	public void onClick(View v) {
		centerlinear.removeAllViews();
		switch (v.getId()) {
		case R.id.tvDiscussMeeting: 
			sm.setSlidingEnabled(true);
			topTv.setText("房屋管理");
			centerlinear.addView(getLocalActivityManager().startActivity(
					"v1",
					new Intent(MainActivity.this, HouseManageActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
			sm.showContent();
			break;
		case R.id.tvToday: 
			sm.setSlidingEnabled(true);
			topTv.setText("房屋登记");
			centerlinear.addView(getLocalActivityManager().startActivity(
					"v1",
					new Intent(MainActivity.this, HouseAddActivity.class).putExtra("values", "0")
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
			sm.showContent();
			break;
		case R.id.tvLastlist:
			sm.setSlidingEnabled(true);
			topTv.setText("设备配置");
			InmarsatSerialNumber.getInstance().setHouseSn(null);//清空临时数据
			centerlinear.addView(getLocalActivityManager().startActivity(
					"v1",
					new Intent(MainActivity.this, DevH3Activity.class).putExtra("values", "4")
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
			sm.showContent();
			break;
		case R.id.tvMyFavorites: 
			topTv.setText("修改密码");
			sm.setSlidingEnabled(true);
			centerlinear.addView(getLocalActivityManager().startActivity(
					"v1",
					new Intent(MainActivity.this, SystemConfigActivity.class)
							.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
					.getDecorView());
			sm.showContent();
			break;
		}
	}
	
	
	/**
	 * 更新版本
	 */
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				 AlertDialog.Builder builder = new AlertDialog.Builder(
							MainActivity.this);
					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("已检查更新，是否现在更新？");
					builder.setPositiveButton("更新",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									 m_progressDlg.setTitle("正在下载");  
			                         m_progressDlg.setMessage("请稍候...");
									UpAPPVersion upapp = new UpAPPVersion(m_progressDlg, upAppUrl, AppVersion, MainActivity.this);
			                         upapp.downFile();
								}
							});
					
					builder.setNegativeButton("暂不更新", 
							new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int whichButton) {
							System.exit(0);
						}
					});
					builder.setCancelable(false);
					AlertDialog alertDialog = builder.create();
					alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener(){

						@Override
						public boolean onKey(DialogInterface dialog,
								int keyCode, KeyEvent event) {
							
							if(keyCode == KeyEvent.KEYCODE_SEARCH){
								return true;
							}else{
								return true;
							}
						}
						
					});
					
					alertDialog.show();
				break;
			}
		};
	};
	
	
	@SuppressWarnings("unused")
	private Thread thread = new Thread(){
		@Override
		public void run() {
			String version = UiUtil.getVerName(MainActivity.this);
			Map<String,String> map = new  HashMap<String,String>();
			map.put("type", "Project");
			map.put("version",version);
			try {
				JSONObject jsonObject = new JSONObject(map.toString());
				String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlUpAppVersion"),jsonObject.toString());
				JSONObject jsonObject2 =new JSONObject(ret);
				String versionCode = (String) jsonObject2.get("match").toString();
				if(versionCode.equals("0")){
					upAppUrl = (String) jsonObject2.get("url");
					AppVersion = (String) jsonObject2.get("version");
					handler.sendEmptyMessage(0);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		};
	};
	
	/**
	 * 检查是否有新版本
	 */
	public void UpVerion(){
		thread.start();
	}
	
}
