package com.yunguo.ygmjconfig;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * 欢迎界面
 * @author Administrator
 *
 */
public class WelcomeActivity extends Activity{
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_welcome);
			
			final Intent it = new Intent(this, LoginActivity.class);
			it.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			Timer timer = new Timer(); 
			TimerTask task = new TimerTask() {  
			    @Override  
			    public void run() {   
			    startActivity(it); //执行  
			    finish();
			     } 
			 };
			timer.schedule(task, 1000 * 2); //10秒后
		}
}
