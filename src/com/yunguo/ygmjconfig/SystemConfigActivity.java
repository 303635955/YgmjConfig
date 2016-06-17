package com.yunguo.ygmjconfig;


import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.throrinstudio.android.common.libs.validator.validator.RegExpValidator;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Util.DataEncrypt;
import com.yunguo.ygmjconfig.Util.HtmlUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SystemConfigActivity extends Activity {
	
	private EditText UserPasswd,NewUserPasswd,NewUserPasswd2;
	private Button sub_userpasswd;
	private SharedPreferences sharedPreferences;
	private String UserName,UserPassword,NewUserPassword,NewUserPassword2;
	private DataEncrypt dataEncrypt;
	private String messge;
	private Form form;
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_systemconfig);
		sharedPreferences = getSharedPreferences("login_info",MODE_PRIVATE);
		dataEncrypt =  new DataEncrypt();
		Init();
		validateform();
	}
	private void Init(){
		UserPasswd = (EditText) findViewById(R.id.UserPasswd);
		NewUserPasswd = (EditText) findViewById(R.id.NewUserPasswd);
		NewUserPasswd2 = (EditText) findViewById(R.id.NewUserPasswd2);
		sub_userpasswd = (Button) findViewById(R.id.sub_userpasswd);
		sub_userpasswd.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isOk = form.validate();
				if(isOk){
					if(sharedPreferences.getBoolean("AUTO_ISCHECK", false)){
						UserName = sharedPreferences.getString("userName", "");
					}
					UserPassword = UserPasswd.getText().toString();
					NewUserPassword = NewUserPasswd.getText().toString();
					//显示等待框
					progressDialog = ProgressDialog.show(SystemConfigActivity.this, "请稍等", "正在提交...", true);
					new Thread(thread).start();
				}
			}
		});
	}
	
	public void validateform(){
		
		form = new Form();
		/**
		 * 非空验证
		 */
		NotEmptyValidator notNull= new NotEmptyValidator(this);
		
		/*RegExpValidator IdCarIs = new RegExpValidator(this,R.string.validator_passwd);
		IdCarIs.setStrvalue(NewUserPasswd.getText().toString());*/
		
		Validate telValidate = new Validate(UserPasswd);
		telValidate.addValidator(notNull);//非空验证
		
		Validate telValidate2 = new Validate(NewUserPasswd);
		telValidate2.addValidator(notNull);//非空验证
		
		Validate telValidate3 = new Validate(NewUserPasswd2);
		telValidate3.addValidator(notNull);//非空验证
		//telValidate3.addValidator(IdCarIs);
		
		form.addValidates(telValidate);
		form.addValidates(telValidate2);
		form.addValidates(telValidate3);
	}
	
	 private Handler handler = new Handler(){
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 	progressDialog.dismiss();
			 switch (msg.what) {
				case 0:
					Toast.makeText(getApplicationContext(),messge, Toast.LENGTH_SHORT).show();
					sharedPreferences.edit().clear().commit();//清空用户缓存
					AlertDialog.Builder builder = new AlertDialog.Builder(
							SystemConfigActivity.this);

					builder.setIcon(R.drawable.ic_launcher);
					builder.setTitle("修改成功，请从新登录！");
					builder.setPositiveButton("确认",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int whichButton) {
									Intent intent = new Intent(SystemConfigActivity.this,LoginActivity.class);
									startActivity(intent);
								}
							});
					builder.create().show();
					break;
				case 1:
					Toast.makeText(getApplicationContext(),messge+",请检查原密码是否正确或网络是否打开", Toast.LENGTH_SHORT).show();
				break;
			}
		 };
	 };
	
	private Thread thread = new Thread(){
		@Override
		public void run() {
			 Map<String,String> map = new HashMap<String,String>();
			 map.put("UserName", UserName);
			 map.put("UserPasswd", dataEncrypt.CBCEncrypt(UserPassword));
			 map.put("NewUserPasswd", dataEncrypt.CBCEncrypt(NewUserPassword));
			 JSONObject jsonObject = new JSONObject(map);
			 String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("AppDetailPawwsd"),jsonObject.toString());
			 JSONObject jsonObject2;
			try {
				jsonObject2 = new JSONObject(ret);
				messge = jsonObject2.getString("message");
				String retmsg = jsonObject2.getString("ret");
				if(retmsg.equals("1")){
					handler.sendEmptyMessage(0);
				}else{
					handler.sendEmptyMessage(1);
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			 
		};
	};
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
}
