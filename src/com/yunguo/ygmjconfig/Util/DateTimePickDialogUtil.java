package com.yunguo.ygmjconfig.Util;

import java.util.Calendar;  
import com.yunguo.ygmjconfig.R;
import android.app.Activity;  
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;  
import android.widget.TimePicker.OnTimeChangedListener;  
import android.widget.Toast;
public class DateTimePickDialogUtil extends Activity {  
	
	 private int hour;
	 private int minute;
	 private TimePicker timePicker;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.common_datetime);
    	
    	  timePicker = (TimePicker)findViewById(R.id.timepicker);
    	  timePicker.setIs24HourView(true);
    		    // 获取当前的小时、分钟
		    Calendar c = Calendar.getInstance();
		    hour = c.get(Calendar.HOUR);
		    minute = c.get(Calendar.MINUTE);
		    
		    // 为TimePicker指定监听器
		    timePicker.setOnTimeChangedListener(new OnTimeChangedListener(){

		      @Override
		      public void onTimeChanged(TimePicker view, int hourOfDay, int minute){
		    	  DateTimePickDialogUtil.this.hour = hourOfDay;
		    	  DateTimePickDialogUtil.this.minute = minute;
		      }
		    });
    	}
    
	    public void showTime(View view) {
	    	Intent intent = new Intent();
			String TimeStr = hour+":"+minute;
			intent.putExtra("TimeStr",TimeStr);
			setResult(100,intent);
			finish();
		}

  
}