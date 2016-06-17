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
import com.yunguo.ygmjconfig.Util.SplitString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 */
public class ODVListAdapter extends BaseAdapter {
    private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private Context context;
    private ViewHolder viewHolder = null;
    HashMap<String,Boolean> states=new HashMap<String,Boolean>();//用于记录每个RadioButton的状态，并保证只可选一个  
    public ODVListAdapter(List<Map<String,String>> list,Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_video_listview, null);
            viewHolder =new ViewHolder();

            viewHolder.textView1 = (TextView) convertView.findViewById(R.id.maker_name);
            viewHolder.textView2 = (TextView) convertView.findViewById(R.id.ip);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        final RadioButton radio=(RadioButton) convertView.findViewById(R.id.radio_btn3);  
        viewHolder.rdBtn = radio;
        
        
     
    //当RadioButton被选中时，将其状态记录进States中，并更新其他RadioButton的状态使它们不被选中      
        viewHolder.rdBtn.setOnClickListener(new View.OnClickListener() {  
                 
               public void onClick(View v) {
            	   
            	   InmarsatSerialNumber.getInstance().setODV_SN("ss");
                   
                   //重置，确保最多只有一项被选中  
                   for(String key:states.keySet()){  
                       states.put(key, false);  
                         
                   }  
                   states.put(String.valueOf(position), radio.isChecked());  
                   ODVListAdapter.this.notifyDataSetChanged();  
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
        
        HashMap<String, String> map = (HashMap<String,String>) list.get(position);
        
        String temp = map.get("RtspUrl");
        
        if(temp.indexOf("?")!=-1){
		   viewHolder.textView1.setText("大华");
		  }else{
		   viewHolder.textView1.setText("海康");
		  }
        
        SplitString splitstr = new SplitString(map.get("RtspUrl"));
        viewHolder.textView2.setText(splitstr.getIp());

        return convertView;
    }

    class ViewHolder{
        TextView textView1,textView2;
        RadioButton rdBtn;
    }
}
