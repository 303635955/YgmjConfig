package com.yunguo.ygmjconfig.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yunguo.ygmjconfig.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/26.
 */
public class MessgeListAdapter extends BaseAdapter {
	private List<Map<String,String>> list = new ArrayList<Map<String,String>>();
    private Context context;
    
    private  ViewHolder viewHolder = null;

    public MessgeListAdapter(List<Map<String,String>> list,Context context){
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
            convertView = LayoutInflater.from(context).inflate(R.layout.housemessge_adapeter, null);
            viewHolder =new ViewHolder();

           

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        
        viewHolder.textView1 = (TextView) convertView.findViewById(R.id.housename);
        viewHolder.textView2 = (TextView) convertView.findViewById(R.id.username);
        viewHolder.textView3 = (TextView) convertView.findViewById(R.id.usertel);
        viewHolder.textView4 = (TextView) convertView.findViewById(R.id.houseaddress);
        viewHolder.textView5 = (TextView) convertView.findViewById(R.id.Access);
        viewHolder.textView6 = (TextView) convertView.findViewById(R.id.Network);
        viewHolder.textView7 = (TextView) convertView.findViewById(R.id.Video);
        
        HashMap<String,String> map = (HashMap<String, String>) list.get(position);

        viewHolder.textView1.setText(map.get("Name"));
        viewHolder.textView2.setText(map.get("HouseOwner"));
        viewHolder.textView3.setText(map.get("HouseOwnerTel"));
        viewHolder.textView4.setText(map.get("Address"));
        viewHolder.textView5.setText("未知");
        viewHolder.textView6.setText("未知");
        viewHolder.textView7.setText("未知");

        return convertView;
    }

    class ViewHolder{
        TextView textView1,textView2,textView3,textView4,textView5,textView6,textView7;
    }
}
