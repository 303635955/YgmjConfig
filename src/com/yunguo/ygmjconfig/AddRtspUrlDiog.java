package com.yunguo.ygmjconfig;

import java.util.Map;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.UrlValidator;
import com.yunguo.ygmjconfig.Util.SplitString;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

public class AddRtspUrlDiog extends Activity{
	private Intent intent;
	private int postion = 666;
	
	private LinearLayout rtsp_edit;
	private LinearLayout brandlinear; 
	private LinearLayout iplinear; 
	private LinearLayout portlinear; 
	private LinearLayout usernamelinear;
	private LinearLayout passlinear; 
	
	private Spinner inputtype_spinner;	//输入类型
	private Spinner doortype_spinner;	//rtsp类型
	private Spinner rtspBrand;			//品牌
	
	private EditText rtspstr;			//rtsp地址
    private EditText RtspIp;			//trspIP
	private EditText RtspProt;			//rtsp端口
	private EditText username;			//rtsp账号
	private EditText passworld;			//rtsp密码
	private Form form;					//校验数据是否合法
	
	private String RtspStr;				//返回：trsp地址
	private String DoorType;			//返回：trsp地址类型
	
	private ImageButton studate;				//提交数据
	private ImageButton stuback;				//取消提交
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_addrtsp);
		
		intent = getIntent();
		findView();
		setAdapter();
		
		if((intent.getExtras().getInt("postion")) != 666){
			postion = intent.getExtras().getInt("postion");
			setData();
		}
		
		setVerify();
		
		setOnClick();
	}
	
	public void findView(){
		rtsp_edit = (LinearLayout) findViewById(R.id.rtsp_edit);
		brandlinear = (LinearLayout) findViewById(R.id.brandlinear);
		iplinear = (LinearLayout) findViewById(R.id.iplinear);
		portlinear = (LinearLayout) findViewById(R.id.portlinear);
		usernamelinear = (LinearLayout) findViewById(R.id.usernamelinear);
		passlinear = (LinearLayout) findViewById(R.id.passlinear);
		
		inputtype_spinner = (Spinner) findViewById(R.id.inputtype_spinner);
		doortype_spinner = (Spinner) findViewById(R.id.doortype_spinner);
		rtspBrand = (Spinner) findViewById(R.id.rtspBrand);
		
		rtspstr = (EditText) findViewById(R.id.rtspstr);
		RtspIp = (EditText) findViewById(R.id.RtspIp);
		RtspProt = (EditText) findViewById(R.id.RtspProt);
		username = (EditText) findViewById(R.id.username);
		passworld = (EditText) findViewById(R.id.passworld);
		
		studate = (ImageButton) findViewById(R.id.studate);
		stuback = (ImageButton) findViewById(R.id.stuback);
	}
	
	public void setData(){
		String RtspStr = intent.getExtras().getString("rtsp");
		
		if(!RtspStr.equals("")){
			rtspstr.setText(RtspStr);//rtsp地址
			if(RtspStr.indexOf("?") != -1){
				setSpinnerItemSelectedByValue(rtspBrand,"大华");//品牌
			}else{
				setSpinnerItemSelectedByValue(rtspBrand,"海康");//品牌 
			}
			SplitString splitString = new SplitString(RtspStr);
			Map<String,String> map = splitString.getPaement();
			RtspIp.setText(map.get("Ipstr"));//IP
			RtspProt.setText(map.get("port"));//端口
			username.setText(map.get("username"));//账户
			passworld.setText(map.get("userpasw"));//密码
		}else{
			
		}
	}
	
	
	public void setAdapter(){
		String[] dItems = new String[]{"无抓拍","门外抓拍","门内抓拍"};
		String[] rTtems = new String[]{"通过参数填写","直接填写地址"};
		String[] bItems = new String[]{"海康","大华"};
		
		ArrayAdapter<String> d_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, dItems);
		ArrayAdapter<String> r_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, rTtems);
		ArrayAdapter<String> b_Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, bItems);
		
		doortype_spinner.setAdapter(d_Adapter);
		inputtype_spinner.setAdapter(r_Adapter);
		rtspBrand.setAdapter(b_Adapter);
	}
	
	public void setVerify(){

		
	    form = new Form();
		/**
		 * 非空验证
		 */
		NotEmptyValidator notNull= new NotEmptyValidator(this);
		
		String SelectInput = (String) inputtype_spinner.getSelectedItem();
		
		if(SelectInput.equals("通过参数填写")){
			Validate telValidate = new Validate(RtspIp);
			telValidate.addValidator(notNull);//非空验证
			Validate telValidate2 = new Validate(RtspProt);
			telValidate2.addValidator(notNull);//非空验证
			Validate telValidate3 = new Validate(username);
			telValidate3.addValidator(notNull);//非空验证
			Validate telValidate4 = new Validate(passworld);
			telValidate4.addValidator(notNull);//非空验证
			form.addValidates(telValidate);
			form.addValidates(telValidate2);
			form.addValidates(telValidate3);
			form.addValidates(telValidate4);
		}else{
			Validate telValidate5 = new Validate(rtspstr);
			telValidate5.addValidator(notNull);//非空验证
			form.addValidates(telValidate5);
		}
			
	}
	
	public void setOnClick(){
		inputtype_spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				String sInfo=arg0.getItemAtPosition(arg2).toString();
				if(sInfo.equals("直接填写地址")){
					brandlinear.setVisibility(View.GONE);
					iplinear.setVisibility(View.GONE);
					portlinear.setVisibility(View.GONE);
					usernamelinear.setVisibility(View.GONE);
					passlinear.setVisibility(View.GONE);
					rtsp_edit.setVisibility(View.VISIBLE);
				}else{
					rtsp_edit.setVisibility(View.GONE);
					brandlinear.setVisibility(View.VISIBLE);
					iplinear.setVisibility(View.VISIBLE);
					portlinear.setVisibility(View.VISIBLE);
					usernamelinear.setVisibility(View.VISIBLE);
					passlinear.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});	
		
		studate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if(form.validate()){
					showSelectedResult();
				}else{
					Toast.makeText(getApplicationContext(), "输入有误", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		stuback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
	
	public void showSelectedResult(){
		DoorType = (String) doortype_spinner.getSelectedItem();
		
		if(inputtype_spinner.getSelectedItem().toString().equals("直接填写地址")){
			RtspStr = rtspstr.getText().toString();
		}else{
			String tmp = (String) rtspBrand.getSelectedItem();
			String str1 = RtspIp.getText().toString();
			String str2 = RtspProt.getText().toString();
			String str3 = username.getText().toString();
			String str4 = passworld.getText().toString();
			if(tmp.equals("海康")){
				RtspStr = "rtsp://"+str3+":"+str4+"@"+str1+":"+str2+"/MPEG-4/ch1/main/av_stream";
			}else{
				RtspStr = "rtsp://"+str3+":"+str4+"@"+str1+":"+str2+"/cam/realmonitor?channel=2&subtype=1";
			}
		}
	/*	
		UrlValidator urlValidator = new UrlValidator(this);
		Validate telValidate = new Validate(RtspStr);
		telValidate.addValidator(urlValidator);//非空验证
*/		Intent intent = new Intent();
		intent.putExtra("rtsp", RtspStr);
		intent.putExtra("rtspType", DoorType);
		intent.putExtra("postion", postion);
		setResult(100,intent);
		finish();
	}
	
	
}
