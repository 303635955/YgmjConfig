package com.yunguo.ygmjconfig;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class DevControllerActivityODV extends Activity {

	private Spinner Brand;
	private EditText IpAddress;
	private EditText port;
	
	private EditText editusername;
	private EditText edituserpasw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_devcontrollerodv);
		
		findView();
	}
	
	/**
	 * 查找控件
	 */
	private void findView() {
		Brand = (Spinner) findViewById(R.id.Brand);
		String[] mItems = new String[]{"海康","大华"};
		ArrayAdapter<String> m_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
		Brand.setAdapter(m_Adapter);
		
		IpAddress = (EditText) findViewById(R.id.IpAddress);
		port = (EditText) findViewById(R.id.port);
		editusername = (EditText) findViewById(R.id.editusername);
		edituserpasw = (EditText) findViewById(R.id.edituserpasw);
	}

	/**
	 * 点击返回结果
	 * @param view
	 */
	public void OnClickSure(View view) {
		String brandstr = (String) Brand.getSelectedItem();
		String Ipstr = IpAddress.getText().toString();
		String portstr = port.getText().toString();
		String username = editusername.getText().toString();
		String userpasw = edituserpasw.getText().toString();
		String rtsp = "rtsp://"+username+":"+userpasw+"@"+Ipstr+":"+portstr+"/MPEG-4/ch1/main/av_stream"; 
		Intent intent = new Intent();
		intent.putExtra("rtsp", rtsp);
		setResult(100,intent);
		finish();
	}
	
	/**
	 * 取消操作
	 */
	public void OnClickfinish(View view){
		finish();
	}
}
