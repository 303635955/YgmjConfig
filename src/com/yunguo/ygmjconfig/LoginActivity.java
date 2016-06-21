package com.yunguo.ygmjconfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.throrinstudio.android.common.libs.validator.Form;
import com.throrinstudio.android.common.libs.validator.Validate;
import com.throrinstudio.android.common.libs.validator.validator.NotEmptyValidator;
import com.yunguo.ygmjconfig.Core.AppCtrl;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.DataEncrypt;
import com.yunguo.ygmjconfig.Util.HtmlUtil;
import com.yunguo.ygmjconfig.Util.UiUtil;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private Form form;
	private EditText editUser,editPsw;
	private Button subBtn;
	private String user,psw;
	private String messge = "";
	private String retmsg = "";
	private boolean flas = true;
	private String tmpstr = "";
	private SharedPreferences sharedPreferences;  
	private ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPreferences = getSharedPreferences("login_info",MODE_PRIVATE);
		
		if(sharedPreferences.getBoolean("AUTO_ISCHECK", false)){
			user = sharedPreferences.getString("userName", "");
	 	    psw = sharedPreferences.getString("password", "");
	 	    flas = false;
	 	 //显示等待框
			progressDialog = ProgressDialog.show(LoginActivity.this,"", "登录中...", true);
	 	    new Thread(mThreadLoadApps).start();
		}
		setContentView(R.layout.activity_login);
		initView();
		IsInputNull();
		OnClickLogin();
	}
	
	public void initView(){
		editUser = (EditText) findViewById(R.id.editUser);
		editPsw = (EditText) findViewById(R.id.editPsw);
		subBtn = (Button) findViewById(R.id.subBtn);
	}
	public void IsInputNull(){
		form = new Form();
		NotEmptyValidator notNull= new NotEmptyValidator(this);
		/**
		 * 非空验证
		 */
		Validate telValidate = new Validate(editUser);
		telValidate.addValidator(notNull);//非空验证
		Validate telValidate2 = new Validate(editPsw);
		telValidate2.addValidator(notNull);//非空验证
		
		form.addValidates(telValidate);
		form.addValidates(telValidate2);
	}
	
	 public void OnClickLogin(){
		 subBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					boolean isOk = form.validate();
					if(isOk){
						 user = editUser.getText().toString();
						 psw = editPsw.getText().toString();
						//显示等待框
						 progressDialog = ProgressDialog.show(LoginActivity.this,"", "登录中...", true);
						 new Thread(mThreadLoadApps).start();
					}
				}
		});
		 
	 }
	 
	 private Handler handler = new Handler(){
		 @Override
		 public void handleMessage(android.os.Message msg) {
			 switch (msg.what) {
				case 0:
					saveAccount(user,psw);
					Toast.makeText(getApplicationContext(), "登录成功！", Toast.LENGTH_SHORT).show();
					Intent intent = new Intent(LoginActivity.this,MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(intent);
					progressDialog.dismiss();
					finish();
					break;
				case 1:
					Toast.makeText(getApplicationContext(), "登录失败，账号或密码错误！", Toast.LENGTH_SHORT).show();
					progressDialog.dismiss();
					break;
				case 2:
					Toast.makeText(getApplicationContext(), "登录失败，请检查网络是否打开！", Toast.LENGTH_SHORT).show();
					progressDialog.dismiss();
					break;
			}
		 };
	 };
	 

		/**
		 * 将此次登陆的账户信息存储下来
		 * */
		@SuppressWarnings("unused")
		private void saveAccount(String username,String password) {
			if(flas){
			    // 获取editor
			    Editor editor = sharedPreferences.edit();
			    // 存入数据
			    editor.putString("userName", username);
			    editor.putString("password", password);
			    editor.putBoolean("AUTO_ISCHECK", true); 
			    // 提交存入文件中
			    editor.commit();
			}
		}
	 
	 private Thread mThreadLoadApps = new Thread(){
		 @Override
		 public void run() {
			 try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			 Map<String,String> map = new HashMap<String,String>();
			 map.put("UserName", user);
			 DataEncrypt dataEncrypt =  new DataEncrypt();
			 map.put("UserPasswd", dataEncrypt.CBCEncrypt(psw));
			 JSONObject jsonObject = new JSONObject(map);
			 String ret = HtmlUtil.PostStringToUrl(AppCtrl.GetObj().GetAppConfig().GetConfig("UrlQueryUserinfo"),jsonObject.toString());
			 try {
				 if(ret != null){
					 JSONObject jsonObject2 = new JSONObject(ret);
					 messge = jsonObject2.getString("message");
					 retmsg = jsonObject2.getString("ret");
					 
					 if(retmsg.equals("1")){
						 JSONArray jsonArray = jsonObject2.getJSONArray("area"); 
						 List<Map<String,String>> list = new ArrayList<Map<String,String>>();
						 for (int i=0;i<jsonArray.length();i++){
							 Map<String,String> map2 = new HashMap<String, String>();
							 JSONObject jsonObjectSon= (JSONObject)jsonArray.opt(i);
							 map.put("Id", jsonObjectSon.getString("AreaId"));
							 map.put("Name", jsonObjectSon.getString("AreaName"));
							 list.add(map);
						 	}
						 /**
						  * 保存用户负责区域
						  */
						 InmarsatSerialNumber.getInstance().setUserAddressId(list);
						 InmarsatSerialNumber.getInstance().setAddressId(list.get(0).get("Id"));
						 handler.sendEmptyMessage(0);
					 }else{
						 handler.sendEmptyMessage(1);
					 }
					 
				 }else{
					 handler.sendEmptyMessage(2);
				 }
			} catch (JSONException e) {
				e.printStackTrace();
				if(retmsg.equals("")){
					handler.sendEmptyMessage(2);
				}else{
					handler.sendEmptyMessage(1);
				}
			}
			 
		 };
	 };
	 
	 @Override
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
		 
		 if(keyCode == KeyEvent.KEYCODE_BACK){
			 AlertDialog.Builder builder = new AlertDialog.Builder(
					 LoginActivity.this);

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

				return true;
			} else {
				return super.onKeyDown(keyCode, event);
			}
	 }
	 
	 @Override
	protected void onDestroy() {
		super.onDestroy();
		if(progressDialog != null){
			progressDialog.dismiss();
		}
	}
	 
}
