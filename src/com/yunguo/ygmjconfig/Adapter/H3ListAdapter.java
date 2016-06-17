package com.yunguo.ygmjconfig.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.yunguo.ygmjconfig.R;
import com.yunguo.ygmjconfig.Entity.InmarsatSerialNumber;
import com.yunguo.ygmjconfig.Util.SwitchData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 */
public class H3ListAdapter extends BaseAdapter {
	 private List<Map<Object,Object>> list = new ArrayList<Map<Object,Object>>();
    private Context context;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个  

    public H3ListAdapter(List<Map<Object,Object>> list,Context context){
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
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
            viewHolder =new ViewHolder();

            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.h3_HostAddress);
            viewHolder.textView9 = (TextView) convertView.findViewById(R.id.h3_Sn);
            viewHolder.textView12= (TextView) convertView.findViewById(R.id.h3_VideoSaveWay);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.radio_btn2);  
        viewHolder.rdBtn = radio;
        
        
     
    //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中      
        viewHolder.rdBtn.setOnClickListener(new View.OnClickListener() {  
                 
               public void onClick(View v) {  
                   
            	   /**
            	    * 设置选中的序列号
            	    */
            	   HashMap map = (HashMap) list.get(position);
            	   InmarsatSerialNumber.getInstance().setH3_SN(map);
            	   
                   //重置，确保最多只有一项被选中  
                   for(String key:states.keySet()){  
                       states.put(key, false);  
                         
                   }  
                   states.put(String.valueOf(position), radio.isChecked());  
                   H3ListAdapter.this.notifyDataSetChanged();  
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
        
		Map<Object,Object> map = list.get(position);
        
        viewHolder.textView1.setText(map.get("Ip")+"");
        viewHolder.textView9.setText(map.get("SerialNo")+"");
        viewHolder.textView12.setText(map.get("VideoStorePath")+"");
        

        return convertView;
    }

    class ViewHolder{
        TextView textView1,textView9,textView12;
        RadioButton rdBtn;
    }
}
