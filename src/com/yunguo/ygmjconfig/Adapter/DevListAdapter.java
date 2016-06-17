package com.yunguo.ygmjconfig.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yunguo.ygmjconfig.R;
import com.yunguo.ygmjconfig.Entity.Controller;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 * 
 */
public class DevListAdapter extends BaseAdapter {
	private List<Controller> list = new ArrayList<Controller>();
    private Context context;
    private boolean selecd = true;
    private  ViewHolder viewHolder = null;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个  

    public DevListAdapter(List<Controller> list,Context context){
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
       
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.devitem_listview, null);
            viewHolder =new ViewHolder();
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.radio_btn1);  
        viewHolder.rdBtn = radio;
        
      //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中      
        viewHolder.rdBtn.setOnClickListener(new View.OnClickListener() {  
                 
               public void onClick(View v) {  
                   //选中时获取当前Item的序列号wgController.getmDevSN()
            	   Controller wgController = (Controller) list.get(position);
            	   
            	   InmarsatSerialNumber.getInstance().setDev_SN(wgController.getmDevSN()+"");
            	   
                   //重置，确保最多只有一项被选中  
                   for(String key:states.keySet()){  
                       states.put(key, false);  
                   }  
                   if(selecd){
                	   states.put(String.valueOf(position),radio.isChecked());  
                	   selecd = false;
                   }else{
                	   InmarsatSerialNumber.getInstance().setDev_SN("0");
                	   selecd = true;
                   }
                   DevListAdapter.this.notifyDataSetChanged();  
               }  
           });  
          
           boolean res=false;  
           if(states.get(String.valueOf(position)) == null || states.get(String.valueOf(position))== false){  
               res=false;  
               states.put(String.valueOf(position), false);  
           }  
           else  
               res = true;  
             
           viewHolder.rdBtn.setChecked(res); 
        
        
        Controller wgController = (Controller) list.get(position);
        viewHolder.textView1 = (TextView) convertView.findViewById(R.id.sn_num);
        viewHolder.textView2 = (TextView) convertView.findViewById(R.id.version);
        viewHolder.textView3 = (TextView) convertView.findViewById(R.id.ip);
        viewHolder.textView4 = (TextView) convertView.findViewById(R.id.controltype);
        viewHolder.textView5 = (TextView) convertView.findViewById(R.id.opendoortime);
        
        viewHolder.textView6 = (TextView) convertView.findViewById(R.id.dhcp);
        viewHolder.textView7 = (TextView) convertView.findViewById(R.id.mask);
        viewHolder.textView8 = (TextView) convertView.findViewById(R.id.butopen);
        viewHolder.textView9 = (TextView) convertView.findViewById(R.id.thedive);
        
        viewHolder.textView10 = (TextView) convertView.findViewById(R.id.readtype);
        viewHolder.textView11 = (TextView) convertView.findViewById(R.id.readprotocol);
        
        

	   	viewHolder.textView1.setText(String.valueOf(wgController.getmDevSN()));//序列号
        viewHolder.textView2.setText(wgController.getVersion());//版本号
        viewHolder.textView3.setText(wgController.getmDevIP());//IP
        viewHolder.textView4.setText(wgController.getState());//控制类型
        viewHolder.textView5.setText(wgController.getOpenDelay());//开门延时
        
        //viewHolder.textView5.setText(wgController.getDHCP());//获取方式
        viewHolder.textView7.setText(wgController.getMask());//子网掩码
        viewHolder.textView8.setText(wgController.getDoorEnable());//按钮开门
        viewHolder.textView9.setText(wgController.getIsSneak());//防潜入
        viewHolder.textView10.setText("");//读头类型
        viewHolder.textView11.setText("");//读头协议类型

        return convertView;
    }

    class ViewHolder{
        TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7,textView8,textView9,textView10,textView11;
        RadioButton rdBtn;
    }
}
